package co.lateralview.myapp.ui.activities.base.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.ui.activities.base.Base;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public abstract class BaseFragment extends Fragment {
    @Inject
    protected ImageManager mImageManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        injectDependencies();
    }

    protected AppComponent getAppComponent() {
        return BaseActivity.getAppComponent();
    }

    protected Base.BaseViewModule getBaseActivityModule() {
        return ((BaseActivity) getActivity()).getBaseActivityModule();
    }

    protected abstract void injectDependencies();

    public void onInternetServiceEnabled() {

    }

    public void onInternetServiceDisabled() {

    }

    public void showProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    public void hideProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }

    public void showComingSoonMessage() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showComingSoonMessage();
        }
    }
}
