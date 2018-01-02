package co.lateralview.myapp.ui.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

public class SystemUtils
{
    public static void setDeviceOrientation(Activity activity)
    {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void vibrate(Activity activity, int millis)
    {
        ((Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(millis);
    }

    public static void keepScreenOn(Activity activity)
    {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static String getVersionName(Context context)
    {
        final PackageInfo packageInfo;
        try
        {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

    public static int getVersionCode(Context context)
    {
        final PackageInfo packageInfo;
        try
        {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            return -1;
        }
    }

    public static void fullScreenMode(Activity activity)
    {
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    public static boolean isRunningOnMainThread()
    {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }

        return false;
    }
}
