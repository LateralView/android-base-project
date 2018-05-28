package co.lateralview.myapp.domain.repository.interfaces;

import co.lateralview.myapp.domain.model.User;
import io.reactivex.Single;

public interface UserRepository {
    Single<User> login(String userEmail, String userPassword);
}
