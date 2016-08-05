package co.lateralview.myapp.infraestructure.manager.implementation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
	private static final String FILE_TEMP_PREFIX = "TANTAN";

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
		return null != file ? Uri.fromFile(file) : null;
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
				stream.close();
			}

			return true;
		} catch (Exception e)
		{
			return false;
		}
	}
}
