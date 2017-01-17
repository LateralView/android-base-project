package co.lateralview.myapp.infraestructure.networking.implementation;

import net.lateralview.simplerestclienthandler.RestClientManager;

public class BaseServerImpl
{
	protected static final String TAG = BaseServerImpl.class.getSimpleName();

	protected RestClientManager mRestClientManager;

	public BaseServerImpl(RestClientManager restClientManager)
	{
		mRestClientManager = restClientManager;
	}
}
