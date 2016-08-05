package co.lateralview.myapp.infraestructure.pushNotification.processor.base;

import java.io.Serializable;

/**
 * Created by Julian on 4/7/16.
 */
public enum NotificationType implements Serializable
{
	NOTIFICATION_TYPE("NOTIFICATION_TYPE");

	private String mType;

	NotificationType(String type)
	{
		mType = type;
	}

	public static NotificationType fromString(String type)
	{
		for (NotificationType notificationType : NotificationType.values())
		{
			if (notificationType.getType().equals(type))
			{
				return notificationType;
			}
		}
		return null;
	}

	public String getType()
	{
		return mType;
	}
}
