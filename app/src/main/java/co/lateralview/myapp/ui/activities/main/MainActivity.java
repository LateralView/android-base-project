package co.lateralview.myapp.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.databinding.ActivityMainBinding;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.infraestructure.util.RxUtils;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.common.MyAppCallback;
import co.lateralview.myapp.ui.util.SnackBarHelper;
import co.lateralview.myapp.ui.util.SystemUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class MainActivity extends BaseActivity
{
	public static final String TAG = MainActivity.class.getSimpleName();

	@Inject
	protected UserRepository mUserRepository;

	protected ActivityMainBinding mBinding;

	protected EditText mDummyEditText;

	public static Intent newInstance(Context fromActivity, boolean clearStack)
	{
		return BaseActivity.newActivityInstance(fromActivity, clearStack, MainActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		MyApp.getAppComponent().inject(this);

		initializeToolbar(false, "Toolbar Title");

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				SnackBarHelper.createSnackBar(view, new SnackBarData(SnackBarData.SnackBarType.DUMMY)).show();
				testRx();
			}
		});

		mDummyEditText = mBinding.contentMain.dummyEditText;

		mDummyEditText.setText("Hi world");
	}

	private void login(final String userEmail, final String userPassword)
	{
		mUserRepository.login(userEmail, userPassword, new MyAppCallback<User, MyAppServerError>()
		{
			@Override
			public void onSuccess(User user)
			{
				mSessionRepository.logIn(user);
			}

			@Override
			public void onError(MyAppServerError error)
			{
				//if cant handler the error call base error handler
				baseErrorHandler(error, new PendingTask(getTAG(), new PendingTask.ITasksListener()
				{
					@Override
					public void callPendingTask()
					{
						login(userEmail, userPassword);
					}
				}));
			}
		}, TAG);
	}

	public String getTAG()
	{
		return TAG;
	}

	private Subscription mSubscription;

	private void testRx()
	{
		mSubscription = getDelayedTask()
				.subscribe(new Observer<String>()
				{
					@Override
					public void onCompleted()
					{
						Log.i(TAG, "onCompleted - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()));
					}

					@Override
					public void onError(Throwable e)
					{
						Log.i(TAG, "onError - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()));
					}

					@Override
					public void onNext(String s)
					{
						Log.i(TAG, "onNext: " + s + " - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()));
					}
				});
	}

	@Override
	protected void onDestroy()
	{
		if (mSubscription != null && !mSubscription.isUnsubscribed())
		{
			mSubscription.unsubscribe();
		}

		super.onDestroy();
	}

	private Observable<String> getDelayedTask() //TODO: This should be on the respect repository
	{
		return RxUtils.newObservableFromIoToMainThread(new Callable<String>() {

			@Override
			public String call() {
				return delayedTask();
			}
		});
	}

	private String delayedTask()
	{
		Log.i(TAG, "delayedTask - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()));

		try
		{
			Thread.sleep(5000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return "Lalala";
	}
}
