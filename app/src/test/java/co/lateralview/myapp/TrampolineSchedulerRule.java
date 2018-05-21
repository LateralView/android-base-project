package co.lateralview.myapp;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class TrampolineSchedulerRule implements TestRule {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
                RxJavaPlugins.setIoSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxAndroidPlugins.reset();
                    RxJavaPlugins.reset();
                }
            }
        };
    }
}
