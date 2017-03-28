package co.lateralview.myapp.ui.activities.base.fragments;

import android.app.Fragment;
import android.os.Bundle;

import javax.inject.Inject;

import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.presenter.BasePresenter;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment
{
	public static final String TAG = BaseFragment.class.getSimpleName();

	@Inject
	protected T mPresenter;

	@Inject
	protected ImageManager mImageManager;

	@Inject
	protected SessionRepository mSessionRepository;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		initDependencies(MyApp.getAppComponent());

		if (mPresenter != null)
		{
			mPresenter.attachView(this);
		}
	}

	protected abstract void initDependencies(AppComponent appComponent);

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

		if (mPresenter != null)
		{
			mPresenter.detachView();
		}
		
		super.onDestroy();
	}

	public void cancelPendingTasks(String tag)
	{
		((BaseActivity) getActivity()).cancelPendingTasks(tag);
	}

	public abstract String getTAG();
}
