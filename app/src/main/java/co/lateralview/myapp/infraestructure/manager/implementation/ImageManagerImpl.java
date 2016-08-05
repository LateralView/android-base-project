package co.lateralview.myapp.infraestructure.manager.implementation;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;

import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ImageManagerImpl implements ImageManager
{
	private Context mContext;

	public ImageManagerImpl(Context context)
	{
		mContext = context;
	}

	public void loadCircleImage(String url, ImageView imageView)
	{
		Glide.with(mContext)
				.load(url)
				.centerCrop()
				.bitmapTransform(new CropCircleTransformation(mContext))
				.into(imageView);
	}

	public void loadImage(String url, ImageView imageView)
	{
		Glide.with(mContext)
				.load(url)
				.into(imageView);
	}

	public void loadImage(String url, SimpleTarget simpleTarget)
	{
		Glide.with(mContext)
				.load(url)
				.asBitmap()
				.into(simpleTarget);
	}

	public void loadGifFromRes(int res, ImageView imageView)
	{
		Glide.with(mContext)
				.load(res)
				.asGif()
				.fitCenter()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(imageView);
	}
}
