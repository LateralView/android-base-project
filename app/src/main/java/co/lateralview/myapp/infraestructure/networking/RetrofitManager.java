package co.lateralview.myapp.infraestructure.networking;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;

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

public class RetrofitManager {
    public static final String TAG = "RetrofitManager";

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    private Application mApplication;
    private Gson mGson;
    private Retrofit mCustomRetrofit;
    private Retrofit mCustomCachedRetrofit;
    private Retrofit mRetrofit;
    private OkHttpClient mCustomOkHttpClient;
    private OkHttpClient mCustomCachedOkHttpClient;
    private OkHttpClient mDefaultOkHttpClient;
    private Cache mCache;
    private SessionRepository mSessionRepository;
    private InternetManager mInternetManager;

    public RetrofitManager(Application application, Gson gson, SessionRepository sessionRepository,
            InternetManager internetManager) {
        mApplication = application;
        mGson = gson;
        mSessionRepository = sessionRepository;
        mInternetManager = internetManager;

        Stetho.initializeWithDefaults(mApplication);
    }

    /**
     * Returns a Custom Retrofit instance.
     */
    public Retrofit getCustomRetrofit() {
        if (mCustomRetrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideHeaderInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addInterceptor(new ChuckInterceptor(mApplication))
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES);

            mCustomOkHttpClient = httpClient.build();

            mCustomRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(mCustomOkHttpClient)
                    .build();
        }

        return mCustomRetrofit;
    }

    /**
     * Returns a Custom Retrofit instance which only checks on Cache.
     */
    public Retrofit getCustomCachedRetrofit() {
        if (mCustomCachedRetrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideForcedOfflineCacheInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor())
                    .cache(provideCache())
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES);

            mCustomCachedOkHttpClient = httpClient.build();

            mCustomCachedRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(mCustomCachedOkHttpClient)
                    .build();
        }

        return mCustomCachedRetrofit;
    }


    /**
     * Returns a Clean Retrofit instance.
     */
    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache());

            mDefaultOkHttpClient = httpClient.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(RestConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(mGson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(mDefaultOkHttpClient)
                    .build();
        }

        return mRetrofit;
    }

    private Cache provideCache() {
        if (mCache == null) {
            try {
                mCache = new Cache(new File(mApplication.getCacheDir(), "http-cache"),
                        10 * 1024 * 1024); // 10 MB
            } catch (Exception e) {
                Log.e(TAG, "Could not create Cache!");
            }
        }

        return mCache;
    }

    private Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (mInternetManager.isOnline()) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            if (!mInternetManager.isOnline()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    private Interceptor provideForcedOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();

            request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();

            return chain.proceed(request);
        };
    }

    public void clean() {

        if (mCustomOkHttpClient != null) {
            // Cancel Pending Request
            mCustomOkHttpClient.dispatcher().cancelAll();
        }

        if (mDefaultOkHttpClient != null) {
            // Cancel Pending Request
            mDefaultOkHttpClient.dispatcher().cancelAll();
        }

        mCustomRetrofit = null;
        mRetrofit = null;

        if (mCache != null) {
            try {
                mCache.evictAll();
            } catch (IOException e) {
                Log.e(TAG, "Error cleaning http cache");
            }
        }

        mCache = null;
    }

    private Interceptor provideHeaderInterceptor() {
        return chain -> {
            Request request = chain.request();

            String accessToken = mSessionRepository.getAccessToken().blockingGet();

            Request.Builder requestBuilder = request.newBuilder()
                    .header(RestConstants.HEADER_AUTH,
                            accessToken != null ? accessToken : "");

            request = requestBuilder.build();

            return chain.proceed(request);
        };
    }
}
