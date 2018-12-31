package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import android.Manifest;

public final class BluetoothConstants {

    private BluetoothConstants() {

    }

    public static final int GET_FIRST_8_BITS = 0xFF;

    public static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
}
