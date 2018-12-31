package co.lateralview.myapp.infraestructure.manager.bluetooth.myBtDevices.devices.myDevice;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface MyOwnBluetoothDeviceManager {
    // Operations
    Completable writeInt(int value);

    Single<String> readValue(int id);
}
