package co.lateralview.myapp.infraestructure.manager.bluetooth.myBtDevices.devices.myDevice;


import static co.lateralview.myapp.infraestructure.manager.bluetooth.myBtDevices
        .MyBluetoothDevicesConstants.UUID_MY_OWN_DEVICE_SERVICE;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.myBtDevices
        .MyBluetoothDevicesConstants.UUID_WRITE_READ_ID;

import android.content.Context;

import co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothDeviceManager;
import co.lateralview.myapp.infraestructure.manager.bluetooth.base.BluetoothManager;
import io.reactivex.Completable;
import io.reactivex.Single;

public class MyOwnBluetoothDeviceManagerImpl extends BluetoothDeviceManager implements
        MyOwnBluetoothDeviceManager {
    private static final String TAG = "MyOwnBluetoothDeviceManagerImpl";

    public MyOwnBluetoothDeviceManagerImpl(BluetoothManager bluetoothManager, Context context,
            String deviceMacAddress) {
        super(bluetoothManager, context, deviceMacAddress);
    }

    @Override
    public Completable writeInt(int value) {
        // Create the packet according your needs
        byte[] packet = new byte[90];
        packet[0] = (byte) value;

        return write(UUID_MY_OWN_DEVICE_SERVICE, UUID_WRITE_READ_ID, packet, false);
    }

    @Override
    public Single<String> readValue(int id) {
        // Create the packet according your needs

        return read(UUID_MY_OWN_DEVICE_SERVICE, UUID_WRITE_READ_ID, true)
                // TODO Get Value from BluetoothGattCharacteristic
                .map(bluetoothGattCharacteristic -> bluetoothGattCharacteristic.getStringValue(0));
    }
}
