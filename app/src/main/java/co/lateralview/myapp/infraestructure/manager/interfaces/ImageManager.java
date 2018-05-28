package co.lateralview.myapp.infraestructure.manager.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

public interface ImageManager
{
    void loadCircleImage(String url, ImageView imageView);

    void loadImage(String url, ImageView imageView);

    void loadImage(String url, SimpleTarget<Bitmap> simpleTarget);

    void loadGifFromRes(int res, ImageView imageView);

    Bitmap compressImage(Bitmap bitmap, File file);

    Bitmap rotateBitmap(Bitmap bitmap, int orientation);

    Bitmap blur(Bitmap image);

    Bitmap transformToCircle(Bitmap bitmap);
}
