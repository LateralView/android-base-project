package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.lateralview.myapp.infraestructure.manager.bluetooth.base.exceptions.BleException;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class BluetoothManagerImpl implements BluetoothManager {
    private static final String TAG = "BluetoothManagerImpl";

    // Forever PS
    private PublishSubject<Integer> mBluetoothStatusPS = PublishSubject.create();
    private final BroadcastReceiver mBluetoothStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBluetoothStatusPS.onNext(BluetoothState.BLUETOOTH_OFF);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mBluetoothStatusPS.onNext(BluetoothState.BLUETOOTH_ON);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };
    private PublishSubject<BluetoothDevice> mBluetoothDeviceDiscoveryPS =
            PublishSubject.create();
    private final BroadcastReceiver mBTDiscoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBluetoothDeviceDiscoveryPS.onNext(device);
            }
        }
    };
    // One Time PS
    private BehaviorSubject<BluetoothDevice> mBluetoothDevicePairingPS;
    private final BroadcastReceiver mBTPairingReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {

                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);

                if (state == BluetoothDevice.BOND_BONDING) {
                    //bonding process is still working
                    //essentially this means that the Confirmation Dialog is still visible
                    return; // Do Nothing
                }

                if (state == BluetoothDevice.BOND_BONDED) {
                    //bonding process was successful
                    //also means that the user pressed OK on the Dialog

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE);

                    mBluetoothDevicePairingPS.onNext(device);

                    return;
                }

                // Paired device not found means error/cancel during pairing process
                mBluetoothDevicePairingPS.onError(new BleException.PairingToBTDeviceException());
            }
        }
    };

    // ======== Start Bluetooth Status Related ========

    private Context mContext;
    private boolean mDiscoveringReceiverRegistered = false;
    private boolean mPairingReceiverRegistered = false;

    public BluetoothManagerImpl(Context context) {
        mContext = context;
        registerToBluetoothChanges();
    }

    @Override
    public Single<Integer> getBluetoothStatus() {
        return Single.fromCallable(() -> {
            BluetoothAdapter mBluetoothAdapter = getBluetoothAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                throw new BleException.BluetoothNotSupportedException();
            }

            return mBluetoothAdapter.isEnabled() ? BluetoothState.BLUETOOTH_ON
                    : BluetoothState.BLUETOOTH_OFF;
        });
    }

    @Override
    public Observable<Integer> observeBluetoothStatus() {
        return mBluetoothStatusPS;
    }

    @Override
    public Completable turnOn() {
        return Single.fromCallable(() -> {
                    BluetoothAdapter mBluetoothAdapter = getBluetoothAdapter();
                    if (mBluetoothAdapter == null) {
                        // Device does not support Bluetooth
                        throw new BleException.BluetoothNotSupportedException();
                    }

                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable(); // Works only with ADMIN BLUETOOTH permission.
                        return true; // Wait for connection, turn on can take a while.
                    }

                    return false;
                }
        ).flatMapCompletable(waitForConnection -> {
            if (waitForConnection) {
                return mBluetoothStatusPS
                        .filter(status -> status == BluetoothState.BLUETOOTH_ON)
                        .firstOrError()
                        .ignoreElement();
            } else {
                return Completable.complete();
            }
        });
    }

    // ======== End Bluetooth Status Related ========

    @Override
    public Completable turnOff() {
        return Completable.fromAction(() -> {
                    BluetoothAdapter mBluetoothAdapter = getBluetoothAdapter();
                    if (mBluetoothAdapter == null) {
                        // Device does not support Bluetooth
                        throw new BleException.BluetoothNotSupportedException();
                    }

                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }
        );
    }

    // ======== Start Pairing Related ========

    @Override
    public Single<List<BluetoothDevice>> getPairedDevices() {
        return Single.fromCallable(() -> new ArrayList<>(getBluetoothPairedDevices()));
    }

    @Override
    public Completable pairDevice(BluetoothDevice bluetoothDevice) {
        // Check if device is already paired
        if (getPairedDevice(bluetoothDevice.getAddress()) != null) {
            return Completable.complete();
        }

        mBluetoothDevicePairingPS = BehaviorSubject.create();

        return mBluetoothDevicePairingPS
                .firstOrError()
                // Re-Check address just for case.
                .filter(pairedBluetoothDevice -> pairedBluetoothDevice.getAddress()
                        .equals(bluetoothDevice.getAddress()))
                .flatMapCompletable(__ -> Completable.complete())
                .doOnSubscribe(__ -> {
                    registerToBTPairingReceiver();
                    bluetoothDevice.createBond();
                });
    }

    @Override
    public Completable startDiscovery() {
        return Completable.fromAction(() -> {
            if (!getBluetoothAdapter().isDiscovering()) {
                getBluetoothAdapter().startDiscovery();
            }

            registerToBTDiscoveryReceiver();
        });
    }

    @Override
    public Completable stopDiscovery() {
        return Completable.fromAction(() -> {
            unregisterToBTDiscoveryReceiver();
            getBluetoothAdapter().cancelDiscovery();
        });
    }

    // ======== End Pairing Related ========

    // ======== Start Discovery Related ========

    @Override
    public Observable<BluetoothDevice> observeBluetoothDiscovery() {
        return mBluetoothDeviceDiscoveryPS;
    }

    @Override
    public void unpair(String deviceMacAddress) {
        for (BluetoothDevice bluetoothDevice : getBluetoothPairedDevices()) {
            if (bluetoothDevice.getAddress().equals(deviceMacAddress)) {
                try {
                    Method m = bluetoothDevice.getClass()
                            .getMethod("removeBond", (Class[]) null);
                    m.invoke(bluetoothDevice, (Object[]) null);
                } catch (Exception e) {
                    Log.d(TAG, "Removing has been failed.", e);
                }
            }
        }
    }

    @Override
    public Completable discoverAndPairDevice(String deviceMacAddress) {
        return stopDiscovery()
                .andThen(startDiscovery())
                .andThen(
                        observeBluetoothDiscovery()
                                .filter(bluetoothDevice -> bluetoothDevice.getAddress().equals(
                                        deviceMacAddress))
                                .firstOrError()
                                .flatMapCompletable(this::pairDevice)
                                .doAfterTerminate(this::stopDiscovery));
    }

    @Override
    public void stop() {
        getBluetoothAdapter().cancelDiscovery();
        unregisterToBTDiscoveryReceiver();
        unregisterToBTPairingReceiver();
    }

    private void registerToBluetoothChanges() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mBluetoothStatusReceiver, filter);
    }

    private void registerToBTPairingReceiver() {
        if (!mPairingReceiverRegistered) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mContext.registerReceiver(mBTPairingReceiver, filter);
            mPairingReceiverRegistered = true;
        }
    }

    // ======== End Discovery Related ========

    private void unregisterToBTPairingReceiver() {
        if (mPairingReceiverRegistered) {
            mContext.unregisterReceiver(mBTPairingReceiver);
            mPairingReceiverRegistered = false;
        }
    }

    private void registerToBTDiscoveryReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mBTDiscoveryReceiver, filter);
        mDiscoveringReceiverRegistered = true;
    }

    private void unregisterToBTDiscoveryReceiver() {
        if (mDiscoveringReceiverRegistered) {
            mContext.unregisterReceiver(mBTDiscoveryReceiver);
            mDiscoveringReceiverRegistered = false;
        }
    }

    private Set<BluetoothDevice> getBluetoothPairedDevices() {
        return getBluetoothAdapter().getBondedDevices();
    }

    protected BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    private boolean isDevicePaired(String mac) {
        return getPairedDevice(mac) != null;
    }

    private BluetoothDevice getPairedDevice(String mac) {
        for (BluetoothDevice bluetoothDevice : getBluetoothPairedDevices()) {
            if (bluetoothDevice.getAddress().equals(mac)) {
                return bluetoothDevice;
            }
        }

        return null;
    }
}
