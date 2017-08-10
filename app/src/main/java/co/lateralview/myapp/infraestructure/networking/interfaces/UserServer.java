package co.lateralview.myapp.infraestructure.networking.interfaces;

import co.lateralview.myapp.domain.model.User;
import io.reactivex.Single;

public interface UserServer
{
    Single<User> login(String userEmail, String userPassword);
}
