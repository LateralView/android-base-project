package co.lateralview.myapp.ui.activities.main;


import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.ui.activities.base.BasePresenter;
import co.lateralview.myapp.ui.util.di.ActivityScoped;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScoped
public class MainPresenter extends BasePresenter implements Main.Presenter
{
    public static final String TAG = "MainPresenter";

    protected CompositeDisposable mSubscriptions = new CompositeDisposable();

    @Inject
    Main.View mView;
    @Inject
    UserRepository mUserRepository;

    @Inject
    MainPresenter()
    {

    }

    @Override
    public void login(String email, String password)
    {
        mSubscriptions.add(mUserRepository.login(email, password)
                .flatMapCompletable(user -> mSessionRepository.logIn(user, "token"))
                .subscribe(() -> {/*login done*/},
                        error -> mView.showError()));
    }

    @Override
    public void destroy()
    {
        mSubscriptions.dispose();
    }
}
