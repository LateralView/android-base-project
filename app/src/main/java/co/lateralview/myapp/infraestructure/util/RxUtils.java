package co.lateralview.myapp.infraestructure.util;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Julian Falcionelli on 12/1/2017.
 */
public class RxUtils
{
	public static Observable<String> newObservableFromIoToMainThread(Callable callable)
	{
		return Observable.fromCallable(callable)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
