package co.lateralview.myapp.ui.activities.base;

import dagger.Module;
import dagger.Provides;

public interface Base {
    interface Presenter {
        //TODO
    }

    interface View {
        void showInternetRequiredError();

        void showUnexpectedErrorHappened();

        void logout();
    }

    @Module
    class BaseViewModule {
        private final View mView;

        public BaseViewModule(View view) {
            mView = view;
        }

        @Provides
        View provideView() {
            return mView;
        }
    }
}
