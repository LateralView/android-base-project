package co.lateralview.myapp.infraestructure.manager.bluetooth.base;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;

import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_READ;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_SUBSCRIBE;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_WRITE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.lateralview.myapp.infraestructure.manager.bluetooth.base.exceptions.BleException;
import co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperation;
import co.lateralview.myapp.ui.util.RxSchedulersUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.BehaviorSubject;

/**
 * You must have a concrete implementation according your BT Device specifications.
 */
public abstract class BluetoothDeviceManager {
    /**
     * Created a thread with a Single behavior to handle all BT I/O requests sequentially.
     */
    @NonNull
    protected static final Scheduler BLUETOOTH_SINGLE_SCHEDULER = RxJavaPlugins.onSingleScheduler(
            RxJavaPlugins.initSingleScheduler(SingleScheduler::new));
    private static final String TAG = "BluetoothDeviceManager";
    protected Context mContext;
    protected String mDeviceMacAddress;
    protected BluetoothManager mBluetoothManager;
    private List<BleOperation> mBleOperations = new ArrayList<>();
    private BluetoothGatt mBtGatt;
    private boolean mConnectionReady = false;
    private boolean mBleOperationInProgress = false;
    private BehaviorSubject<Pair<Integer, Integer>> mBluetoothOperationIdPS =
            BehaviorSubject.create();
    private BehaviorSubject<BluetoothGattCharacteristic> mBluetoothOperationResultsPS =
            BehaviorSubject.create();
    private BehaviorSubject<Integer> mBluetoothDeviceStatusPS = BehaviorSubject.create();
    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
            Log.d(TAG, "New CharacteristicWrite");

