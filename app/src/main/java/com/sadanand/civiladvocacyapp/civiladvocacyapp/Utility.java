package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class Utility {

    private static final String TAG = "Utility";
    public static final String Selection = "selected_item";
    public static String KEY = "AIzaSyA9v_6t3rijev-iZgpnptkQWTkOc7O0KYY";
    public static  boolean checkInternetConn(Context context) {
        Log.d(TAG, "checkInternetConn: Internet Check init()");
        ConnectivityManager NetworkCheck = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return NetworkCheck.getActiveNetworkInfo() == null || !NetworkCheck.getActiveNetworkInfo().isConnected();
    }

}
