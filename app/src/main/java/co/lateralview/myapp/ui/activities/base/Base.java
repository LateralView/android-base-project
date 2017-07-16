/*
 * Copyright © 2017 Razer Inc. All rights reserved.
 */

package co.lateralview.myapp.ui.activities.base;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

public interface Base
{
    interface Presenter
    {
        //TODO
    }

    interface View
    {
        //TODO
    }

    @Module
    class BaseViewModule
    {
        private final View mView;
        private final Activity mActivity;

        public BaseViewModule(View view, Activity activity)
        {
            mView = view;
            mActivity = activity;
        }

        @Provides
        View provideView()
        {
            return mView;
        }

        @Provides
        Activity activity() {
            return mActivity;
        }
    }
}
