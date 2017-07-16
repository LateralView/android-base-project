package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.soundcloud.android.crop.Crop;

public class CropManager
{
    public static final int MAX_IMAGE_WIDTH = 600;
    public static final int MAX_IMAGE_HEIGHT = 600;

    protected Activity mCallerActivity;
    protected Fragment mCallerFragment;

    protected int mRequestId;

    public CropManager(Fragment fragment, int requestId)
    {
        this(fragment.getActivity(), requestId);
        mCallerFragment = fragment;
    }

    public CropManager(Activity activity, int requestId)
    {
        mCallerActivity = activity;
        mRequestId = requestId;
    }

    public boolean requestCrop(Uri imagePath, Uri outPath)
    {
        //Uri croppedImage = new FileManagerImpl().createPhotoUri();

        if (outPath != null)
        {
            if (mCallerFragment != null)
            {
                Crop.of(imagePath, outPath)
                        .withMaxSize(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT)
                        .start(mCallerActivity, mCallerFragment, mRequestId);
            } else
            {
                Crop.of(imagePath, outPath)
                        .withMaxSize(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT)
                        .start(mCallerActivity, mRequestId);
            }

            return true;
        }

        return false;
    }
}
