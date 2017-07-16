package co.lateralview.myapp.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lateralview.myapp.R;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.util.SnackBarHelper;
import co.lateralview.myapp.ui.util.SystemUtils;

public class MainActivity extends BaseActivity<MainPresenter> implements Main.View
{
    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.dummyEditText)
    EditText mDummyEditText;

    public static Intent newInstance(Context fromActivity, boolean clearStack)
    {
        return BaseActivity.newActivityInstance(fromActivity, clearStack, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initializeToolbar(false, "Toolbar Title");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
        {
            SnackBarHelper.createSnackBar(view,
                    new SnackBarData(SnackBarData.SnackBarType.DUMMY)).show();
            mPresenter.testRx();
        });

        mDummyEditText.setText("Hi world");
    }

    @Override
    protected void injectDependencies()
    {
        DaggerMainActivityComponent.builder()
                .appComponent(getAppComponent())
                .baseViewModule(getBaseActivityModule())
                .viewModule(new Main.ViewModule(this))
                .build().inject(this);
    }

    public String getTAG()
    {
        return TAG;
    }

    @Override
    public void showResult(User user)
    {
        Log.i(TAG, "onNext: " + user + " - Running on UI Thread: " + String.valueOf(
                SystemUtils.isRunningOnMainThread()));
    }

    @Override
    public void showError()
    {
        Log.i(TAG, "onError - Running on UI Thread: " + String.valueOf(
                SystemUtils.isRunningOnMainThread()));
    }
}
