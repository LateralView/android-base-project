package co.lateralview.myapp.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
}
