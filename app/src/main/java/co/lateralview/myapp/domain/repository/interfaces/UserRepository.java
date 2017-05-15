package co.lateralview.myapp.domain.repository.interfaces;

import co.lateralview.myapp.domain.model.User;
import io.reactivex.Observable;

public interface UserRepository
{
	Observable<User> login(String userEmail, String userPassword);
}
