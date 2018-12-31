package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import android.Manifest;

public class BluetoothConstants {

    public static final int GET_FIRST_8_BITS = 0xFF;

    public final static String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
}
