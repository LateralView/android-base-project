package co.lateralview.myapp.infraestructure.manager.bluetooth.base.model;

import java.util.UUID;

public final class BleOperation {
    private static int sTemporaryId = 0;

    /**
     * Identify temporarily the BLE Operation until we get the final Characteristic Instance ID.
     */
    private int id;

    private UUID service;
    private UUID charUuid;

    // Only used for characteristic write
    private byte[] data;

    // Only used for characteristic notification subscription
    private boolean enable;

    @BleOperationType
    private int type;

    private boolean highPriority = false;

    private BleOperation(UUID service, UUID charUuid, byte[] data, boolean enable, int type,
            boolean highPriority) {
        this.id = sTemporaryId++;
        this.service = service;
        this.charUuid = charUuid;
        this.data = data;
        this.enable = enable;
        this.type = type;
        this.highPriority = highPriority;
    }

    public static BleOperation newWriteOperation(UUID service, UUID charUuid, byte[] data,
            boolean highPriority) {
        return new BleOperation(service, charUuid, data, false, BleOperationType.TYPE_WRITE,
                highPriority);
    }

    public static BleOperation newReadOperation(UUID service, UUID charUuid, boolean highPriority) {
        return new BleOperation(service, charUuid, null, false, BleOperationType.TYPE_READ,
                highPriority);
    }

    public static BleOperation newSubscribeOperation(UUID service, UUID charUuid, boolean enable,
            boolean highPriority) {
        return new BleOperation(service, charUuid, null, enable, BleOperationType.TYPE_SUBSCRIBE,
                highPriority);
    }

    public int getId() {
        return id;
    }

    public UUID getService() {
        return service;
    }

    public UUID getCharUuid() {
        return charUuid;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isEnable() {
        return enable;
    }

    public int getType() {
        return type;
    }

    public boolean isHighPriority() {
        return highPriority;
    }
}
