package com.noti.plugin.showcase;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.noti.plugin.data.PairDeviceInfo;

public class RemoteActionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PairDeviceInfo pairDeviceInfo = PairDeviceInfo.fromIntent(getIntent());

        if(pairDeviceInfo != null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle("Remote Action Launched!");
            dialogBuilder.setMessage(String.format("""
                    Target Device Info
                    Name: %s
                    Id: %s
                    Type: %s
                    """, pairDeviceInfo.getDeviceName(), pairDeviceInfo.getDeviceId(), pairDeviceInfo.getDeviceType().toString()));
            dialogBuilder.setPositiveButton("Close", (dialog, which) -> finishAffinity());
            dialogBuilder.create().show();
        }
    }
}
