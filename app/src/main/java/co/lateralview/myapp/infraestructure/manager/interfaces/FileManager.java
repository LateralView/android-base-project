package co.lateralview.myapp.infraestructure.manager.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public interface FileManager
{
	String savePhotoToInternalStorage(Bitmap bitmapImage);

	File createPhotoFile();

	Uri getUri(File file);
}
