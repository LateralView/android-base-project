package co.lateralview.myapp.infraestructure.networking.implementation;

import co.lateralview.myapp.infraestructure.networking.RetrofitManager;

public class BaseServerImpl
{
    protected static final String TAG = BaseServerImpl.class.getSimpleName();

    protected RetrofitManager mRetrofitManager;

    public BaseServerImpl(RetrofitManager retrofitManager)
    {
        mRetrofitManager = retrofitManager;
    }
}
