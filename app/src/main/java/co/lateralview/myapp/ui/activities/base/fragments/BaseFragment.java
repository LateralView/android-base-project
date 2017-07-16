package co.lateralview.myapp.ui.activities.base.fragments;

import android.app.Fragment;

import javax.inject.Inject;

import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.ui.activities.base.BaseActivity;

public abstract class BaseFragment extends Fragment
{
    @Inject
    protected ImageManager mImageManager;

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
