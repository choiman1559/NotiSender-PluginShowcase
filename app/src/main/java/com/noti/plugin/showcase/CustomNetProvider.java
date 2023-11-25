package com.noti.plugin.showcase;

import android.content.Context;

import com.noti.plugin.process.NetworkProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//NetProvider API implementation example
//Overriding data transmission/reception that occurs in NotiSender by inheriting NetworkProvider
public class CustomNetProvider extends NetworkProvider {
    @Override
    public void onPostRequested(Context context, JSONObject data) {
        super.onPostRequested(context, data);
        try {
            //Spoofing send device information
            JSONObject rawData = data.getJSONObject("data")
                    .put("device_id", "testDevice6969")
                    .put("device_name", "testDevice");

            //Convert JSON object to Map object
            Map<String, String> map = new HashMap<>();
            for (Iterator<String> it = rawData.keys(); it.hasNext(); ) {
                String key = it.next();
                map.put(key, rawData.getString(key));
            }

            //Loopback to notisender
            NetworkProvider.pushReceivedData(context, map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
