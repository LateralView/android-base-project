package co.lateralview.myapp.ui.activities.splash;

import android.os.Bundle;
import android.os.Handler;

import co.lateralview.myapp.R;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.activities.base.DaggerBaseComponent;
import co.lateralview.myapp.ui.activities.main.MainActivity;
import co.lateralview.myapp.ui.util.SystemUtils;

public class SplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        SystemUtils.fullScreenMode(this);

        new Handler().postDelayed(() -> start(),
            2000);
    }

    public String getTAG() {
        return TAG;
    }

    @Override
    protected void injectDependencies() {
        DaggerBaseComponent.builder()
            .appComponent(getAppComponent())
            .baseViewModule(getBaseActivityModule())
            .build().inject(this);
    }

    private void start() {
        startActivity(MainActivity.newInstance(SplashActivity.this, true));
    }
}
