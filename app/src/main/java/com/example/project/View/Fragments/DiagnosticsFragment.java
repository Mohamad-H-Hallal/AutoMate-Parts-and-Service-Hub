package com.example.project.View.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import com.example.project.R;

public class DiagnosticsFragment extends BaseFragment {

    private ImageView carDataFilter;
    private CardView carDataCardFilter;
    private AppCompatButton importData;
    private static final String TAG = "BluetoothClient";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Same as server UUID
    private static final String DEVICE_ADDRESS = "2C:CF:67:03:A4:11"; // Replace with server MAC address
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private ProgressBar progressBar;
    private TextView statusText;

    public DiagnosticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        progressBar = view.findViewById(R.id.progress_bar_id);
        statusText = view.findViewById(R.id.status_text);
        importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBluetoothEnabled()) {
                    requestBluetoothEnable();
                    return;
                }
                checkAndRequestPermissions();
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }
        carDataFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carDataCardFilter.getVisibility() == View.VISIBLE) {
                    carDataCardFilter.setVisibility(View.GONE);
                    carDataFilter.setBackground(null);
                } else {
                    carDataCardFilter.setVisibility(View.VISIBLE);
                    carDataFilter.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.filter_circle));
                }
            }
        });
    }

    private boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    private void requestBluetoothEnable() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
            return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }


    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_WRITE_STORAGE_PERMISSION);
        } else {
            connectToDevice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                connectToDevice();
            } else {
                Toast.makeText(getContext(), "Permissions denied. Cannot import data.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_ENABLE_BT) {
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


    private void connectToDevice() {
        new ConnectTask().execute();
    }

    private class ConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE); // Show progress bar
            statusText.setText(getResources().getString(R.string.connecting)); // Update status text
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
                BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();
                receiveFile(socket);
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to server", e);
            } catch (SecurityException e) {
                Log.e(TAG, "Security exception during connection", e);
            }
            return null;
        }


        private void receiveFile(BluetoothSocket socket) throws IOException {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, "car_data.zip");
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytes = 0;
            int fileSize = inputStream.available(); // Assuming file size can be obtained

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                progressBar.setProgress((int) ((totalBytes * 1.0f) / fileSize * 100));
            }

            outputStream.close();
            inputStream.close();
            socket.close();

            Log.d(TAG, "File received: " + file.getAbsolutePath());
            publishProgress(); // Trigger UI update on successful file reception
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            statusText.setText(getResources().getString(R.string.downloading)); // Update status text during download
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE); // Hide progress bar
            statusText.setText(getResources().getString(R.string.file_saved)); // Update status text on success
        }
    }
}