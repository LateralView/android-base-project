package co.lateralview.myapp.ui.activities.base;


import co.lateralview.myapp.application.AppComponent;
import co.lateralview.myapp.ui.util.di.ActivityScoped;
import dagger.Component;

@ActivityScoped
@Component(dependencies = AppComponent.class,
        modules = {Base.BaseViewModule.class})
public interface BaseComponent
{

}
