package co.lateralview.myapp.domain.repository;

import javax.inject.Singleton;

import co.lateralview.myapp.domain.repository.implementation.SessionRepositoryImpl;
import co.lateralview.myapp.domain.repository.implementation.UserRepositoryImpl;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.domain.repository.interfaces.UserRepository;
import co.lateralview.myapp.infraestructure.manager.interfaces.SharedPreferencesManager;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule
{
	@Provides
	@Singleton
	public SessionRepository provideSessionRepository(SharedPreferencesManager sharedPreferencesManager)
	{
		return new SessionRepositoryImpl(sharedPreferencesManager);
	}

	@Provides
	@Singleton
	public UserRepository provideUserRepository(UserServer userServer)
	{
		return new UserRepositoryImpl(userServer);
	}
}