package co.lateralview.myapp.domain.repository.implementation;

import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import io.reactivex.Single;

public class UserRepositoryImpl implements UserRepository
{
    private UserServer mUserServer;

    public UserRepositoryImpl(UserServer userServer)
    {
        mUserServer = userServer;
    }

    @Override
    public Single login(final String email, final String password)
    {
        return mUserServer.login(email, password)
                .compose(RxSchedulersUtils.applySingleSchedulers());
    }
}
