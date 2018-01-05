package co.lateralview.myapp.domain.repository;

import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.implementation.SessionRepositoryImpl;
import co.lateralview.myapp.domain.repository.implementation.UserRepositoryImpl;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule
{
    @Provides
    @Singleton
    public SessionRepository provideSessionRepository(
            SessionRepositoryImpl sessionRepository)
    {
        return sessionRepository;
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserRepositoryImpl userRepository)
    {
        return userRepository;
    }
}