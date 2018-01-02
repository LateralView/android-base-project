package co.lateralview.myapp.application;


import android.app.Application;

import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.RepositoryModule;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.infraestructure.networking.NetModule;
import dagger.Component;

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
    Application application();

    ImageManager imageManager();

    InternetManager internetManager();

    TaskManager taskManager();

    UserRepository userRepository();

    SessionRepository sessionRepository();
}
