package co.lateralview.myapp.domain.repository.implementation;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {

    @Mock UserServer mUserServer;
    @InjectMocks UserRepositoryImpl mUserRepository;

    @BeforeClass
    public static void setUpClass()
    {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

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
