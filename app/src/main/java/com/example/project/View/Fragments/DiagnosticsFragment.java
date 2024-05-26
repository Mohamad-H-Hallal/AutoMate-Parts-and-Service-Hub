package com.example.project.View.Fragments;

import static com.example.project.Controller.GetImagePath.getRealPath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import androidx.preference.PreferenceManager;

import com.example.project.Controller.UnzipUtil;
import com.example.project.Controller.UserData;
import com.example.project.DataSentListener;
import com.example.project.FileUpload.AppFilesService;
import com.example.project.FileUpload.FileUploaderClass;
import com.example.project.FileUpload.ImageUploaderClass;
import com.example.project.FileUpload.RetrofitApiClient;
import com.example.project.Model.DiagnosticDataModel;
import com.example.project.Model.UserDataResponse;
import com.example.project.R;
import com.example.project.SendPostRequest;
import com.example.project.SerialListener;
import com.example.project.SerialService;

import com.example.project.View.Activities.InfoActivity;
import com.example.project.View.Activities.LoginActivity;
import com.example.project.View.Activities.SettingActivity;
import com.example.project.View.Adapters.DiagnosticsAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagnosticsFragment extends BaseFragment implements SerialListener {

    private ImageView carDataFilter, infoData;
    private EditText ipUserInput, ipServerInput;
    private CardView carDataCardFilter;
    private AppCompatButton importData, carDataFilterSubmit;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 2;
    private static String FILE_NAME = "car_data.zip";
    private static final String UNZIP_DESTINATION = "unzipped";
    private ListView dataListView;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private AlertDialog Dialog;
    private TextView dataClearAll, noData;
    private SerialService service;
    private Uri zippedfile= null;


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
        carDataFilterSubmit = view.findViewById(R.id.carDataFilterSubmit);
        dataListView = view.findViewById(R.id.dataListView);
        infoData = view.findViewById(R.id.infoData);
        dataClearAll = view.findViewById(R.id.dataClearAll);
        noData = view.findViewById(R.id.noData);
        service = new SerialService();

//        ipServerInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String text = s.toString();
//                if (!isValidIp(text)) {
//                    ipServerInput.setError("Invalid format. Please use XXX.XXX.XXX.XXX with each segment between 0 and 255");
//                } else {
//                    ipServerInput.setError(null);
//                }
//                checkBothFields();
//            }
//        });

//        ipUserInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String text = s.toString();
//                if (!text.matches("^([0-9A-F]{2}:){5}[0-9A-F]{2}$")) {
//                    ipUserInput.setError("Invalid format. Please use XX:XX:XX:XX:XX:XX");
//                } else {
//                    ipUserInput.setError(null);
//                }
//                checkBothFields();
//            }
//        });


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

        infoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View DialogView = LayoutInflater.from(getContext()).inflate(R.layout.info_data_dialog, null);
                final AppCompatButton infoButton = DialogView.findViewById(R.id.infoButton);
                final AppCompatButton okButton = DialogView.findViewById(R.id.okButton);
                builder.setView(DialogView);
                infoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), InfoActivity.class);
                        startActivity(intent);
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog = builder.create();
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Dialog.show();
            }
        });

        carDataFilterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = "0";
                String month = "0";
                String year = "0";
                String hour = "-1";
                if (!daySpinner.getSelectedItem().equals(daySpinner.getItemAtPosition(0).toString())) {
                    day = daySpinner.getSelectedItem().toString();
                }
                if (!monthSpinner.getSelectedItem().equals(monthSpinner.getItemAtPosition(0).toString())) {
                    month = monthSpinner.getSelectedItem().toString();
                }
                if (!yearSpinner.getSelectedItem().equals(yearSpinner.getItemAtPosition(0).toString())) {
                    year = yearSpinner.getSelectedItem().toString();
                }
                if (!hourSpinner.getSelectedItem().equals(hourSpinner.getItemAtPosition(0).toString())) {
                    hour = hourSpinner.getSelectedItem().toString();
                }
                if ((month.equals("0") && year.equals("0") && hour.equals("-1") && !day.equals("0")) || (month.equals("0") && year.equals("0") && !hour.equals("-1") && !day.equals("0")) || (month.equals("0") && !year.equals("0") && !hour.equals("-1") && !day.equals("0")) || (month.equals("0") && !year.equals("0") && hour.equals("-1") && !day.equals("0"))) {
                    Toast.makeText(getContext(), "Please enter a month with day!", Toast.LENGTH_SHORT).show();
                } else {
                    filterFilesByDateTime(UserData.getId(), day, month, year, hour);
                }
            }
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

        dataClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daySpinner.setSelection(0);
                monthSpinner.setSelection(0);
                yearSpinner.setSelection(0);
                hourSpinner.setSelection(0);
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/zip");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

