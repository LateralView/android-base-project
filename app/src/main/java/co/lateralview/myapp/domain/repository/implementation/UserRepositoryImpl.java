package co.lateralview.myapp.domain.repository.implementation;

import java.util.concurrent.Callable;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import co.lateralview.myapp.infraestructure.util.RxUtils;
import rx.Observable;

public class UserRepositoryImpl implements UserRepository
{
	private UserServer mUserServer;

	public UserRepositoryImpl(UserServer userServer)
	{
		mUserServer = userServer;
	}

	@Override
	public Observable login(final String userEmail, final String userPassword)
	{
		return RxUtils.newObservableFromIoToMainThread(new Callable<User>()
		{
			@Override
			public User call() //All background work
			{
				return mUserServer.signIn(userEmail, userPassword);
			}
		});
	}
}
