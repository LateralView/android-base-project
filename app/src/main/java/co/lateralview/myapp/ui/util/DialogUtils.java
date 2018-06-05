package co.lateralview.myapp.ui.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public final class DialogUtils {

    private DialogUtils() {

    }

    public static AlertDialog createDialog(Context context,
            DialogInterface.OnClickListener disconnectClickListener) {
        return new AlertDialog.Builder(context)
                .setTitle("Title")
                .setMessage("Description")
                .setPositiveButton("Ok", disconnectClickListener)
                .setNegativeButton("cancel", null)
                .setCancelable(false)
                .create();
    }
}
