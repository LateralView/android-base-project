package co.lateralview.myapp.infraestructure.manager.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.lateralview.myapp.infraestructure.manager.interfaces.FileManager;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class FileManagerImpl implements FileManager
{
	private static final String FILE_TEMP_PREFIX = "MYAPP";

	private Context mContext;

	public FileManagerImpl(Context mContext)
	{
		this.mContext = mContext;
	}

	public String savePhotoToInternalStorage(Bitmap bitmapImage)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		File photoFile = createPhotoFile();

		writeFile(photoFile, bytes.toByteArray());

		return getUri(photoFile).toString();
	}

	public File createPhotoFile()
	{
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = FILE_TEMP_PREFIX + "_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		storageDir.mkdirs();

		try
		{
			return File.createTempFile(imageFileName, ".jpg", storageDir);
		} catch (IOException e)
		{
			return null;
		}
	}

	public Uri getUri(File file)
	{
		return null != file ? FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file) : null;
	}

	public Uri createPhotoUri()
	{
		File file = createPhotoFile();

		return null != file ? getUri(file) : null;
	}

	public void saveBitmapToFile(Bitmap croppedImage, Uri saveUri)
	{
		if (saveUri != null)
		{
			FileOutputStream outputStream = null;
			try
			{
				outputStream = new FileOutputStream(saveUri.getPath());

				if (outputStream != null)
				{
					croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			finally
			{
				closeSilently(outputStream);
				croppedImage.recycle();
			}
		}
	}

	private boolean writeFile(File f, byte[] bytes)
	{
		return writeFile(f.getAbsolutePath(), bytes);
	}

	private boolean writeFile(String path, byte[] bytes)
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(path);

			try
			{
				stream.write(bytes);
			} finally
			{
				closeSilently(stream);
			}

			return true;
		} catch (Exception e)
		{
			return false;
		}
	}


	private void closeSilently(@Nullable Closeable c)
	{
		if (c == null)
		{
			return;
		}

		try
		{
			c.close();
		} catch (Throwable t)
		{
			// Do nothing
		}
	}
}
