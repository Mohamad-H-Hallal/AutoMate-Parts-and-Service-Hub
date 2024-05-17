package com.example.project.View.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DiagnosticsFragment extends BaseFragment {

    private ImageView carDataFilter;
    private CardView carDataCardFilter;
    private AppCompatButton importData;
    private static final String TAG = "DiagnosticsFragment";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;
    private static final String SERVER_ADDRESS = "2C:CF:67:03:A4:11"; // Replace with your server's Bluetooth address
    private static final UUID SERVER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Replace with your server's UUID

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private List<String> fileNames;
    private Spinner daySpinner, monthSpinner, yearSpinner, hourSpinner;

    public DiagnosticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnostics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carDataFilter = view.findViewById(R.id.carDataFilter);
        carDataCardFilter = view.findViewById(R.id.carDataCardFilter);
        importData = view.findViewById(R.id.importData);
        daySpinner = view.findViewById(R.id.daySpinner);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        hourSpinner = view.findViewById(R.id.hourSpinner);

        String[] dayArray = getResources().getStringArray(R.array.day_choices);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dayArray);
        daySpinner.setAdapter(dayAdapter);
        String[] monthArray = getResources().getStringArray(R.array.month_choices);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, monthArray);
        monthSpinner.setAdapter(monthAdapter);
        String[] yearArray = getResources().getStringArray(R.array.year_choice_d);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearArray);
        yearSpinner.setAdapter(yearAdapter);
        String[] hourArray = getResources().getStringArray(R.array.hour_choices);
        ArrayAdapter<String> hourAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, hourArray);
        hourSpinner.setAdapter(hourAdapter);

        // Initialize fileNames list
        fileNames = new ArrayList<>();

        importData.setOnClickListener(v -> {
            if (!isBluetoothEnabled()) {
                requestBluetoothEnable();
                return;
            }
            checkAndRequestPermissions();
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        carDataFilter.setOnClickListener(v -> {
            if (carDataCardFilter.getVisibility() == View.VISIBLE) {
                carDataCardFilter.setVisibility(View.GONE);
                carDataFilter.setBackground(null);
            } else {
                carDataCardFilter.setVisibility(View.VISIBLE);
                carDataFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
            }
        });
    }

    private boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private void requestBluetoothEnable() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
            connectToDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                connectToDevice();
            } else {
                Toast.makeText(getContext(), "Permissions denied. Cannot import data.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_ENABLE_BT
        ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(getContext(), "Bluetooth enablement cancelled. Cannot continue.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(getContext(), "Bluetooth enablement cancelled. Cannot continue.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void connectToDevice() {
        new Thread(() -> {
            try {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(SERVER_ADDRESS);
                bluetoothSocket = device.createRfcommSocketToServiceRecord(SERVER_UUID);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                receiveZipFile(inputStream);
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to Raspberry Pi: " + e.getMessage(), e);
                // Handle connection error gracefully
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Failed to connect to Raspberry Pi", Toast.LENGTH_SHORT).show();
                });
            } finally {
                try {
                    if (bluetoothSocket != null) bluetoothSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing socket: " + e.getMessage(), e);
                }
            }
        }).start();
    }


    private void receiveZipFile(InputStream inputStream) {
        try {
            File storageDir = new File(Environment.getExternalStorageDirectory(), "BluetoothFiles");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            File zipFile = new File(storageDir, "received_files.zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            unzip(zipFile.getAbsolutePath(), storageDir.getAbsolutePath());
            zipFile.delete();

            for (File file : storageDir.listFiles()) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }

        } catch (IOException e) {
            Log.e(TAG, "Error receiving file: " + e.getMessage(), e);
        }
    }

    private void unzip(String zipFilePath, String destDirectory) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[1024];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
