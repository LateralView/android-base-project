package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import co.lateralview.myapp.infraestructure.manager.implementation.FileManagerImpl;

public class SocialManager {
    private FileManagerImpl mFileManager;

    public SocialManager(FileManagerImpl fileManager) {
        mFileManager = fileManager;
    }

    public void shareData(Activity activity, String text) {
        Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text)
                .getIntent();

        activity.startActivity(shareIntent);
    }

    public void shareData(Activity activity, String text, Bitmap image) {
        Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("image/jpeg")
                .setText(text)
                .setStream(Uri.parse(mFileManager.savePhotoToInternalStorage(image)))
                .getIntent();

        activity.startActivity(shareIntent);
    }
}
