package co.lateralview.myapp.infraestructure.pushNotification.processor.base;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import java.io.Serializable;

import co.lateralview.myapp.R;
import co.lateralview.myapp.infraestructure.pushNotification.processor.ConcretePushNotification;

public abstract class PushNotificationProcessor implements Serializable {
    protected NotificationType mNotificationType;
    protected String mTitle;
    protected String mDescription;
    private boolean mAppRunning; //Some Activity on the stack

    public PushNotificationProcessor(NotificationType notificationType, String title,
            String description, boolean appRunning) {
        mNotificationType = notificationType;
        mTitle = title;
        mDescription = description;
        mAppRunning = appRunning;
    }

    public static PushNotificationProcessor create(NotificationType notificationType, String title,
            String message, boolean appRunning) {
        switch (notificationType) {
            default:
                return new ConcretePushNotification(notificationType, title, message, appRunning);
        }
    }

    protected void sendNotification(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mTitle != null && !mTitle.isEmpty() ? mTitle
                        : context.getString(R.string.app_name))
                .setContentText(mDescription)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationType.ordinal(), notificationBuilder.build());
    }

    public abstract void process(Context context);

    public NotificationType getNotificationType() {
        return mNotificationType;
    }

    public boolean isAppInForeground() {
        return mAppRunning;
    }
}
