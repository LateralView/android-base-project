package co.lateralview.myapp.infraestructure.networking;

import android.app.Application;

import com.google.gson.Gson;

import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.networking.implementation.UserServerImpl;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {
    @Provides
    @Singleton
    public RetrofitManager provideRetrofitManager(Application application,
            Gson gson,
            SessionRepository sessionRepository,
            InternetManager internetManager) {
        return new RetrofitManager(application, gson, sessionRepository, internetManager);
    }

    @Provides
    @Singleton
    public UserServer provideUserServer(UserServerImpl userServer) {
        return userServer;
    }
}