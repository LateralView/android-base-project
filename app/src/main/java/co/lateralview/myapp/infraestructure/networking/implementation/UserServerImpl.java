package co.lateralview.myapp.infraestructure.networking.implementation;

import javax.inject.Inject;

import co.lateralview.myapp.domain.model.User;
import co.lateralview.myapp.infraestructure.networking.RetrofitManager;
import co.lateralview.myapp.infraestructure.networking.interfaces.UserServer;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class UserServerImpl extends BaseServerImpl implements UserServer
{
    protected static final String TAG = UserServerImpl.class.getSimpleName();

    private IUserServer mIUserServer;

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

    @Inject
    UserServerImpl(RetrofitManager retrofitManager)
    {
        super(retrofitManager);
        mIUserServer = mRetrofitManager.getRetrofit().create(IUserServer.class);
    }

    @Override
    public Single<User> login(String email, String password)
    {
        return mIUserServer.login(email, password);
    }

    interface IUserServer
    {
        @FormUrlEncoded
        @Headers({"Accept: */*"})
        @POST("users/authenticate")
        Single<User> login(@Field(value = "email") String email,
                @Field(value = "password") String password);
    }
}
