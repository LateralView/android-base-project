package co.lateralview.myapp.domain.repository.interfaces;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.infraestructure.networking.MyAppServerError;
import co.lateralview.myapp.ui.common.MyAppCallback;

public interface UserRepository
{
	void login(String userEmail, String userPassword, MyAppCallback<User, MyAppServerError> callback, String tag);
}
