package co.lateralview.myapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.databinding.ActivityMainBinding;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.common.MyAppCallback;
import co.lateralview.myapp.ui.util.ToolbarUtils;

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

		ToolbarUtils.setupActionBar(this, false, "Toolbar Title");

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
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
				baseErrorHandler(new PendingTask(getTAG(), new PendingTask.ITasksListener()
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
}
