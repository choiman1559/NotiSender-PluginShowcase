package com.noti.plugin.showcase;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.application.isradeleon.notify.Notify;
import com.noti.plugin.data.NotificationData;
import com.noti.plugin.data.PairDeviceInfo;
import com.noti.plugin.listener.PluginResponse;
import com.noti.plugin.process.PluginAction;

//Plugin Data Handling Class
//Warning: All methods in this class are called "unexpectedly" in the background, not on the UI thread.
public class PluginResponses implements PluginResponse {
    @Override
    public void onReceiveRemoteActionRequest(Context context, PairDeviceInfo deviceInfo, String taskType, String args) {
        //Handling Remote Work Requests
        if(taskType.equals("notification")) {
            //Notification Post Actions
            Notify.build(context)
                    .setTitle(args)
                    .setContent("Notification from " + deviceInfo.getDeviceName())
                    .setLargeIcon(R.mipmap.ic_launcher)
                    .largeCircularIcon()
                    .setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setChannelName("Testing Channel")
                    .setChannelId("Notification Test")
                    .setAutoCancel(true)
                    .show();
        }
    }

    @Override
    public void onReceiveRemoteDataRequest(Context context, PairDeviceInfo deviceInfo, String requestedDataType) {
        //Handling Remote Data Sending Requests
        //The requested data must be replied through the PluginAction.responseRemoteData method.
        if(requestedDataType.equals("battery")) {
            //Getting Battery Status
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, filter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            int batteryPct = (int) (level * 100 / (float) scale);
            boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);
            String dataToSend = batteryPct + "%" + (isCharging ? ", charging" : "");

            //Return data to the plugin here
            PluginAction.responseRemoteData(context, deviceInfo, requestedDataType, dataToSend);
        }
    }

    @Override
    public void onReceiveException(Context context, Exception e) {
        //Receive and handle exceptions thrown by plugins here
        e.printStackTrace();
    }

    @Override
    public void onNotificationReceived(Context context, NotificationData notification) {
        Log.d("NOTIFICATION", notification.APP_NAME + "|" + "|" + notification.TITLE + notification.CONTENT + "|" + notification.DEVICE_NAME + "|" + notification.DATE + "|" + notification.APP_NAME + "|" + notification.PACKAGE_NAME);
    }
}
