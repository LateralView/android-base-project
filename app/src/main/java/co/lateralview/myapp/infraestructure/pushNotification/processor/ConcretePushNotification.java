package co.lateralview.myapp.infraestructure.pushNotification.processor;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

import co.lateralview.myapp.infraestructure.pushNotification.processor.base.NotificationType;
import co.lateralview.myapp.infraestructure.pushNotification.processor.base
        .PushNotificationProcessor;


public class ConcretePushNotification extends PushNotificationProcessor implements Serializable {
    public ConcretePushNotification(NotificationType notificationType, String title,
            String description, boolean fromActivity) {
        super(notificationType, title, description, fromActivity);
    }

    @Override
    public void process(Context context) {
        Intent intent = null;

        if (isAppInForeground()) {
            //The app is open
        } else {
            //The app is closed
        }

        sendNotification(context, intent);
    }
}
