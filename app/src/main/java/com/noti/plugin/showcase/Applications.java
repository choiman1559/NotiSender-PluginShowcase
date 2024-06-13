package com.noti.plugin.showcase;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.res.ResourcesCompat;

import com.noti.plugin.Plugin;
import com.noti.plugin.data.PairDeviceType;
import com.noti.plugin.data.PairRemoteAction;

//Plugin initialization when application is first run
//Due to some technical issues, you need to launch any activity in your app at least once to start the plugin for the first time.
public class Applications extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Check if notification post permission is granted
        boolean isNotificationPermission = Build.VERSION.SDK_INT < 31 || ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();

        //Initialize the plugin
        Plugin plugin = Plugin.init(new PluginResponses());

        //Setting plugin options
        plugin.setPluginTitle("Plugin Showcase");
        plugin.setPluginDescription("Examples of using the plugin API");
        plugin.setAppPackageName(getPackageName());
        plugin.setPluginReady(isNotificationPermission);
        plugin.setSettingClass(MainActivity.class);
        plugin.setRequireSensitiveAPI(true);

        //Get Bitmap for Remote Action
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, null);
        Bitmap actionIcon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(actionIcon);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        //Register new Remote Action Item
        plugin.addPairRemoteActions(new PairRemoteAction.Builder()
                .setActionClass(RemoteActionActivity.class)
                .setActionIcon(actionIcon)
                .setActionType("Showcase Remote Action Example")
                .setActionDescription("Demonstrates how to use remote action API of plugin.")
                .setTargetDeviceTypeScope(PairDeviceType.DEVICE_TYPE_PHONE, PairDeviceType.DEVICE_TYPE_TABLET) //Optional, Scoped to all devices if not specified
                .build()
        );

        //Setting custom network provider options
        try {
            plugin.setNetworkProvider(CustomNetProvider.class);
            plugin.getNetworkProvider().setProviderName("Plugin Showcase");
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
