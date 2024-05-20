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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.example.project.SendPostRequest;

public class DiagnosticsFragment extends BaseFragment {

    private ImageView carDataFilter;
    private EditText ipUserInput, ipServerInput;
    private CardView carDataCardFilter;
    private AppCompatButton importData;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;

    private BluetoothAdapter bluetoothAdapter;
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
        ipUserInput = view.findViewById(R.id.ipUserInput);
        ipServerInput = view.findViewById(R.id.ipServerInput);

        ipServerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!isValidIp(text)) {
                    ipServerInput.setError("Invalid format. Please use XXX.XXX.XXX.XXX with each segment between 0 and 255");
                } else {
                    ipServerInput.setError(null);
                }
                checkBothFields();
            }
        });

        ipUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.matches("^([0-9A-F]{2}:){5}[0-9A-F]{2}$")) {
                    ipUserInput.setError("Invalid format. Please use XX:XX:XX:XX:XX:XX");
                } else {
                    ipUserInput.setError(null);
                }
                checkBothFields();
            }
        });


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

    private void checkBothFields() {
        String ipText = ipServerInput.getText().toString();
        String macText = ipUserInput.getText().toString();

        boolean isIpValid = isValidIp(ipText);
        boolean isMacValid = macText.matches("^([0-9A-F]{2}:){5}[0-9A-F]{2}$");

        if (isIpValid && isMacValid) {
            importData.setEnabled(true);
        } else {
            importData.setEnabled(false);
        }
    }

    private boolean isValidIp(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
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
            String ipServer = ipServerInput.getText().toString();
            String ipUser = ipUserInput.getText().toString();
            if (!ipUser.isEmpty()||!ipServer.isEmpty()) {
                new SendPostRequest(getContext()).execute(ipServer, ipUser);
            } else {
                Toast.makeText(getContext(), "Please enter an IP prefix", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                String ipServer = ipServerInput.getText().toString();
                String ipUser = ipUserInput.getText().toString();
                if (!ipUser.isEmpty()||!ipServer.isEmpty()) {
                    new SendPostRequest(getContext()).execute(ipServer, ipUser);
                } else {
                    Toast.makeText(getContext(), "Please enter an IP prefix", Toast.LENGTH_SHORT).show();
                }
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
}