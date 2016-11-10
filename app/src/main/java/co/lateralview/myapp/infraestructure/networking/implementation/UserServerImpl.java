package co.lateralview.myapp.infraestructure.networking.implementation;

import android.os.Bundle;

import com.android.volley.Request;

import net.lateralview.simplerestclienthandler.RestClientManager;
import net.lateralview.simplerestclienthandler.base.RequestHandler;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.infraestructure.networking.MyAppServerCallback;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.infraestructure.networking.RestConstants;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import co.lateralview.myapp.ui.common.MyAppCallback;

public class UserServerImpl extends BaseServerImpl implements UserServer
{
	protected static final String TAG = UserServerImpl.class.getSimpleName();

	public UserServerImpl(RestClientManager restClientManager)
	{
		super(restClientManager);
	}

	@Override
	public void signIn(String userEmail, String userPassword, MyAppCallback callback, String tag)
	{
		Bundle bundle = new Bundle();

		bundle.putString(Parameters.EMAIL, userEmail);
		bundle.putString(Parameters.PASSWORD, userPassword);

		mRestClientManager.makeJsonRequest(Request.Method.POST, Url.SIGN_IN.getUrl(), new RequestHandler(new MyAppServerCallback<User, MyAppServerError>(callback), bundle));
	}

	public enum Url
	{
		SIGN_IN(RestConstants.getUrl("users/authenticate"));

		private String mUrl;

		Url(String url)
		{
			mUrl = url;
		}

		public String getUrl()
		{
			return mUrl;
		}
	}

	public enum Subcode
	{
		INVALID_CREDENTIALS(200001);

		private int mSubcode;

		Subcode(int subcode)
		{
			mSubcode = subcode;
		}

		public static Subcode fromInt(int code)
		{
			for (Subcode subcode : Subcode.values())
			{
				if (subcode.getSubcode() == code)
				{
					return subcode;
				}
			}
			return null;
		}

		public int getSubcode()
		{
			return mSubcode;
		}
	}

	public static class Parameters
	{
		public static final String EMAIL = "email";
		public static final String PASSWORD = "password";
	}

}