            // Result of a characteristic write operation
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mBluetoothOperationResultsPS.onNext(characteristic);
                // TODO Handler Errors too (Maybe Object with all the Info ?)
            }

            performNextOperation();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
            Log.d(TAG, "New CharacteristicRead");

            // Result of a characteristic read operation
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mBluetoothOperationResultsPS.onNext(characteristic);
                // TODO Handler Errors too (Maybe Object with all the Info ?)
            }

            performNextOperation();
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                BluetoothGattDescriptor descriptor,
                int status) {
            Log.d(TAG, "New DescriptorWrite");
            performNextOperation();
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                BluetoothGattDescriptor descriptor,
                int status) {
            Log.d(TAG, "New DescriptorRead");
            performNextOperation();
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt,
                int status,
                int newState) {
            Log.d(TAG, "onConnectionStateChange");
            switch (newState) {
                case STATE_DISCONNECTED:
                    Log.d(TAG, "onConnectionStateChange STATE_DISCONNECTED");
                    mBtGatt = null;
                    mConnectionReady = false;
                    mBleOperationInProgress = false;
                    mBluetoothDeviceStatusPS.onNext(BluetoothDeviceState.DISCONNECTED);
                    break;
                case STATE_CONNECTED:
                    Log.d(TAG, "onConnectionStateChange STATE_CONNECTED");
                    mBtGatt.discoverServices();
                    mBluetoothDeviceStatusPS.onNext(BluetoothDeviceState.CONNECTED);
                    break;
                default:
                    Log.d(TAG, "onConnectionStateChange OTHER");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt,
                int status) {
            Log.d(TAG, "onServicesDiscovered");
            if (status == GATT_SUCCESS) {
                mConnectionReady = true;
                onBTServicesDiscovered();
                performNextOperation();
            }
        }
    };

    @SuppressLint("CheckResult")
    public BluetoothDeviceManager(BluetoothManager bluetoothManager, Context context,
            String deviceMacAddress) {
        mContext = context;
        mDeviceMacAddress = deviceMacAddress;
        mBluetoothManager = bluetoothManager;

        mBluetoothManager.observeBluetoothStatus()
                .compose(RxSchedulersUtils.observeObservableOnIO())
                .subscribe(status -> {
                    if (status == BluetoothState.BLUETOOTH_OFF) {
                        clearStates();
                        Log.d(TAG, "Bluetooth Status: " + String.valueOf(status));
                    }
                }, error -> {
                });
    }

    public Observable<Integer> observeDeviceState() {
        return mBluetoothDeviceStatusPS;
    }

    public void dump() {
        BluetoothDevice bluetoothDevice = getPairedDevice(mDeviceMacAddress);

        if (bluetoothDevice == null) {
            return; // Nothing
        }

        BluetoothUtils.dumpBluetoothDevice(bluetoothDevice);
    }

    private BluetoothDevice getPairedDevice(String mac) {
        for (BluetoothDevice bluetoothDevice : mBluetoothManager.getPairedDevices().blockingGet()) {
            if (bluetoothDevice.getAddress().equals(mac)) {
                return bluetoothDevice;
            }
        }

        return null;
    }

    protected Completable write(UUID service, UUID charUuid, byte[] data, boolean highPriority) {
        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(
                            BleOperation.newWriteOperation(service, charUuid, data, highPriority));
                    checkQueue();
                }))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doWrite(BleOperation bleOperation) {
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());
        characteristic.setValue(bleOperation.getData());

        if (mBtGatt.writeCharacteristic(characteristic)) {
            Log.d(TAG, "Performing write");
        } else {
            Log.d(TAG, "Couldn't perform write!");
        }
    }

    protected Single<BluetoothGattCharacteristic> read(UUID service, UUID charUuid) {
        return read(service, charUuid, false);
    }

    protected Single<BluetoothGattCharacteristic> read(UUID service, UUID charUuid,
            boolean highPriority) {
        //TODO Replicate to Write Flow
        BleOperation bleOperation = BleOperation.newReadOperation(service, charUuid, highPriority);

        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(bleOperation);
                    checkQueue();
                }))
                .andThen(mBluetoothOperationIdPS
                        .filter(operationIds -> operationIds.first == bleOperation.getId())
                        .firstOrError()
                        .flatMap(operationIds -> mBluetoothOperationResultsPS
                                .filter(bluetoothGattCharacteristic ->
                                        bluetoothGattCharacteristic.getInstanceId()
                                                == operationIds.second)
                                .doOnNext(resultPepe -> Log.i(TAG,
                                        "New Read Result: " + resultPepe.toString()))
                                .firstOrError()))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doRead(BleOperation bleOperation) {
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());

        //TODO Replicate to Write Flow
        mBluetoothOperationIdPS.onNext(
                new Pair<>(bleOperation.getId(), characteristic.getInstanceId()));

        if (mBtGatt.readCharacteristic(characteristic)) {
            Log.d(TAG, "Performing read");
        } else {
            Log.d(TAG, "Couldn't perform read!");
        }
    }

    protected Completable subscribe(UUID service, UUID charUuid, boolean enable,
            boolean highPriority) {
        return checkConnection()
                .andThen(Completable.fromAction(() -> {
                    addOperationToQueue(
                            BleOperation.newSubscribeOperation(service, charUuid, enable,
                                    highPriority));
                    checkQueue();
                }))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER);
    }

    private void doSubscribe(BleOperation bleOperation) {
        Log.d(TAG, "doSubscribe");
        BluetoothGattService gattService = mBtGatt.getService(bleOperation.getService());
        BluetoothGattCharacteristic characteristic = gattService.getCharacteristic(
                bleOperation.getCharUuid());
        if (mBtGatt.setCharacteristicNotification(characteristic, bleOperation.isEnable())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")); //TODO Check this
            if (descriptor != null) {
                byte[] value =
                        bleOperation.isEnable() ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
                descriptor.setValue(value);

                if (mBtGatt.writeDescriptor(descriptor)) {
                    Log.d(TAG, "Performing subscription");
                } else {
                    Log.d(TAG, "Couldn't perform subscription!");
                }
            }
        }
    }

    private Completable checkConnection() {
        return mBluetoothManager.turnOn()
                .doOnError(error -> Log.d(TAG, "Error Turning ON BT"))
                .andThen(Completable.fromAction(this::startConnection));
    }

    private synchronized void startConnection()
            throws BleException.BluetoothDeviceUnpairedException {
        if (mBtGatt == null) {
            BluetoothDevice bluetoothDevice = getPairedDevice(mDeviceMacAddress);
            if (bluetoothDevice == null) { // Device got unpaired
                Log.d(TAG, "Device got unpaired");
                throw new BleException.BluetoothDeviceUnpairedException();
            }

            mBtGatt = bluetoothDevice.connectGatt(mContext, false, mBluetoothGattCallback);
        }
    }

    protected void onBTServicesDiscovered() {
        // Do something ?
    }

    private synchronized void addOperationToQueue(BleOperation bleOperation) {
        int position = 0;

        for (int i = 0; i < mBleOperations.size(); i++) {
            if (!mBleOperations.get(0).isHighPriority()) { // Keep High Priority Operations at top
                break;
            }

            position++;
        }

        mBleOperations.add(position, bleOperation);
    }

    private synchronized void checkQueue() {
        if (!mBleOperationInProgress) {
            performNextOperation();
        }
    }

    @SuppressLint("CheckResult")
    private synchronized void performNextOperation() {
        if (!mConnectionReady) {
            return; // Connection Not Ready
        }

        if (mBleOperations.size() == 0) {
            mBleOperationInProgress = false;
            return;
        }

        mBleOperationInProgress = true;

        Completable.fromAction(() -> performOperation(mBleOperations.remove(0)))
                .subscribeOn(BLUETOOTH_SINGLE_SCHEDULER)
                .subscribe(
                        () -> Log.d(TAG, "Performing BT Operation"),
                        error -> Log.w(TAG, "Error performing BT Operation")
                );
    }

    private synchronized void performOperation(BleOperation bleOperation) {
        switch (bleOperation.getType()) {
            case TYPE_READ:
                doRead(bleOperation);
                break;
            case TYPE_WRITE:
                doWrite(bleOperation);
                break;
            case TYPE_SUBSCRIBE:
                doSubscribe(bleOperation);
                break;
        }
    }

    protected void clearStates() {
        if (mBtGatt != null) {
            mBtGatt.close();
            mBtGatt = null;
        }

        mBleOperations = new ArrayList<>();
        mConnectionReady = false;
        mBleOperationInProgress = false;
    }
}
