package co.lateralview.myapp.infraestructure.networking;

import net.lateralview.simplerestclienthandler.base.RequestCallbacks;

import co.lateralview.myapp.ui.common.MyAppCallback;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class MyAppServerCallback extends RequestCallbacks
{
	private MyAppCallback mCallback;

	public MyAppServerCallback(MyAppCallback callback)
	{

	}

	@Override
	protected void onRequestFinish()
	{
		mCallback.onFinish();
	}

	@Override
	protected void onRequestStart()
	{
		mCallback.onStart();
	}

	@Override
	protected void onRequestSuccess(Object o)
	{
		mCallback.onSuccess(o);
	}

	@Override
	protected void onRequestError(Object o)
	{
		mCallback.onError(o);
	}
}
