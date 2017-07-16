package co.lateralview.myapp.ui.activities.base;


import android.app.Activity;

import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.ui.util.di.ActivityScoped;

@ActivityScoped
public class BasePresenter
{
    @Inject
    SessionRepository mSessionRepository;
    @Inject
    Activity mActivity;
    @Inject
    Base.View mBaseViw;
    @Inject
    TaskManager mTaskManager; //Help us to retry failed tasks

    public void cancelPendingTasks(String tag)
    {
        mTaskManager.removeTasks(tag);
    }
}
