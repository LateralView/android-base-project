package co.lateralview.myapp.infraestructure.networking.interfaces;

import co.lateralview.myapp.domain.model.User;

public interface UserServer
{
    User signIn(String userEmail, String userPassword);
}
