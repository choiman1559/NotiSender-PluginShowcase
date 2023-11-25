package com.noti.plugin.showcase;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.noti.plugin.Plugin;

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

        //Setting custom network provider options
        try {
            plugin.setNetworkProvider(CustomNetProvider.class);
            plugin.getNetworkProvider().setProviderName("Plugin Showcase");
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
