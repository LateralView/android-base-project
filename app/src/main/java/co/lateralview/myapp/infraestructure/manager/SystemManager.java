package co.lateralview.myapp.infraestructure.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SystemManager {
    private Context mContext;

    public SystemManager(Context context) {
        mContext = context;
    }

    /**
     * Shown in console the KeyHash application.
     */
    public void showKeyHash() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void showDeviceInfo() {
        switch (mContext.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                Log.i("DeviceScreenDensity", "ldpi");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Log.i("DeviceScreenDensity", "mdpi");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Log.i("DeviceScreenDensity", "hdpi");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Log.i("DeviceScreenDensity", "xhdpi");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Log.i("DeviceScreenDensity", "xxhdpi");
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Log.i("DeviceScreenDensity", "xxxhdpi");
                break;
        }
    }
}
