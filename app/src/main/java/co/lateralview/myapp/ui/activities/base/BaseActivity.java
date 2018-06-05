package co.lateralview.myapp.ui.activities.base;

import android.app.Fragment;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.lateralview.myapp.R;
import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.util.SnackBarData;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.ui.activities.base.fragments.BaseFragment;
import co.lateralview.myapp.ui.broadcast.InternetReceiver;
import co.lateralview.myapp.ui.util.SnackBarHelper;
import co.lateralview.myapp.ui.util.ToolbarUtils;


public abstract class BaseActivity
        extends AppCompatActivity
        implements InternetReceiver.InternetReceiverListener, Base.View {

    public static final String TAG = "BaseActivity";

    private static boolean isAppVisible = false;

    @Inject
    protected InternetManager mInternetManager;

    private ProgressDialog mProgressDialog;
    private InternetReceiver mInternetReceiver;
    private IntentFilter mInternetStatusChangedFilter;
    private boolean mProcessInternetChangeReceiver = false;

    private Snackbar mNoInternetSnackbar;
    private Snackbar mConnectionErrorSnackbar;

    private List<WeakReference<Fragment>> mFragments = new ArrayList<>();

    protected static Intent newActivityInstance(Context fromActivity, boolean clearStack,
            Class toActivity) {
        Intent intent = new Intent(fromActivity, toActivity);

        if (clearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

    public static AppComponent getAppComponent() {
        return MyApp.getAppComponent();
    }

    public static boolean isAppVisible() {
        return isAppVisible;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApp.setCurrentScreenTag(getTAG());

        enterTransition();

        injectDependencies();

        initInternetBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAppVisible = true;
    }

    @Override
    protected void onPause() {
        isAppVisible = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mInternetReceiver);

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mInternetReceiver, mInternetStatusChangedFilter);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        mFragments.add(new WeakReference(fragment));
    }

    @Override
    public void onInternetServiceEnabled() {
        if (mProcessInternetChangeReceiver) {
            if (mNoInternetSnackbar != null) {
                mNoInternetSnackbar.dismiss();
            }

        } else {
            mProcessInternetChangeReceiver = true;
        }

        notifyFragmentsConnectivityChange(true);
    }

    @Override
    public void onInternetServiceDisabled() {
        if (!mProcessInternetChangeReceiver) {
            mProcessInternetChangeReceiver = true;
        }

        if (canShowInternetConnectionProblemsSnackbar()) {
            showInternetConnectionProblemsSnackbar();
        }

        notifyFragmentsConnectivityChange(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitTransition();
    }

    @Override
    public void showInternetRequiredError() {
        //TODO
    }

    @Override
    public void showUnexpectedErrorHappened() {
        //TODO
    }

    @Override
    public void logout() {
        //TODO
    }

    public void initializeToolbar(boolean backEnabled) {
        initializeToolbar(backEnabled, null);
    }

    public void initializeToolbar(boolean backEnabled, @Nullable int title) {
        initializeToolbar(backEnabled, getString(title));
    }

    public void initializeToolbar(boolean backEnabled, @Nullable String title) {
        ToolbarUtils.initializeToolbar(this, backEnabled, title);
    }

    public void setActionBarVisibility(int visibility) {
        ToolbarUtils.setActionBarVisibility(this, visibility);
    }

    public void setToolbarTitle(String title) {
        ToolbarUtils.setToolbarTitle(this, title);
    }

    protected void injectDependencies() {
        DaggerBaseComponent.builder()
                .appComponent(getAppComponent())
                .baseViewModule(getBaseActivityModule())
                .build()
                .inject(this);
    }

    public Base.BaseViewModule getBaseActivityModule() {
        return new Base.BaseViewModule(this);
    }

    private void initInternetBroadcastReceiver() {
        mInternetReceiver = new InternetReceiver(this, mInternetManager);
        mInternetStatusChangedFilter = mInternetReceiver.getIntentFilter();
    }

    private void notifyFragmentsConnectivityChange(boolean enabled) {
        for (WeakReference<Fragment> fragmentWeakReference : mFragments) {
            Fragment fragment = fragmentWeakReference.get();

            if (fragment != null && fragment instanceof BaseFragment) {
                BaseFragment baseFragment = (BaseFragment) fragment;

                if (enabled) {
                    baseFragment.onInternetServiceEnabled();
                } else {
                    baseFragment.onInternetServiceDisabled();
                }
            }
        }
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
            mProgressDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.layout_progress_dialog);

            if (message != null) {
                mProgressDialog.setMessage(message);
            }
        }
    }

    public void showProgressDialog() {
        showProgressDialog(null);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public abstract String getTAG();

    protected void showInternetConnectionProblemsSnackbar() {
        if (mNoInternetSnackbar == null) {
            mNoInternetSnackbar = SnackBarHelper.createSnackBar(this,
                    new SnackBarData(SnackBarData.SnackBarType.NO_INTERNET, null));
        }

        mNoInternetSnackbar.show();
    }

    protected boolean canShowInternetConnectionProblemsSnackbar() {
        return true;
    }

    public void showComingSoonMessage() {
        Toast.makeText(this, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
    }

    protected void enterTransition() {
        /*override method on child's*/
    }

    protected void exitTransition() {
        /*override method on child's*/
    }
}
