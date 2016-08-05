package co.lateralview.myapp.domain.repository.implementation;

import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import co.lateralview.myapp.ui.common.MyAppCallback;

public class UserRepositoryImpl implements UserRepository
{
	private UserServer mUserServer;

	public UserRepositoryImpl(UserServer userServer)
	{
		mUserServer = userServer;
	}

	@Override
	public void login(String userEmail, String userPassword, MyAppCallback callback, String tag)
	{
		mUserServer.signIn(userEmail, userPassword, callback, tag);
	}
}
