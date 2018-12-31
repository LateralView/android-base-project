package co.lateralview.myapp.infraestructure.manager.bluetooth.base.model;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_READ;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_SUBSCRIBE;
import static co.lateralview.myapp.infraestructure.manager.bluetooth.base.model.BleOperationType
        .TYPE_WRITE;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

@Retention(SOURCE)
@IntDef({TYPE_SUBSCRIBE, TYPE_READ, TYPE_WRITE})
public @interface BleOperationType {
    int TYPE_SUBSCRIBE = 0;
    int TYPE_READ = 1;
    int TYPE_WRITE = 2;
}
