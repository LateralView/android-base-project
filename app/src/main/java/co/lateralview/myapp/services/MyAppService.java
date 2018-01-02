package co.lateralview.myapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.ui.activities.base.DaggerBaseComponent;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import co.lateralview.myapp.ui.util.SystemUtils;
import io.reactivex.Observable;

public class MyAppService extends Service
{
    public static final String TAG = "MyAppService";

    @Inject
    SessionRepository mSessionRepository;

    public static void startService(Context context)
    {
        if (!SystemUtils.isServiceRunning(context, MyAppService.class))
        {
            context.startService(new Intent(context, MyAppService.class));
        }
    }

    @Override
    public void onCreate()
    {
        Log.i(TAG, "Initiate");
        injectDependencies(this);

        Observable.interval(
                2, TimeUnit.MINUTES)
                .compose(RxSchedulersUtils.applyObservableSchedulers())
                .subscribe(__ -> {/*do something*/},
                        error -> Log.e(TAG, "Error executing MyAppService", error));
    }

    @Override
    public void onDestroy()
    {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY; // To keep alive this Service until app is closed or killed
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void injectDependencies(Context context)
    {
        DaggerBaseComponent.builder()
                .appComponent(MyApp.getAppComponent())
                .build()
                .inject(this);
    }
}
