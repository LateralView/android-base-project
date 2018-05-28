package co.lateralview.myapp.ui.activities.main;

import dagger.Module;
import dagger.Provides;

public interface Main {
    interface Presenter {
        void login(String email, String password);

        void destroy();
    }

    interface View {
        void showError();
    }

    @Module
    class ViewModule {
        private final View mView;

        public ViewModule(View view) {
            mView = view;
        }

        @Provides
        View provideView() {
            return mView;
        }
    }
}
