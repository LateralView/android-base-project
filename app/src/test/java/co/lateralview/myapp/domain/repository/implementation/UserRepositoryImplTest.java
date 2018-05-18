package co.lateralview.myapp.domain.repository.implementation;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import co.lateralview.myapp.TrampolineSchedulerRule;
import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {

    @ClassRule public static TrampolineSchedulerRule mTrampolineSchedulerRule = new TrampolineSchedulerRule();

    @Mock UserServer mUserServer;
    @InjectMocks UserRepositoryImpl mUserRepository;

    @Test
    public void itShouldLoginUser()
    {
        String email = "email";
        String password = "password";
        User user = new User();
        when(mUserServer.login(email, password)).thenReturn(Single.just(user));

        TestObserver<User> observer = mUserRepository.login(email, password).test();
        observer.assertValue(user);
    }
}
