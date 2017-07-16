package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsManager
{
    private Activity mActivity;
    private String[] mPermissions;
    private int mRequestCode;
    private IPermissionsService mListener;

    public PermissionsManager(Activity activity, String permission, int requestCode,
            IPermissionsService listener)
    {
        this(activity, new String[]{permission}, requestCode, listener);
    }

    public PermissionsManager(Activity activity, String[] permissions, int requestCode,
            IPermissionsService listener)
    {
        mActivity = activity;
        mPermissions = permissions;
        mRequestCode = requestCode;
        mListener = listener;
    }

    public boolean hasPermissionsGranted()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            for (String permission : mPermissions)
            {
                if (ContextCompat.checkSelfPermission(mActivity, permission)
                        != PackageManager.PERMISSION_GRANTED) //ask the user for permission.
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void requestPermission()
    {
        ActivityCompat.requestPermissions(mActivity, mPermissions, mRequestCode);
    }

    public void processResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == mRequestCode)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // permission was granted, yay!
                mListener.onPermissionGranted();
            } else
            {
                // permission denied, boo!
                mListener.onPermissionDenied();
            }

            return;
        }
    }

    public interface IPermissionsService
    {
        void onPermissionGranted();

        void onPermissionDenied();
    }
}
