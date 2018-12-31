package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothDeviceState
        .CONNECTED;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothDeviceState
        .DISCONNECTED;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@IntDef({CONNECTED, DISCONNECTED})
public @interface BluetoothDeviceState {
    int CONNECTED = 0;
    int DISCONNECTED = 1;
}
