package co.lateralview.myapp.ui.activities.base.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public abstract class BaseFragment extends Fragment
{
	public static final String TAG = BaseFragment.class.getSimpleName();

	@Inject
	protected ImageManager mImageManager;

	@Inject
	protected SessionRepository mSessionRepository;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		MyApp.getAppComponent().inject(this);
	}

	protected void changeToolbarTitle(String title)
	{
		((BaseActivity) getActivity()).setToolbarTitle(title);
	}

	public void showProgressDialog()
	{
		((BaseActivity) getActivity()).showProgressDialog();
	}

	public void hideProgressDialog()
	{
		((BaseActivity) getActivity()).hideProgressDialog();
	}

	public void baseErrorHandler(MyAppServerError error)
	{
		((BaseActivity) getActivity()).baseErrorHandler(error);
	}

	public void baseErrorHandler(MyAppServerError nutrilonServerError, PendingTask pendingTask)
	{
		((BaseActivity) getActivity()).baseErrorHandler(nutrilonServerError, pendingTask);
	}

	public void showComingSoonMessage()
	{
		((BaseActivity) getActivity()).showComingSoonMessage();
	}

	@Override
	public void onDestroy()
	{
		cancelPendingTasks(getTAG());

		unsubscribeObservers();

		super.onDestroy();
	}

	public void cancelPendingTasks(String tag)
	{
		((BaseActivity) getActivity()).cancelPendingTasks(tag);
	}

	public abstract String getTAG();

	public void unsubscribeObservers() {};

}
