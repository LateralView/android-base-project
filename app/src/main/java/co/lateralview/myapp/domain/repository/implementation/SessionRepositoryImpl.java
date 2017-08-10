package co.lateralview.myapp.domain.repository.implementation;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.interfaces.SharedPreferencesManager;


public class SessionRepositoryImpl implements SessionRepository
{
    public static final String SHARED_PREFERENCES_ACCESS_TOKEN_KEY = "AccessToken";

    private SharedPreferencesManager mSharedPreferencesManager;
    private User mCurrentUser;
    private String mAccessToken;

    public SessionRepositoryImpl(SharedPreferencesManager sharedPreferencesManager)
    {
        mSharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public boolean isUserLoggedIn()
    {
        User currentUser = getCurrentUser();
        return currentUser != null;
    }

    @Override
    public void logOut()
    {
        mCurrentUser = null;

        mSharedPreferencesManager.clear();
    }


    @Override
    public void logIn(User user, String accessToken)
    {
        save(user);
        setAccessToken(accessToken);
    }

    @Override
    public User getCurrentUser()
    {
        if (mCurrentUser == null)
        {
            mCurrentUser = mSharedPreferencesManager.get(User.SHARED_PREFERENCE_KEY, User.class);
        }

        return mCurrentUser;
    }

    @Override
    public void updateUser(User user)
    {
        save(user);
    }

    @Override
    public String getAccessToken()
    {
        if (mAccessToken == null)
        {
            mAccessToken = mSharedPreferencesManager.get(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                    String.class);
        }

        return mAccessToken;
    }

    public void setAccessToken(String accessToken)
    {
        mAccessToken = accessToken;

        mSharedPreferencesManager.save(SHARED_PREFERENCES_ACCESS_TOKEN_KEY, accessToken);
    }

    private void save(User newUser)
    {
        mCurrentUser = newUser;

        mSharedPreferencesManager.save(User.SHARED_PREFERENCE_KEY, newUser);
    }
}
