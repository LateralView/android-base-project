package co.lateralview.myapp.ui.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.infraestructure.networking.interfaces.BaseServer;
import co.lateralview.myapp.ui.broadcast.InternetReceiver;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity implements InternetReceiver.InternetReceiverListener
{
	public static final String TAG = BaseActivity.class.getSimpleName();

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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		MyApp.setCurrentScreenTag(getTAG());

		MyApp.getAppComponent().inject(this);

		initInternetBroadcastReceiver();
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
		Log.i("InternetReceiver", "Enabled");
		if (mProcessInternetChangeReceiver)
		{
			//Dismiss internet problem alert
			mTaskManager.callPendingTasks();
		} else
		{
			mProcessInternetChangeReceiver = true;
		}
	}

	@Override
	public void onInternetServiceDisabled()
	{
		Log.i("InternetReceiver", "Disabled");
		if (!mProcessInternetChangeReceiver)
		{
			mProcessInternetChangeReceiver = true;
		}
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	public void showProgressDialog()
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
		}
	}

	public void hideProgressDialog()
	{
		if (mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
	}

	public abstract String getTAG();

	public ImageManager getImageService()
	{
		return mImageManager;
	}

	public void baseErrorHandler()
	{
		baseErrorHandler(null);
	}

	public void baseErrorHandler(PendingTask pendingTask)
	{
		if (!mInternetManager.isOnline())
		{
			//Show Internet Problem
		} else
		{
			//Show Connection Problem
		}

		if (pendingTask != null)
		{
			mTaskManager.addTask(pendingTask);
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
}
