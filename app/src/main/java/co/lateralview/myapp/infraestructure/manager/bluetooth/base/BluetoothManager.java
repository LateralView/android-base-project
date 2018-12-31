package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * BLE Generic Operations:
 * - Turn On / Off
 * - Discover Devices
 * - Pairing / Unpairing
 *
 * Once you got your device paired you must use a BluetoothDeviceManager Instance to perform I/O
 * events.
 */
public interface BluetoothManager {
    // Status
    Single<Integer> getBluetoothStatus();

    Observable<Integer> observeBluetoothStatus();

    // Turn On / Off
    Completable turnOn();

    Completable turnOff();

    // Devices
    Single<List<BluetoothDevice>> getPairedDevices();

    // Discovery
    Completable startDiscovery();

    Completable stopDiscovery();

    Observable<BluetoothDevice> observeBluetoothDiscovery();

    // Actions
    Completable pairDevice(BluetoothDevice bluetoothDevice);

    void unpair(String deviceMacAddress);

    Completable discoverAndPairDevice(String deviceMacAddress);

    // Stop / Clear states
    void stop();
}
