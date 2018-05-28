package co.lateralview.myapp.infraestructure.manager.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public interface FileManager {
    String savePhotoToInternalStorage(Bitmap bitmapImage);

    File createPhotoFile();

    Uri getUri(File file);

    Uri createPhotoUri();

    void saveBitmapToFile(Bitmap croppedImage, Uri saveUri);
}
