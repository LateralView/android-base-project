package co.lateralview.myapp.ui.activities.main;


import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.ui.activities.base.Base;
import co.lateralview.myapp.ui.util.di.ActivityScoped;
import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class,
        modules = {Base.BaseViewModule.class, Main.ViewModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
