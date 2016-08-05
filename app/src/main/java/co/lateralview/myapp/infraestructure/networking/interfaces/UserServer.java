package co.lateralview.myapp.infraestructure.networking.interfaces;

import co.lateralview.myapp.ui.common.MyAppCallback;

public interface UserServer extends BaseServer
{
	void signIn(String userEmail, String userPassword, MyAppCallback callback, String tag);
}
