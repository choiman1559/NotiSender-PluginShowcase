package com.noti.plugin.showcase;

import android.app.Application;

import com.noti.plugin.Plugin;

public class Applications extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Plugin plugin = Plugin.init(new PluginResponses());

        plugin.setPluginTitle("Plugin Showcase");
        plugin.setPluginDescription("Examples of using the plugin API");
        plugin.setAppPackageName(getPackageName());
        plugin.setPluginReady(true);
        plugin.setSettingClass(MainActivity.class);
        plugin.setRequireSensitiveAPI(false);
    }
}
