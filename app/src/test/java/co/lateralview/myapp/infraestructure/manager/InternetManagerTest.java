package co.lateralview.myapp.infraestructure.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class InternetManagerTest
{
    private ShadowConnectivityManager mShadowConnectivityManager;
    private ShadowNetworkInfo mShadowNetworkInfo;

    private InternetManager mInternetManager;

    @Before
    public void setUpTest() {
        final Context context = RuntimeEnvironment.application.getApplicationContext();
        mInternetManager = new InternetManager(context);

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mShadowConnectivityManager = shadowOf(connectivityManager);
        mShadowNetworkInfo = shadowOf(mShadowConnectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void itShouldReturnIsOnlineIfConnectedToNetwork() {
        mShadowNetworkInfo.setConnectionStatus(State.CONNECTED);
        assertTrue(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnIsOnlineIfIsConnectingToNetwork() {
        mShadowNetworkInfo.setConnectionStatus(State.CONNECTING);
        assertTrue(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnIsNotOnline() {
        mShadowNetworkInfo.setConnectionStatus(State.DISCONNECTED);
        assertFalse(mInternetManager.isOnline());
    }

    @Test
    public void itShouldReturnOnWifi() {
        mShadowConnectivityManager.setActiveNetworkInfo(mShadowConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI));
        assertTrue(mInternetManager.onWifi());
    }

    @Test
    public void itShouldReturnNotOnWifi() {
        mShadowConnectivityManager.setActiveNetworkInfo(mShadowConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE));
        assertFalse(mInternetManager.onWifi());
    }
}
