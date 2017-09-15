package com.nandy.vkchanllenge.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yana on 15.09.17.
 */

public class ConnectionUtils {

    public static boolean isInternetConnection(Context context) {
        return isConnectedToWiFi(context) || isConnectedToMobileData(context);
    }

    private static boolean isConnectedToWiFi(Context context) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static boolean isConnectedToMobileData(Context context) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
}
