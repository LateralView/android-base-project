package co.lateralview.myapp.ui.activities.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.infraestructure.networking.RestConstants;
import co.lateralview.myapp.infraestructure.networking.interfaces.BaseServer;
import co.lateralview.myapp.ui.broadcast.InternetReceiver;
import co.lateralview.myapp.ui.presenter.BasePresenter;
import co.lateralview.myapp.ui.util.SnackBarHelper;
import co.lateralview.myapp.ui.util.ToolbarUtils;


public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements InternetReceiver.InternetReceiverListener
{
	public static final String TAG = BaseActivity.class.getSimpleName();

	@Inject
	protected T mPresenter;

	@Inject
	protected SessionRepository mSessionRepository;
	@Inject
	protected ImageManager mImageManager;
	@Inject
	protected InternetManager mInternetManager;
	@Inject
	protected BaseServer mBaseServer;
	@Inject
	protected TaskManager mTaskManager; //Help us to retry failed tasks

	private ProgressDialog mProgressDialog;
	private InternetReceiver mInternetReceiver;
	private IntentFilter mInternetStatusChangedFilter;
	private boolean mProcessInternetChangeReceiver = false;

	private Snackbar mNoInternetSnackbar, mConnectionErrorSnackbar;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		MyApp.setCurrentScreenTag(getTAG());

		initDependencies(MyApp.getAppComponent());

		initInternetBroadcastReceiver();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void initializeToolbar(boolean backEnabled)
	{
		initializeToolbar(backEnabled, null);
	}

	public void initializeToolbar(boolean backEnabled, @Nullable int title)
	{
		initializeToolbar(backEnabled, getString(title));
	}

	public void initializeToolbar(boolean backEnabled, @Nullable String title)
	{
		ToolbarUtils.initializeToolbar(this, backEnabled, title);
	}

	public void setActionBarVisibility(int visibility)
	{
		ToolbarUtils.setActionBarVisibility(this, visibility);
	}

	public void setToolbarTitle(String title)
	{
		ToolbarUtils.setToolbarTitle(this, title);
	}

	protected abstract void initDependencies(AppComponent appComponent);

	protected static Intent newActivityInstance(Context fromActivity, boolean clearStack, Class toActivity)
	{
		Intent intent = new Intent(fromActivity, toActivity);

		if (clearStack)
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		return intent;
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		registerReceiver(mInternetReceiver, mInternetStatusChangedFilter);
	}


	@Override
	protected void onStop()
	{
		unregisterReceiver(mInternetReceiver);

		super.onStop();
	}

	private void initInternetBroadcastReceiver()
	{
		mInternetReceiver = new InternetReceiver(this, mInternetManager);
		mInternetStatusChangedFilter = mInternetReceiver.getIntentFilter();
	}

	@Override
	public void onInternetServiceEnabled()
	{
		if (mProcessInternetChangeReceiver)
		{
			if (mNoInternetSnackbar != null)
			{
				mNoInternetSnackbar.dismiss();
			}

			mTaskManager.callPendingTasks();
		} else
		{
			mProcessInternetChangeReceiver = true;
		}
	}

	@Override
	public void onInternetServiceDisabled()
	{
		if (!mProcessInternetChangeReceiver)
		{
			mProcessInternetChangeReceiver = true;
		}

		if (canShowInternetConnectionProblemsSnackbar())
		{
			showInternetConnectionProblemsSnackbar();
		}
	}

	public void showProgressDialog(String message)
	{
		if (mProgressDialog == null)
		{
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setCancelable(false);
		}

		if (!mProgressDialog.isShowing())
		{
			mProgressDialog.show();
			mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mProgressDialog.setContentView(R.layout.layout_progress_dialog);

			if (message != null)
			{
				mProgressDialog.setMessage(message);
			}
		}
	}

	public void showProgressDialog()
	{
		showProgressDialog(null);
	}

	public void hideProgressDialog()
	{
		if (mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
	}

	public abstract String getTAG();

	public void baseErrorHandler(MyAppServerError error)
	{
		baseErrorHandler(error, null);
	}

	public void baseErrorHandler(MyAppServerError error, final PendingTask pendingTask)
	{
		if (error != null)
		{
			switch (RestConstants.Subcode.fromInt(error.getErrorCode()))
			{
				case INVALID_TOKEN:
				{
					mSessionRepository.logOut();
					//Do something
					return;
				}
			}
		}

		if (pendingTask != null)
		{
			mTaskManager.addTask(pendingTask);
		}

		if (!mInternetManager.isOnline())
		{
			showInternetConnectionProblemsSnackbar();
		} else
		{
			SnackBarData snackBarData = new SnackBarData(SnackBarData.SnackBarType.CONNECTION_ERROR, null);

			if (pendingTask != null)
			{
				snackBarData.setSnackBarListener(new SnackBarHelper.ISnackBarHandler()
				{
					@Override
					public void onActionClick()
					{
						pendingTask.callPendingTask();
					}
				});
			}

			mConnectionErrorSnackbar = SnackBarHelper.createSnackBar(this, snackBarData);
			mConnectionErrorSnackbar.show();
		}
	}

	@Override
	protected void onDestroy()
	{
		cancelPendingTasks(getTAG());

		super.onDestroy();
	}

	public void cancelPendingRequest(String tag)
	{
		mBaseServer.cancelRequest(tag);
	}

	public void cancelPendingTasks(String tag)
	{
		cancelPendingRequest(tag);
		mTaskManager.removeTasks(tag);
	}

	protected void showInternetConnectionProblemsSnackbar()
	{
		if (mNoInternetSnackbar == null)
		{
			mNoInternetSnackbar = SnackBarHelper.createSnackBar(this, new SnackBarData(SnackBarData.SnackBarType.NO_INTERNET, null));
		}

		mNoInternetSnackbar.show();
	}

	protected boolean canShowInternetConnectionProblemsSnackbar()
	{
		return true;
	}

	public void showComingSoonMessage()
	{
		Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
	}
}
