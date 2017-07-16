package co.lateralview.myapp.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import co.lateralview.myapp.infraestructure.manager.InternetManager;

public class InternetReceiver extends BroadcastReceiver
{
    private InternetReceiverListener mReceiverHandler;
    private InternetManager mInternetManager;

    public InternetReceiver(InternetReceiverListener handler, InternetManager internetManager)
    {
        mReceiverHandler = handler;
        mInternetManager = internetManager;
    }

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        if (mInternetManager.isOnline())
        {
            mReceiverHandler.onInternetServiceEnabled();
        } else
        {
            mReceiverHandler.onInternetServiceDisabled();
        }
    }

    public IntentFilter getIntentFilter()
    {
        IntentFilter locationServicesChangeFilter = new IntentFilter();

        locationServicesChangeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        return locationServicesChangeFilter;
    }

    public interface InternetReceiverListener
    {
        void onInternetServiceEnabled();

        void onInternetServiceDisabled();
    }


}
