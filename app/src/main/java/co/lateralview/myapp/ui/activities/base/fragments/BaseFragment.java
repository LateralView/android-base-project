package co.lateralview.myapp.ui.activities.base.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.ui.activities.base.Base;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public abstract class BaseFragment extends Fragment
{
    @Inject
    protected ImageManager mImageManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        injectDependencies();
    }

    protected AppComponent getAppComponent()
    {
        return ((BaseActivity) getActivity()).getAppComponent();
    }

    protected Base.BaseViewModule getBaseActivityModule()
    {
        return ((BaseActivity) getActivity()).getBaseActivityModule();
    }

    protected abstract void injectDependencies();

    protected void changeToolbarTitle(String title)
    {
        ((BaseActivity) getActivity()).setToolbarTitle(title);
    }

    public void onInternetServiceEnabled()
    {

    }

    public void onInternetServiceDisabled()
    {

    }

    public void showProgressDialog()
    {
        ((BaseActivity) getActivity()).showProgressDialog();
    }

    public void hideProgressDialog()
    {
        ((BaseActivity) getActivity()).hideProgressDialog();
    }

    public void showComingSoonMessage()
    {
        ((BaseActivity) getActivity()).showComingSoonMessage();
    }
}
