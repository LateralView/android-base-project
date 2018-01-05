package co.lateralview.myapp.domain.repository.implementation;

import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository
{
    @Inject
    UserServer mUserServer;

    @Inject
    public UserRepositoryImpl()
    {
    }

    @Override
    public Single login(final String email, final String password)
    {
        return mUserServer.login(email, password)
                .compose(RxSchedulersUtils.applySingleSchedulers());
    }
}
