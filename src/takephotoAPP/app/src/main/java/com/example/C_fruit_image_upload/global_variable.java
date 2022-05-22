package com.example.C_fruit_image_upload;

import android.app.Application;

public class global_variable extends Application {

    public static String  url  = "";
    public static String  ip_address  = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getTestString() {
        return url;
    }

    public static void setTestString(String num_updates) {
        url = num_updates;
    }
}