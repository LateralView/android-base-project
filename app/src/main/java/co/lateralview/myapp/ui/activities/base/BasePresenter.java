package co.lateralview.myapp.ui.activities.base;


import android.app.Application;
import android.util.Log;

import javax.inject.Inject;

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import co.lateralview.myapp.infraestructure.manager.interfaces.TaskManager;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.infraestructure.networking.RestConstants;
import co.lateralview.myapp.infraestructure.networking.ServerException;
import co.lateralview.myapp.ui.util.di.ActivityScoped;

@ActivityScoped
public class BasePresenter implements Base.Presenter
{
    private static final String TAG = "BasePresenter";

    @Inject
    protected SessionRepository mSessionRepository;
    @Inject
    InternetManager mInternetManager;
    @Inject
    Application mApplication;
    @Inject
    Base.View mBaseView;
    @Inject
    TaskManager mTaskManager; //Help us to retry failed tasks

    public BasePresenter() {
    }

    public BasePresenter(final SessionRepository sessionRepository) {
        mSessionRepository = sessionRepository;
    }

    public void cancelPendingTasks(String tag)
    {
        mTaskManager.removeTasks(tag);
    }

    protected boolean handlerError(Throwable throwable)
    {
        try
        {
            if (!mInternetManager.isOnline())
            {
                noInternetError();
                return true;
            }

            if (throwable instanceof ServerException)
            {
                ServerException serverException = (ServerException) throwable;

                if (serverException.getKind() == ServerException.Kind.HTTP)
                {
                    MyAppServerError serverError = serverException.getServerError();
                    Integer errorCode = serverError.getErrorCode();

                    switch (RestConstants.Subcode.fromInt(errorCode))
                    {
                        case INVALID_TOKEN:
                        {
                            mBaseView.logout();
                            return true;
                        }
                    }
                } else
                {
                    unexpectedErrorHappened();
                    return true;
                }
            }
        } catch (Exception e)
        {
            unexpectedErrorHappened();
            return true;
        }

        return false;
    }

    protected void noInternetError()
    {
        mBaseView.showInternetRequiredError();
    }

    protected void unexpectedErrorHappened()
    {
        Log.e(TAG, "Unexpected Error Happened");
        mBaseView.showUnexpectedErrorHappened();
    }
}
