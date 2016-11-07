package co.lateralview.myapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.ui.activity.BaseActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseFragment extends Fragment
{
	public static final String TAG = BaseFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("Fragment TAG", getTAG());

		super.onCreate(savedInstanceState);

		MyApp.getAppComponent().inject(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	public void showProgressDialog()
	{
		((BaseActivity) getActivity()).showProgressDialog();
	}

	public void hideProgressDialog()
	{
		((BaseActivity) getActivity()).hideProgressDialog();
	}

	public ImageManager getImageService()
	{
		return ((BaseActivity) getActivity()).getImageService();
	}

	public abstract String getTAG();

	public void baseErrorHandler()
	{
		((BaseActivity) getActivity()).baseErrorHandler();
	}

	@Override
	public void onDestroy()
	{
		cancelPendingTasks(getTAG());
		super.onDestroy();
	}

	public void cancelPendingTasks(String tag)
	{
		((BaseActivity) getActivity()).cancelPendingTasks(tag);
	}

}
