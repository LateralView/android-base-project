package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import co.lateralview.myapp.infraestructure.manager.interfaces.FileManager;

public class CameraManager
{
	protected Activity mCallerActivity;
	private int mRequestTakePhotoCode;
	private ICameraServiceCallback mCameraServiceListener;
	private File mPhotoFile;

	private FileManager mFileManager;

	public CameraManager(Activity callerActivity, FileManager fileManager, int requestCode, ICameraServiceCallback cameraServiceListener)
	{
		mCallerActivity = callerActivity;
		mRequestTakePhotoCode = requestCode;
		mFileManager = fileManager;
		mCameraServiceListener = cameraServiceListener;
	}

	public void startCameraService()
	{
		mPhotoFile = dispatchTakePictureIntent();
	}

	private File dispatchTakePictureIntent()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File photoFile = null;

		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(mCallerActivity.getPackageManager()) != null)
		{
			// Create the File where the photo should go
			photoFile = mFileManager.createPhotoFile();

			// Continue only if the File was successfully created
			if (photoFile != null)
			{
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

				mCallerActivity.startActivityForResult(takePictureIntent, mRequestTakePhotoCode);
			}
		}

		return photoFile;
	}

	public void processResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK && requestCode == mRequestTakePhotoCode)
		{
			onRequestTakePhotoSuccess();
		}
	}

	private void onRequestTakePhotoSuccess()
	{
		Bitmap imageBitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

		if (mCameraServiceListener != null)
		{
			mCameraServiceListener.onPictureTaken(imageBitmap, mPhotoFile);
		}
	}

	public interface ICameraServiceCallback
	{
		void onPictureTaken(Bitmap picture, File file);
	}
}
