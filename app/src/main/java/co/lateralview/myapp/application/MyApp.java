package co.lateralview.myapp.application;

import android.app.Application;


public class MyApp extends Application
{
	public static final String TAG = MyApp.class.getSimpleName();

	private static String sCurrentScreenTag;

	private static AppComponent mAppComponent;

	public static AppComponent getAppComponent()
	{
		return mAppComponent;
	}

	public static void setCurrentScreenTag(String tag)
	{
		sCurrentScreenTag = tag;
	}

	public static boolean isApplicationRunning()
	{
		return sCurrentScreenTag != null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		initializeServices();
	}

	private void initializeServices()
	{
		mAppComponent = DaggerAppComponent.builder()
				// list of modules that are part of this component need to be created here too
				.appModule(new AppModule(this))
				.build();
	}
}

