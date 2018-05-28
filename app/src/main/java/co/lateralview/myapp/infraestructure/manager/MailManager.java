package co.lateralview.myapp.infraestructure.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import co.lateralview.myapp.R;

public class MailManager {
    public MailManager() {

    }

    public void sendSupportMail(Activity activity, String to, String subject) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("mailto:")
            .append(to)
            .append("?subject=")
            .append(subject)
            .append("&body=");

        String uriString = buffer.toString().replace(" ", "%20");

        activity.startActivity(
            Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse(uriString)),
                activity.getString(R.string.app_name) + " help mail"));
    }
}
