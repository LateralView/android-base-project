package co.lateralview.myapp.infraestructure.manager.bluetooth.myBtDevices.exceptions;

public class MyOwnBluetoothException extends Exception {

    public MyOwnBluetoothException(String message) {
        super(message);
    }

    public static class DeviceNotSupportedException extends MyOwnBluetoothException {
        public DeviceNotSupportedException() {
            super("The selected device is not in the whitelist of supported devices.");
        }
    }
}
