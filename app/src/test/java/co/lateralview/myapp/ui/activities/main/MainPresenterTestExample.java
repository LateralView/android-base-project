package co.lateralview.myapp.ui.activities.main;

import org.junit.Before;
import org.junit.Test;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.ui.activities.main.Main.View;
import io.reactivex.Completable;
import io.reactivex.Single;

import static org.junit.Assert.assertEquals;

public class MainPresenterTestExample {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private MockView mView = new MockView();
    private MockUserRepository mUserRepository = new MockUserRepository();
    private MockSessionRepository mSessionRepository = new MockSessionRepository();

    private MainPresenter mMainPresenter;

    @Before
    public void setUpTest() {
        mMainPresenter = new MainPresenter(mView, mUserRepository, mSessionRepository);
    }

    @Test
    public void itShouldLoginSuccessfully() {
        mMainPresenter.login(EMAIL, PASSWORD);
        assertEquals(null, mView.getError());
    }

    @Test
    public void itShouldShowErrorIfLoginFails() {
        mMainPresenter.login(EMAIL, "error");
        assertEquals("Login error", mView.getError());
    }

    static class MockUserRepository implements UserRepository {

        @Override
        public Single<User> login(final String userEmail, final String userPassword) {
            if (EMAIL.equals(userEmail) && PASSWORD.equals(userPassword)) {
                return Single.just(new User());
            } else {
                return Single.error(new Exception());
            }
        }
    }

    static class MockView implements View {

        private String error;

        public String getError() {
            return error;
        }

        @Override
        public void showError() {
            error = "Login error";
        }
    }

    static class MockSessionRepository implements SessionRepository {

        @Override
        public Single<Boolean> isUserLoggedIn() {
            return null;
        }

        @Override
        public Completable logOut() {
            return null;
        }

        @Override
        public Completable logIn(final User user, final String accessToken) {
            if (user != null) {
                return Completable.complete();
            } else {
                return Completable.error(new Exception());
            }
        }

        @Override
        public Single<User> getCurrentUser() {
            return null;
        }

        @Override
        public Single<User> updateUser(final User user) {
            return null;
        }

        @Override
        public Single<String> getAccessToken() {
            return null;
        }
    }
}
