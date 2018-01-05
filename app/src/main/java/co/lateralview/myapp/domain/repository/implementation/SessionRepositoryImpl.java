package co.lateralview.myapp.domain.repository.implementation;

import javax.inject.Inject;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.interfaces.SharedPreferencesManager;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import io.reactivex.Completable;
import io.reactivex.Single;


public class SessionRepositoryImpl implements SessionRepository
{
    public static final String SHARED_PREFERENCES_ACCESS_TOKEN_KEY =
            "SHARED_PREFERENCES_ACCESS_TOKEN_KEY";

    @Inject
    SharedPreferencesManager mSharedPreferencesManager;

    private User mCurrentUser;
    private String mAccessToken;

    @Inject
    public SessionRepositoryImpl()
    {
    }

    @Override
    public Single isUserLoggedIn()
    {
        return Single.create(e ->
        {
            if (mCurrentUser == null)
            {
                mCurrentUser = mSharedPreferencesManager.get(User.SHARED_PREFERENCE_KEY,
                        User.class);
            }

            e.onSuccess(mCurrentUser != null);
        }).compose(RxSchedulersUtils.applySingleSchedulers());
    }

    @Override
    public Completable logOut()
    {
        return Completable.create(e ->
        {
            mCurrentUser = null;
            mAccessToken = null;
            mSharedPreferencesManager.clearBlocking();
            e.onComplete();
        }).compose(RxSchedulersUtils.applyCompletableSchedulers());
    }

    @Override
    public Completable logIn(User user, String accessToken)
    {
        return save(user).toCompletable()
                .andThen(setAccessToken(accessToken).toCompletable())
                .compose(RxSchedulersUtils.applyCompletableSchedulers());
    }

    @Override
    public Single getCurrentUser()
    {
        return Single.create(e ->
        {
            if (mCurrentUser == null)
            {
                mCurrentUser = mSharedPreferencesManager.get(User.SHARED_PREFERENCE_KEY,
                        User.class);
            }

            e.onSuccess(mCurrentUser);
        }).compose(RxSchedulersUtils.applySingleSchedulers());
    }

    @Override
    public Single<User> updateUser(User user)
    {
        return save(user).compose(RxSchedulersUtils.applySingleSchedulers());
    }

    @Override
    public Single getAccessToken()
    {
        return Single.create(e ->
        {
            if (mAccessToken == null)
            {
                mAccessToken = mSharedPreferencesManager.get(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                        String.class);
            }

            e.onSuccess(mAccessToken);
        }).compose(RxSchedulersUtils.applySingleSchedulers());
    }

    private Single<String> setAccessToken(String accessToken)
    {
        return Single.create(e ->
        {
            mSharedPreferencesManager.saveBlocking(SHARED_PREFERENCES_ACCESS_TOKEN_KEY,
                    accessToken);
            mAccessToken = accessToken;

            e.onSuccess(mAccessToken);
        });
    }

    private Single<User> save(User newUser)
    {
        return Single.create(e ->
        {
            mSharedPreferencesManager.saveBlocking(User.SHARED_PREFERENCE_KEY, newUser);

            mCurrentUser = newUser;

            e.onSuccess(mCurrentUser);
        });
    }
}
