package co.lateralview.myapp.infraestructure.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

@RunWith(RobolectricTestRunner.class)
public class InternetManagerTest {

    private ShadowConnectivityManager mShadowConnectivityManager;
    private InternetManager mInternetManager;

    @Before
    public void setUpTest() {
        final Context context = RuntimeEnvironment.application.getApplicationContext();
        mInternetManager = new InternetManager(context);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        mShadowConnectivityManager = shadowOf(connectivityManager);
    }

    @Test
    public void itShouldReturnIsOnlineIfConnectedToNetwork() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI, 0, true, true);
        mShadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        assertTrue(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnIsOnlineIfIsConnectingToNetwork() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTING,
                ConnectivityManager.TYPE_WIFI, 0, true, true);
        mShadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        assertTrue(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnIsNotOnline() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_WIFI, 0, true, false);
        mShadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        assertFalse(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnOnWifi() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI, 0, true, true);
        mShadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        assertTrue(mInternetManager.onWifi());
    }

    @Test
    public void itShouldReturnNotOnWifi() {
        NetworkInfo networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_MOBILE, 0, true, true);
        mShadowConnectivityManager.setActiveNetworkInfo(networkInfo);

        assertFalse(mInternetManager.onWifi());
    }
}
