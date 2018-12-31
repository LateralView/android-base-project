package co.lateralview.myapp.ui.util;


import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class RxSchedulersUtils {

    private RxSchedulersUtils() {

    }

    public static <T> ObservableTransformer<T, T> applyObservableSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> MaybeTransformer<T, T> applyMaybeSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySingleSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer applyCompletableSchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // Useful methods to quickly dispose subscriptions

    public static void safeDispose(CompositeDisposable compositeDisposable) {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public static void safeDispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static void safeDispose(List<Disposable> disposableList) {
        if (disposableList != null && !disposableList.isEmpty()) {
            for (Disposable disposable : disposableList) {
                disposable.dispose();
            }
        }
    }

    // Observe observable stream in the UI Thread

    public static <T> ObservableTransformer<T, T> observeObservableOnUi() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> MaybeTransformer<T, T> observeMaybeOnUi() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> observeSingleOnUi() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer observeCompletableOnUi() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread());
    }

    // Observe observable stream in the IO Thread in case we don't need to update the UI

    public static <T> ObservableTransformer<T, T> observeObservableOnIO() {
        return upstream -> upstream.observeOn(Schedulers.io());
    }

    public static <T> MaybeTransformer<T, T> observeMaybeOnIO() {
        return upstream -> upstream.observeOn(Schedulers.io());
    }

    public static <T> SingleTransformer<T, T> observeSingleOnIO() {
        return upstream -> upstream.observeOn(Schedulers.io());
    }

    public static CompletableTransformer observeCompletableOnIO() {
        return upstream -> upstream.observeOn(Schedulers.io());
    }

    // Subscribe observable on IO Thread to not block UI Thread

    public static <T> ObservableTransformer<T, T> subscribeObservableOnIO() {
        return upstream -> upstream.subscribeOn(Schedulers.io());
    }

    public static <T> MaybeTransformer<T, T> subscribeMaybeOnIO() {
        return upstream -> upstream.subscribeOn(Schedulers.io());
    }

    public static <T> SingleTransformer<T, T> subscribeSingleOnIO() {
        return upstream -> upstream.subscribeOn(Schedulers.io());
    }

    public static CompletableTransformer subscribeCompletableOnIO() {
        return upstream -> upstream.subscribeOn(Schedulers.io());
    }

    // Subscribe observable on Single Thread to not block UI Thread and run them sequentially


    public static <T> SingleTransformer<T, T> subscribeSingleOnSingle() {
        return upstream -> upstream.subscribeOn(Schedulers.single());
    }

    public static CompletableTransformer subscribeCompletableOnSingle() {
        return upstream -> upstream.subscribeOn(Schedulers.single());
    }
}
