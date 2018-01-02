package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.io.File;

import co.lateralview.myapp.infraestructure.manager.implementation.FileManagerImpl;
import co.lateralview.myapp.infraestructure.manager.interfaces.FileManager;

public class GalleryPhotoManager
{
    protected Fragment mCallerFragment;
    protected Activity mCallerActivity;
    protected IGalleryPhotoCallback mCallback;
    protected Uri mCroppedImage;
    private int mRequestCodePhotoFromGallery;
    private int mRequestCodePhotoFromGalleryCrop;
    private int mRequestCodeCropImage;
    private FileManager mFileManager;

    public GalleryPhotoManager(Fragment fragment, IGalleryPhotoCallback callback, int requestCode)
    {
        this(fragment.getActivity(), callback, requestCode);
        mCallerFragment = fragment;
    }

    public GalleryPhotoManager(Activity activity, IGalleryPhotoCallback callback, int requestCode)
    {
        mRequestCodePhotoFromGallery = requestCode;
        mCallerActivity = activity;
        mCallback = callback;
        mFileManager = new FileManagerImpl(mCallerActivity);
    }

    public void startService()
    {
        start(false);
    }

    public void startServiceWithCrop(int cropImageRequestCode1, int cropImageRequestCode2)
    {
        mRequestCodePhotoFromGalleryCrop = cropImageRequestCode1;
        mRequestCodeCropImage = cropImageRequestCode2;

        start(true);
    }

    private void start(boolean cropIt)
    {
        Intent mediaChooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mediaChooser.setType("image/*");

        if (mCallerFragment != null)
        {
            mCallerFragment.startActivityForResult(mediaChooser,
                    cropIt ? mRequestCodePhotoFromGalleryCrop : mRequestCodePhotoFromGallery);
        } else
        {
            mCallerActivity.startActivityForResult(mediaChooser,
                    cropIt ? mRequestCodePhotoFromGalleryCrop : mRequestCodePhotoFromGallery);
        }
    }

    public void processResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == mRequestCodePhotoFromGallery)
            {
                onPhotoFromGallerySuccess(data);
                return;
            }

            if (requestCode == mRequestCodeCropImage)
            {
                onPhotoFromGalleryCroppedSuccess(mCroppedImage);
                return;
            }

            if (requestCode == mRequestCodePhotoFromGalleryCrop)
            {
                mCroppedImage = mFileManager.createPhotoUri();

                if (mCallerFragment != null)
                {
                    new CropManager(mCallerFragment, mRequestCodeCropImage).requestCrop(
                            data.getData(), mCroppedImage);
                } else
                {
                    new CropManager(mCallerActivity, mRequestCodeCropImage).requestCrop(
                            data.getData(), mCroppedImage);
                }

                return;
            }
        }
    }

    protected void onPhotoFromGallerySuccess(Intent data)
    {
        if (data != null)
        {
            final String path =
                    null != data.getData() ? data.getData().getPath() : data.getAction().replace(
                            "file://", "");

            getPhotoFromPath(path);
        }
    }

    protected void onPhotoFromGalleryCroppedSuccess(final Uri imageUri)
    {
        if (imageUri != null)
        {
            getPhotoFromPath(imageUri.getPath());
        }
    }

    protected void getPhotoFromPath(final String path)
    {
        new PhotoDecodeTask(photo -> mCallerActivity.runOnUiThread(
                () -> mCallback.onPhotoObtained(photo, new File(path)))).execute(path);
    }

    public interface IGalleryPhotoCallback
    {
        void onPhotoObtained(Bitmap picture, File file);
    }
}
