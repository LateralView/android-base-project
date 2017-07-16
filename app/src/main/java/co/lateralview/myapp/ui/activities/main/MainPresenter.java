package co.lateralview.myapp.ui.activities.main;


import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.ui.activities.base.BasePresenter;
import co.lateralview.myapp.ui.util.di.ActivityScoped;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScoped
public class MainPresenter extends BasePresenter implements Main.Presenter
{
    @Inject
    Main.View mView;
    @Inject
    UserRepository mUserRepository;

    protected CompositeDisposable mSubscriptions = new CompositeDisposable();

    @Inject
    MainPresenter()
    {

    }

    @Override
    public void testRx()
    {
        mSubscriptions.add(mUserRepository.login("Username", "Password")
                .subscribe(
                        mView::showResult,
                        error -> mView.showError()
                )
        );
    }
}
