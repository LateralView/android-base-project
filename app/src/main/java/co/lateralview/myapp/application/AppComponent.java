package co.lateralview.myapp.application;


import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.RepositoryModule;
import co.lateralview.myapp.infraestructure.networking.NetModule;
import co.lateralview.myapp.ui.activities.base.BaseActivity;
import co.lateralview.myapp.ui.activities.base.fragments.BaseFragment;
import co.lateralview.myapp.ui.activities.main.MainActivity;
import co.lateralview.myapp.ui.presenter.PresenterModule;
import dagger.Component;

/**
 * Created by julianfalcionelli on 5/12/16.
 */
@Singleton
@Component(
		modules = {
				AppModule.class,
				NetModule.class,
				RepositoryModule.class,
				PresenterModule.class
		}
)
public interface AppComponent
{

	void inject(MainActivity activity);

}
