package co.lateralview.myapp.infraestructure.manager.interfaces;

import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;

public interface ImageManager
{
	void loadCircleImage(String url, ImageView imageView);

	void loadImage(String url, ImageView imageView);

	void loadImage(String url, SimpleTarget simpleTarget);

	void loadGifFromRes(int res, ImageView imageView);
}
