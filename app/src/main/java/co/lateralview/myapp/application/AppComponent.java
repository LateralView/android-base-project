package co.lateralview.myapp.application;


import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.RepositoryModule;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.infraestructure.networking.NetModule;
import dagger.Component;

/**
 * Created by julianfalcionelli on 5/12/16.
 */
@Singleton
@Component(
        modules = {
                AppModule.class,
                NetModule.class,
                RepositoryModule.class
        }
)
public interface AppComponent
{
    ImageManager imageManager();

    InternetManager internetManager();

    TaskManager taskManager();

    UserRepository userReposiroty();

    SessionRepository sessionReposiroty();
}
