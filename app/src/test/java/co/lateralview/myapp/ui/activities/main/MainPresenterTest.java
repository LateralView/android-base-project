package co.lateralview.myapp.ui.activities.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import io.reactivex.Completable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock Main.View mView;
    @Mock UserRepository mUserRepository;
    @Mock SessionRepository mSessionRepository;
    @InjectMocks MainPresenter mMainPresenter;

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";

    private User user = new User();

    @Test
    public void itShouldLoginSuccessfully() {
        when(mUserRepository.login(EMAIL, PASSWORD)).thenReturn(Single.just(user));
        when(mSessionRepository.logIn(user, TOKEN)).thenReturn(Completable.complete());

        mMainPresenter.login(EMAIL, PASSWORD);
        verify(mUserRepository).login(EMAIL, PASSWORD);
        verify(mSessionRepository).logIn(user, TOKEN);
        verifyZeroInteractions(mView);
    }

    @Test
    public void itShouldShowErrorIfLoginFails() {
        when(mUserRepository.login(EMAIL, PASSWORD)).thenReturn(Single.error(new Exception()));

        mMainPresenter.login(EMAIL, PASSWORD);
        verify(mUserRepository).login(EMAIL, PASSWORD);
        verify(mSessionRepository, never()).logIn(isA(User.class), eq(TOKEN));
        verify(mView).showError();
    }

    @Test
    public void itShouldShowErrorIfSessionFails() {
        when(mUserRepository.login(EMAIL, PASSWORD)).thenReturn(Single.just(user));
        when(mSessionRepository.logIn(user, "token")).thenReturn(Completable.error(new Exception()));

        mMainPresenter.login(EMAIL, PASSWORD);
        verify(mUserRepository).login(EMAIL, PASSWORD);
        verify(mSessionRepository).logIn(isA(User.class), eq(TOKEN));
        verify(mView).showError();
    }
}
