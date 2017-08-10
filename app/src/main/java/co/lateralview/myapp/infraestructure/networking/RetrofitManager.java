package co.lateralview.myapp.infraestructure.networking;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import co.lateralview.myapp.domain.repository.interfaces.SessionRepository;
import co.lateralview.myapp.infraestructure.manager.InternetManager;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager
{
    public static final String TAG = "RetrofitManager";

    public static final String CACHE_CONTROL = "Cache-Control";

    private Application mApplication;
    private Gson mGson;
    private Retrofit mCustomRetrofit, mRetrofit;
    private Cache mCache;
    private SessionRepository mSessionRepository;
    private InternetManager mInternetManager;

    public RetrofitManager(Application application, Gson gson, SessionRepository sessionRepository,
            InternetManager internetManager)
    {
        mApplication = application;
        mGson = gson;
        mSessionRepository = sessionRepository;
        mInternetManager = internetManager;

        Stetho.initializeWithDefaults(mApplication);
    }

    /**
     * Returns a Custom Retrofit instance
     */
    public Retrofit getCustomRetrofit()
    {
        if (mCustomRetrofit == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideHeaderInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache());

            mCustomRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return mCustomRetrofit;
    }

    /**
     * Returns a Clean Retrofit instance
     */
    public Retrofit getRetrofit()
    {
        if (mRetrofit == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache());

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return mRetrofit;
    }

    private Cache provideCache()
    {
        if (mCache == null)
        {
            try
            {
                mCache = new Cache(new File(mApplication.getCacheDir(), "http-cache"),
                        10 * 1024 * 1024); // 10 MB
            } catch (Exception e)
            {
                Log.e(TAG, "Could not create Cache!");
            }
        }

        return mCache;
    }

    private Interceptor provideCacheInterceptor()
    {
        return chain ->
        {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (mInternetManager.isOnline())
            {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else
            {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();

        };
    }

    private Interceptor provideOfflineCacheInterceptor()
    {
        return chain ->
        {
            Request request = chain.request();

            if (!mInternetManager.isOnline())
            {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    public void clean()
    {
        mCustomRetrofit = null;
        mRetrofit = null;

        if (mCache != null)
        {
            try
            {
                mCache.evictAll();
            } catch (IOException e)
            {
                Log.e(TAG, "Error cleaning http cache");
            }
        }

        mCache = null;
    }

    private Interceptor provideHeaderInterceptor()
    {
        return chain ->
        {
            Request request = chain.request();

            String accessToken = mSessionRepository.getAccessToken();

            Request.Builder requestBuilder = request.newBuilder()
                    .header(RestConstants.HEADER_AUTH,
                            accessToken != null ? accessToken : "");

            request = requestBuilder.build();

            return chain.proceed(request);
        };
    }
}
