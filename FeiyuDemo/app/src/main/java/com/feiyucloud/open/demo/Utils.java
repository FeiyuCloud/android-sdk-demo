package com.feiyucloud.open.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static void saveUserId(Context context, String value) {
        SharedPreferences sp = context.getSharedPreferences("app_preference", Context.MODE_PRIVATE);
        sp.edit().putString("user_id", value).apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app_preference", Context.MODE_PRIVATE);
        return sp.getString("user_id", null);
    }

}
