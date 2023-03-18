package com.noti.plugin.showcase;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import com.noti.plugin.data.PairDeviceInfo;
import com.noti.plugin.process.PluginAction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<PairDeviceInfo> deviceList = new ArrayList<>();
    int selectedDeviceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if notification post permission is granted
        if(Build.VERSION.SDK_INT >= 31 && !((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled()) {
            ActivityCompat.requestPermissions(this, new String[] { "android.permission.POST_NOTIFICATIONS" }, 100);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener((v) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/choiman1559/NotiSender-PluginShowcase"))));

        //Request device list
        //Use PluginAction.requestDeviceList to request device list
        Button deviceListButton = findViewById(R.id.deviceListButton);
        LinearLayoutCompat remoteActionLayout = findViewById(R.id.remoteActionLayout);
        MaterialAutoCompleteTextView deviceListSpinner = findViewById(R.id.deviceListSpinner);

        remoteActionLayout.setVisibility(View.GONE);
        deviceListSpinner.setOnItemClickListener((parent, view, position, id) -> selectedDeviceIndex = position);
        deviceListButton.setOnClickListener((v) -> PluginAction.requestDeviceList(this, data -> {
            deviceList = data;
            if (deviceList.isEmpty()) {
                remoteActionLayout.setVisibility(View.GONE);
            } else {
                remoteActionLayout.setVisibility(View.VISIBLE);
                ArrayList<String> deviceNames = new ArrayList<>();
                for (PairDeviceInfo device : deviceList) {
                    deviceNames.add(device.getDevice_name());
                }

                deviceListSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deviceNames));
            }
        }));

        //Request remote action
        //Use PluginAction.requestRemoteAction to request remote action
        TextInputEditText notificationInput = findViewById(R.id.notificationInput);
        Button notificationButton = findViewById(R.id.notificationButton);

        notificationButton.setOnClickListener((v) -> {
            String value = notificationInput.getText() == null ? "" : notificationInput.getText().toString();
            if (value.isEmpty()) {
                notificationInput.setError("Title value is empty");
            } else {
                PluginAction.requestRemoteAction(this, deviceList.get(selectedDeviceIndex), "notification", value);
            }
        });

        //Request remote data
        //Use PluginAction.requestRemoteData to request remote data
        TextView batteryText = findViewById(R.id.batteryText);
        Button batteryButton = findViewById(R.id.batteryButton);

        batteryButton.setOnClickListener((v) -> PluginAction.requestRemoteData(this, deviceList.get(selectedDeviceIndex), "battery", (type, data) -> {
            if (type.equals("battery")) batteryText.setText(String.format("Battery status: %s", data));
        }));

        //Request preference of host
        //Use PluginAction.requestPreferences to get host (NotiSender) preferences data
        //Note: For security reasons, requests for some Preference values are restricted.
        //When requesting a restricted value, an exception is thrown through PluginResponse.onReceiveException.
        TextView preferenceText = findViewById(R.id.preferenceText);
        TextInputEditText preferenceInput = findViewById(R.id.preferenceInput);
        Button preferenceButton = findViewById(R.id.preferenceButton);

        preferenceButton.setOnClickListener((v) -> {
            String key = preferenceInput.getText() == null ? "" : preferenceInput.getText().toString();
            if (key.isEmpty()) {
                preferenceInput.setError("Key value is empty");
            } else {
                PluginAction.requestPreferences(this, key, (key1, data) -> preferenceText.setText(String.format("Preference value: %s", data)));
            }
        });

        //Request host service status
        //Use PluginAction.requestHostServiceStatus to check host service (NotiSender) status
        TextView serviceStatusTextView = findViewById(R.id.serviceStatusText);
        Button serviceStatusButton = findViewById(R.id.serviceStatusButton);

        serviceStatusButton.setOnClickListener((v) -> PluginAction.requestServiceStatus(this, isRunning -> serviceStatusTextView.setText(String.format("Service status: %s", isRunning ? "Running" : "Stopped"))));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int foo : grantResults) {
            if (foo != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }
}