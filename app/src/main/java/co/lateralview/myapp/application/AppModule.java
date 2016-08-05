package co.lateralview.myapp.application;

import android.app.Application;

import javax.inject.Singleton;

import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.SystemManager;
import co.lateralview.myapp.infraestructure.manager.implementation.ImageManagerImpl;
import co.lateralview.myapp.infraestructure.manager.implementation.ParserManagerImpl;
import co.lateralview.myapp.infraestructure.manager.implementation.SharedPreferencesManagerImpl;
import co.lateralview.myapp.infraestructure.manager.implementation.TaskManagerImpl;
import co.lateralview.myapp.infraestructure.manager.interfaces.ImageManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.ParserManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.SharedPreferencesManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import dagger.Module;
import dagger.Provides;

/**
 * Created by julianfalcionelli on 5/9/16.
 */

@Module
public class AppModule
{
	protected Application mApplication;

	public AppModule(Application application)
	{
		mApplication = application;
	}

	@Provides
	@Singleton
	public Application providesApplication()
	{
		return mApplication;
	}

	@Provides
	@Singleton
	public ImageManager providesImageManager(Application application)
	{
		return new ImageManagerImpl(application);
	}

	@Provides
	@Singleton
	public ParserManager providesParserManager()
	{
		return new ParserManagerImpl();
	}

	@Provides
	@Singleton
	public SharedPreferencesManager providesSharedPreferencesManager(Application application, ParserManager parserManager)
	{
		return new SharedPreferencesManagerImpl(application, parserManager);
	}

	@Provides
	@Singleton
	public SystemManager providesSystemManager(Application application)
	{
		return new SystemManager(application);
	}

	@Provides
	@Singleton
	public InternetManager providesInternetManager(Application application)
	{
		return new InternetManager(application);
	}

	@Provides
	@Singleton
	public TaskManager providesTaskManager()
	{
		return new TaskManagerImpl();
	}
}
