package co.lateralview.myapp.ui.activities.base;


import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.services.MyAppService;
import co.lateralview.myapp.ui.activities.splash.SplashActivity;
import co.lateralview.myapp.ui.util.di.ActivityScoped;
import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class,
        modules = {Base.BaseViewModule.class})
public interface BaseComponent
{
    void inject(BaseActivity activity);

    void inject(SplashActivity activity);

    void inject(MyAppService service);
}
