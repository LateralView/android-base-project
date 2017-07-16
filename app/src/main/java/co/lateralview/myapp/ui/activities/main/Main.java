/*
 * Copyright © 2017 Razer Inc. All rights reserved.
 */

package co.lateralview.myapp.ui.activities.main;

import co.lateralview.myapp.domain.model.User;
import dagger.Module;
import dagger.Provides;

public interface Main
{
    interface Presenter
    {
        void testRx();
    }

    interface View
    {
        void showResult(User user);

        void showError();
    }

    @Module
    class ViewModule
    {
        private final View mView;

        public ViewModule(View view)
        {
            mView = view;
        }

        @Provides
        View provideView()
        {
            return mView;
        }
    }
}
