package co.lateralview.myapp.domain.repository.implementation;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.interfaces.SharedPreferencesManager;


public class SessionRepositoryImpl implements SessionRepository
{
	private SharedPreferencesManager mSharedPreferencesManager;
	private User mCurrentUser;

	public SessionRepositoryImpl(SharedPreferencesManager sharedPreferencesManager)
	{
		mSharedPreferencesManager = sharedPreferencesManager;
	}

	public boolean isUserLoggedIn()
	{
		User currentUser = getCurrentUser();
		return currentUser != null;
	}

	public void logOut()
	{
		mCurrentUser = null;

		mSharedPreferencesManager.clear();
	}

	public void logIn(User user)
	{
		save(user);
	}

	public User getCurrentUser()
	{
		if (mCurrentUser == null)
		{
			mCurrentUser = mSharedPreferencesManager.get(User.SHARED_PREFERENCE_KEY, User.class);
		}

		return mCurrentUser;
	}

	public void updateUser(User user)
	{
		save(user);
	}

	private void save(User newUser)
	{
		mCurrentUser = newUser;

		mSharedPreferencesManager.save(User.SHARED_PREFERENCE_KEY, newUser);
	}
}
