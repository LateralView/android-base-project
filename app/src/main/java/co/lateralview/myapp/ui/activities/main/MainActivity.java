package co.lateralview.myapp.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.lateralview.simplerestclienthandler.base.HttpErrorException;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.databinding.ActivityMainBinding;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.util.SnackBarHelper;
import co.lateralview.myapp.ui.util.SystemUtils;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

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
		fab.setOnClickListener(view -> {
            SnackBarHelper.createSnackBar(view, new SnackBarData(SnackBarData.SnackBarType.DUMMY)).show();
            testRx();
        });

		mDummyEditText = mBinding.contentMain.dummyEditText;

		mDummyEditText.setText("Hi world");
	}

	public String getTAG()
	{
		return TAG;
	}

	private Disposable mSubscription;

	private void testRx()
	{
		mSubscription = mUserRepository.login("Username", "Password")
				.subscribe(
                        user -> Log.i(TAG, "onNext: " + user + " - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread())),

                        error -> {
                            if (error instanceof HttpErrorException)
                            {
                                MyAppServerError myAppServerError = ((MyAppServerError) ((HttpErrorException) error).getError());
                                //Do something
                            }

                            Log.i(TAG, "onError - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()));
                        },

                        () ->  Log.i(TAG, "onCompleted - Running on UI Thread: " + String.valueOf(SystemUtils.isRunningOnMainThread()))
                );
	}

	@Override
	public void unsubscribeObservers()
	{
		if (mSubscription != null && !mSubscription.isDisposed())
		{
			mSubscription.dispose();
		}
	}
}
