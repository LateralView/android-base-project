package co.lateralview.myapp.infraestructure.networking;

import net.lateralview.simplerestclienthandler.base.ICallbackTypes;
import net.lateralview.simplerestclienthandler.base.RequestCallbacks;
import net.lateralview.simplerestclienthandler.helper.ReflectionHelper;

import java.lang.reflect.Type;

import co.lateralview.myapp.ui.common.MyAppCallback;

/**
 * Created by julianfalcionelli on 7/28/16.
 */
public class MyAppServerCallback extends RequestCallbacks implements ICallbackTypes
{
	private MyAppCallback mCallback;

	public MyAppServerCallback(MyAppCallback callback)
	{
		mCallback = callback;
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

	@Override
	public Type getResponseType()
	{
		return ReflectionHelper.getTypeArgument(mCallback, 0);
	}

	@Override
	public Type getErrorType()
	{
		return ReflectionHelper.getTypeArgument(mCallback, 1);
	}
}