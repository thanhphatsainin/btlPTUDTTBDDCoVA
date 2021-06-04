package com.example.namtcshop.Connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckConnection {
    public static boolean NetworkConnection(Context context) {
        boolean connectedWifi = false;
        boolean connectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : netInfo) {
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (networkInfo.isConnected())
                    connectedWifi = true;
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (networkInfo.isConnected())
                    connectedMobile = true;
        }
        return connectedWifi || connectedMobile;
    }
    public  static  void ShowToastThongBao(Context context, String s){
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }
}
