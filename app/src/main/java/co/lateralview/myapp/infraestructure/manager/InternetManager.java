package co.lateralview.myapp.infraestructure.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetManager
{
    private Context mContext;

    public InternetManager(Context context)
    {
        mContext = context;
    }

    public Boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
