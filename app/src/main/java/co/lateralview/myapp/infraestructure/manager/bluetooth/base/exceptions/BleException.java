package co.lateralview.myapp.infraestructure.manager.bluetooth.base.exceptions;

public class BleException extends Exception {

    public BleException(String message) {
        super(message);
    }

    public static class BluetoothDeviceUnpairedException extends BleException {
        public BluetoothDeviceUnpairedException() {
            super("The requested device got unpaired.");
        }
    }

    public static class BluetoothNotEnabledException extends BleException {
        public BluetoothNotEnabledException() {
            super("Bluetooth is Off.");
        }
    }

    public static class BluetoothAccessNotGrantedException extends BleException {
        public BluetoothAccessNotGrantedException() {
            super("Bluetooth access not granted.");
        }
    }

    public static class BluetoothNotSupportedException extends BleException {
        public BluetoothNotSupportedException() {
            super("The current device doesn't support Bluetooth.");
        }
    }

    public static class PairingToBTDeviceException extends BleException {
        public PairingToBTDeviceException() {
            super("Couldn't pair to the selected Bluetooth device. Check if it is in pairing "
                    + "mode and in the appropriate range.");
        }
    }
}
