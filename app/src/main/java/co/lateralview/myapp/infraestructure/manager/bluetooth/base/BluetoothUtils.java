package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.util.Log;

public final class BluetoothUtils {
    public static final String TAG = "BluetoothUtils";
    private static final String LOG_SEPARATOR = "===========================================";
    private static final String LOG_NEW_LINE = "\n";
    private static final String LOG_ITEM_SEPARATOR = "----------";

    private BluetoothUtils() {

    }

    public static void dumpBluetoothDevice(BluetoothDevice bluetoothDevice) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(LOG_SEPARATOR);
        stringBuilder.append("Dumping Bluetooth Device Data");
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append("Mac Address: " + bluetoothDevice.getAddress());
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append("Name: " + bluetoothDevice.getName());
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_ITEM_SEPARATOR);
        stringBuilder.append(LOG_NEW_LINE);

        stringBuilder.append("List Of Services: ");
        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_NEW_LINE);

        ParcelUuid[] uuids = bluetoothDevice.getUuids();

        if (uuids != null) {
            for (ParcelUuid parcelUuid : bluetoothDevice.getUuids()) {
                stringBuilder.append("Service: " + parcelUuid.getUuid().toString());
                stringBuilder.append(LOG_NEW_LINE);
            }
        }

        stringBuilder.append(LOG_NEW_LINE);
        stringBuilder.append(LOG_SEPARATOR);
        stringBuilder.append(LOG_NEW_LINE);

        Log.d(TAG, stringBuilder.toString());
    }
}
