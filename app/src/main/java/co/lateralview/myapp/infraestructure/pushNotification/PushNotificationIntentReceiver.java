package co.lateralview.myapp.infraestructure.pushNotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import co.lateralview.myapp.application.MyApp;
import co.lateralview.myapp.infraestructure.pushNotification.processor.base.NotificationType;
import co.lateralview.myapp.infraestructure.pushNotification.processor.base
        .PushNotificationProcessor;

/**
 * Created by Julian on 4/5/16.
 */
public class PushNotificationIntentReceiver extends WakefulBroadcastReceiver
{
    public static final String TAG = PushNotificationIntentReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = intent.getExtras();

        PushNotificationProcessor concretePushNotification = PushNotificationProcessor.create(
                NotificationType.fromString(extras.getString("type").toUpperCase()),
                extras.getString("title"), extras.getString("message"),
                MyApp.isApplicationRunning());

        if (concretePushNotification != null)
        {
            concretePushNotification.process(context);
        }
    }
}
