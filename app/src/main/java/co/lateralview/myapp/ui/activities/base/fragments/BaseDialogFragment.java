package co.lateralview.myapp.ui.activities.base.fragments;

import android.support.v4.app.DialogFragment;

import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.ui.activities.base.Base;
import co.lateralview.myapp.ui.activities.base.BaseActivity;


public class BaseDialogFragment extends DialogFragment {
    protected AppComponent getAppComponent() {
        return ((BaseActivity) getActivity()).getAppComponent();
    }

    protected Base.BaseViewModule getBaseActivityModule() {
        return ((BaseActivity) getActivity()).getBaseActivityModule();
    }
}
