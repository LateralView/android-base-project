package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import co.lateralview.myapp.infraestructure.manager.implementation.FileManagerImpl;
import co.lateralview.myapp.infraestructure.manager.implementation.ImageManagerImpl;
import co.lateralview.myapp.infraestructure.manager.interfaces.FileManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;

public class CameraManager {
    protected Activity mCallerActivity;
    protected Fragment mCallerFragment;
    protected Uri mCroppedImage;
    private int mRequestCodeTakePhoto;
    private int mRequestCodeTakePhotoCrop;
    private int mRequestCodeCropImage;
    private ICameraServiceCallback mCameraServiceListener;
    private File mPhotoFile;
    private FileManager mFileManager;
    private ImageManager mImageManager;

    public CameraManager(Fragment fragment, ICameraServiceCallback cameraServiceListener,
            int takePhotoRequestCode) {
        this(fragment.getActivity(), cameraServiceListener, takePhotoRequestCode);
        mCallerFragment = fragment;
    }

    public CameraManager(Activity callerActivity, ICameraServiceCallback cameraServiceListener,
            int takePhotoRequestCode) {
        mCallerActivity = callerActivity;
        mCameraServiceListener = cameraServiceListener;
        mFileManager = new FileManagerImpl(callerActivity);
        mImageManager = new ImageManagerImpl(callerActivity);
        mRequestCodeTakePhoto = takePhotoRequestCode;
    }

    public void startService() {
        mPhotoFile = dispatchTakePictureIntent(false);
    }

    public void startServiceWithCrop(int cropImageRequestCode1, int cropImageRequestCode2) {
        mRequestCodeTakePhotoCrop = cropImageRequestCode1;
        mRequestCodeCropImage = cropImageRequestCode2;

        mPhotoFile = dispatchTakePictureIntent(true);
    }

    private File dispatchTakePictureIntent(boolean cropIt) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mCallerActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = mFileManager.createPhotoFile();

            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileManager.getUri(photoFile));

                if (mCallerFragment != null) {
                    mCallerFragment.startActivityForResult(takePictureIntent,
                            cropIt ? mRequestCodeTakePhotoCrop : mRequestCodeTakePhoto);
                } else {
                    mCallerActivity.startActivityForResult(takePictureIntent,
                            cropIt ? mRequestCodeTakePhotoCrop : mRequestCodeTakePhoto);
                }
            }
        }

        return photoFile;
    }

    public void processResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCodeCropImage) {
                onRequestTakePhotoCropSuccess(mCroppedImage);
                return;
            }

            if (requestCode == mRequestCodeTakePhoto) {
                onRequestTakePhotoSuccess();
                return;
            }

            if (requestCode == mRequestCodeTakePhotoCrop) {
                normalizeImageForUri(mCallerActivity, mFileManager.getUri(mPhotoFile));

                mCroppedImage = mFileManager.createPhotoUri();

                if (mCallerFragment != null) {
                    new CropManager(mCallerFragment,
                            mRequestCodeCropImage).requestCrop(
                            mFileManager.getUri(mPhotoFile), mCroppedImage);
                } else {
                    new CropManager(mCallerActivity,
                            mRequestCodeCropImage).requestCrop(
                            mFileManager.getUri(mPhotoFile), mCroppedImage);
                }

                return;
            }
        }
    }

    private void onRequestTakePhotoSuccess() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

        if (mCameraServiceListener != null) {
            mCameraServiceListener.onPictureTaken(imageBitmap, mPhotoFile);
        }
    }

    private void onRequestTakePhotoCropSuccess(Uri imageCropperUri) {
        if (imageCropperUri != null) {
            final String path = imageCropperUri.getPath();

            new PhotoDecodeTask(
                    photo -> mCallerActivity.runOnUiThread(
                            () -> mCameraServiceListener.onPictureTaken(photo,
                                    new File(path)))).execute(path);
        }
    }

    /**
     * Allows to fix issue for some phones when image processed with android-crop
     * is not rotated properly.
     * Based on https://github.com/jdamcd/android-crop/issues/140#issuecomment-125109892
     *
     * @param context - context to use while saving file
     * @param uri     - origin file uri
     */
    private void normalizeImageForUri(Context context, Uri uri) {
        try {
            ExifInterface exif = new ExifInterface(uri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            Bitmap rotatedBitmap = mImageManager.rotateBitmap(bitmap, orientation);

            if (!bitmap.equals(rotatedBitmap)) {
                mFileManager.saveBitmapToFile(rotatedBitmap, uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface ICameraServiceCallback {
        void onPictureTaken(Bitmap picture, File file);
    }
}
