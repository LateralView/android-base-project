package co.lateralview.myapp.infraestructure.networking.implementation;

import net.lateralview.simplerestclienthandler.RestClientManager;

import co.lateralview.myapp.infraestructure.networking.interfaces.BaseServer;

public class BaseServerImpl implements BaseServer
{
	protected static final String TAG = BaseServerImpl.class.getSimpleName();

	protected RestClientManager mRestClientManager;

	public BaseServerImpl(RestClientManager restClientManager)
	{
		mRestClientManager = restClientManager;
	}

	@Override
	public void cancelRequest(String tag)
	{
		mRestClientManager.cancelPendingRequests(tag);
	}

}
