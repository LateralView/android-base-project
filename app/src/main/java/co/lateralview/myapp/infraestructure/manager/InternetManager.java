package co.lateralview.myapp.infraestructure.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetManager {
    private ConnectivityManager mConnectivityManager;

    public InternetManager(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public Boolean isOnline() {
        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean onWifi() {
        return !mConnectivityManager.isActiveNetworkMetered();
    }
}
