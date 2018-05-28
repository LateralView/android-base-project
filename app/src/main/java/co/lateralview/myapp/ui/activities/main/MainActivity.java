package co.lateralview.myapp.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import co.lateralview.myapp.R;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public class MainActivity extends BaseActivity implements Main.View {
    public static final String TAG = "MainActivity";

    @Inject
    MainPresenter mPresenter;

    public static Intent newInstance(Context fromActivity, boolean clearStack) {
        return BaseActivity.newActivityInstance(fromActivity, clearStack, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initializeToolbar(false);
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    protected void injectDependencies() {
        DaggerMainComponent.builder()
            .appComponent(getAppComponent())
            .baseViewModule(getBaseActivityModule())
            .viewModule(new Main.ViewModule(this))
            .build().inject(this);
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void showError() {

    }
}