//    private void checkBothFields() {
//        String ipText = ipServerInput.getText().toString();
//        String macText = ipUserInput.getText().toString();
//
//        boolean isIpValid = isValidIp(ipText);
//        boolean isMacValid = macText.matches("^([0-9A-F]{2}:){5}[0-9A-F]{2}$");
//
//        if (isIpValid && isMacValid) {
//            importData.setEnabled(true);
//        } else {
//            importData.setEnabled(false);
//        }
//    }

//    private boolean isValidIp(String ip) {
//        String[] parts = ip.split("\\.");
//        if (parts.length != 4) {
//            return false;
//        }
//        for (String part : parts) {
//            try {
//                int value = Integer.parseInt(part);
//                if (value < 0 || value > 255) {
//                    return false;
//                }
//            } catch (NumberFormatException e) {
//                return false;
//            }
//        }
//        return true;
//    }

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
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            BluetoothDevice targetDevice = null;
            for (BluetoothDevice device : pairedDevices) {

                if (device.getName().equals("raspberrypi")) {

                    targetDevice = device;

                    break;
                }
            }
            try {
                service.connect(targetDevice, this, requireContext().getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                BluetoothDevice targetDevice = null;
                for (BluetoothDevice device : pairedDevices) {

                    if (device.getName().equals("raspberrypi")) {

                        targetDevice = device;

                        break;
                    }
                }
                try {
                    service.connect(targetDevice, this, requireContext().getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Log.d("FileNameDebug", "Selected file Uri: " + uri.toString());
                if(isCarDataZip(getFileName(uri))){
                    FILE_NAME=getFileName(uri);
                    zippedfile = uri;
                handleSelectedFile(uri);}
                else{
                    Toast.makeText(getContext(), "you didn't pick the correct file!", Toast.LENGTH_SHORT).show();
                    openFilePicker();
                }
            }
        } else if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                checkAndRequestPermissions();
            } else {
                Toast.makeText(getContext(), "Bluetooth enablement cancelled. Cannot continue.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isCarDataZip(String fileName) {
        String regex = "^car_data.*\\.zip$";
        return fileName.matches(regex);
    }


    private void handleSelectedFile(Uri uri) {
        getContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String unzipDestPath = locateAndUnzipFile(uri);
        if (unzipDestPath != null) {
            uploadfiles(unzipDestPath);
            Toast.makeText(getContext(), "Data sent successfully!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "File not found or failed to unzip.", Toast.LENGTH_SHORT).show();
        }
    }


    private String locateAndUnzipFile(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File tempFile = new File(getContext().getCacheDir(), FILE_NAME);
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();

                String unzipDestPath = getContext().getFilesDir().getAbsolutePath() + File.separator + UNZIP_DESTINATION;
                UnzipUtil.unzip(tempFile.getAbsolutePath(), unzipDestPath);
                return unzipDestPath;
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to unzip file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void uploadfiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    uploadFile(file.getPath());
                }
            }
        }
    }

    private void uploadFile(String filePath) {
        FileUploaderClass.uploadFile(filePath, "diagnostics/" + UserData.getId(), UserData.getId(), new FileUploaderClass.onSuccessfulTask() {
            @Override
            public void onSuccess() {
                performDelete(zippedfile);
                deleteLocalFiles();
                fetchRecentFiles(UserData.getId());
            }

            @Override
            public void onFailed(String error) {
                Log.d("error", error);
            }
        });
    }
    private void performDelete(Uri uri) {
        try {
            ContentResolver contentResolver = requireContext().getContentResolver();
            if (DocumentsContract.isDocumentUri(requireContext(), uri)) {
                DocumentsContract.deleteDocument(contentResolver, uri);

            } else {
                int rowsDeleted = contentResolver.delete(uri, null, null);
                if (rowsDeleted > 0) {

                } else {
                    Toast.makeText(requireContext(), "File deletion failed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteLocalFiles() {
        File zipFiles = new File(zippedfile.toString());

        File zipFile = new File(getContext().getCacheDir(), FILE_NAME);
        if (zipFile.exists()) {
            zipFile.delete();
        }

        File unzipDir = new File(getContext().getFilesDir(), UNZIP_DESTINATION);
        if (unzipDir.exists() && unzipDir.isDirectory()) {
            for (File file : unzipDir.listFiles()) {
                file.delete();
            }
            Boolean.toString(unzipDir.delete());
        }
    }
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void checkUserDataAndFetchFiles() {
        int userId = UserData.getId();
        AppFilesService apiInterface = RetrofitApiClient.getClient().create(AppFilesService.class);
        Call<UserDataResponse> call = apiInterface.checkUserData(userId);

        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.body() != null && response.body().isHasData()) {
                    noData.setVisibility(View.GONE);
                    fetchRecentFiles(UserData.getId());
                } else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to check user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecentFiles(int user_id) {
        AppFilesService apiInterface = RetrofitApiClient.getClient().create(AppFilesService.class);
        Call<List<DiagnosticDataModel>> call = apiInterface.getRecentFiles(user_id);

        call.enqueue(new Callback<List<DiagnosticDataModel>>() {
            @Override
            public void onResponse(Call<List<DiagnosticDataModel>> call, Response<List<DiagnosticDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noData.setVisibility(View.GONE);
                    DiagnosticsAdapter adapter = new DiagnosticsAdapter(getContext(), response.body());
                    dataListView.setAdapter(adapter);
                } else {
                    Log.e("Fetch Error", "Response code: " + response.code());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DiagnosticDataModel>> call, Throwable t) {
                Log.e("Fetch Error", "Failure message: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch files", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterFilesByDateTime(int user_id, String day, String month, String year, String hour) {
        AppFilesService apiInterface = RetrofitApiClient.getClient().create(AppFilesService.class);
        Call<List<DiagnosticDataModel>> call = apiInterface.filterFiles(user_id, day, month, year, hour);

        call.enqueue(new Callback<List<DiagnosticDataModel>>() {
            @Override
            public void onResponse(Call<List<DiagnosticDataModel>> call, Response<List<DiagnosticDataModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DiagnosticsAdapter adapter = new DiagnosticsAdapter(getContext(), response.body());
                    dataListView.setAdapter(adapter);
                } else {
                    Log.e("Fetch Error", "Response code: " + response.code());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DiagnosticDataModel>> call, Throwable t) {
                Log.e("Fetch Error", "Failure message: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch files", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUserDataAndFetchFiles();
    }

    @Override
    public void onSerialConnect() {
        try {
            service.write("Script1".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSerialConnectError(Exception e) {
        Toast.makeText(getContext(), "connection error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSerialRead(byte[] data) {

    }

    @Override
    public void onSerialRead(ArrayDeque<byte[]> datas) {
        // Handle received data
        while (!datas.isEmpty()) {
            byte[] data = datas.poll(); // Retrieve and remove the first byte array from the deque
            if (data != null) {
                processReceivedData(data);
            }
        }
    }

    private void processReceivedData(byte[] data) {
        // Process the received byte array
        String receivedMessage = new String(data); // Convert byte array to string (assuming it contains text data)
        if (receivedMessage.equals("success")) {
            openFilePicker();
        } else if (receivedMessage.equals("fail")) {
            Toast.makeText(getContext(), "you should accept the file!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Error Receiving the file!Try Again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSerialIoError(Exception e) {
        Toast.makeText(getContext(), "something wrong happened!", Toast.LENGTH_SHORT).show();
    }
}