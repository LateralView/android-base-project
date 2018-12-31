package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothState
        .BLUETOOTH_OFF;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothState
        .BLUETOOTH_ON;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@IntDef({BLUETOOTH_ON, BLUETOOTH_OFF})
public @interface BluetoothState {
    int BLUETOOTH_ON = 0;
    int BLUETOOTH_OFF = 1;
}
