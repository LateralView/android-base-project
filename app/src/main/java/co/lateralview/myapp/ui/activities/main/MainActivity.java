package co.lateralview.myapp.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.databinding.ActivityMainBinding;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.infraestructure.manager.implementation.PendingTask;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.common.MyAppCallback;
import co.lateralview.myapp.ui.util.SnackBarHelper;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView
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
		mBinding.setView(this);

		initializeToolbar(false, "Toolbar Title");

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				SnackBarHelper.createSnackBar(view, new SnackBarData(SnackBarData.SnackBarType.DUMMY)).show();

				//SnackBarHelper.createSnackBar(MainActivity.this, new SnackBarData(SnackBarData.SnackBarType.DUMMY)).show();
			}
		});

		mDummyEditText = mBinding.contentMain.dummyEditText;

		mDummyEditText.setText("Hi world");
	}

	@Override
	protected void initDependencies(AppComponent appComponent)
	{
		appComponent.inject(this);
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
}
