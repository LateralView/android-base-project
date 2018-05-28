package co.lateralview.myapp.infraestructure.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;

public class PhotoDecodeTask extends AsyncTask<String, Integer, Integer> {
    private IPhotoDecodeTaskCallback mPhotoDecodeTaskListener;

    public PhotoDecodeTask(IPhotoDecodeTaskCallback photoDecodeTaskListener) {
        mPhotoDecodeTaskListener = photoDecodeTaskListener;
    }

    protected Integer doInBackground(String... paths) {
        String photoPath = paths[0];

        if (!photoPath.isEmpty()) {
            final File photo = new File(photoPath);

            if (photo.exists()) {
                final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

                mPhotoDecodeTaskListener.onPhotoDecodeTaskSuccess(bitmap);
            }
        }

        return 0;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    public interface IPhotoDecodeTaskCallback {
        void onPhotoDecodeTaskSuccess(Bitmap photo);
    }

}
