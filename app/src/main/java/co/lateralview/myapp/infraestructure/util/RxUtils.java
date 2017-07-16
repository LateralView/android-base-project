package co.lateralview.myapp.infraestructure.util;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Julian Falcionelli on 12/1/2017.
 */
public class RxUtils
{
    public static Observable newObservableFromIoToMainThread(Callable callable)
    {
        return Observable.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single newSingleFromIoToMainThread(Callable callable)
    {
        return Single.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
