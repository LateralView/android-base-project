package co.lateralview.myapp.infraestructure.networking;

import android.app.Application;

import net.lateralview.simplerestclienthandler.RestClientManager;

import javax.inject.Singleton;

import co.lateralview.myapp.infraestructure.networking.implementation.BaseServerImpl;
import co.lateralview.myapp.infraestructure.networking.implementation.UserServerImpl;
import co.lateralview.myapp.infraestructure.networking.interfaces.BaseServer;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import dagger.Module;
import dagger.Provides;

@Module
public class NetModule
{
	@Provides
	@Singleton
	public RestClientManager provideRestClient(Application application)
	{
		RestClientManager.initialize(application);

		return RestClientManager.getInstance();
	}

	@Provides
	@Singleton
	public BaseServer provideBaseServer(RestClientManager restClientManager)
	{
		return new BaseServerImpl(restClientManager);
	}

	@Provides
	@Singleton
	public UserServer provideUserServer(RestClientManager restClientManager)
	{
		return new UserServerImpl(restClientManager);
	}

}