package co.lateralview.myapp.ui.util;


import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import co.lateralview.myapp.domain.util.SnackBarData;

public final class SnackBarHelper {

    private SnackBarHelper() {
    }

    public static Snackbar createSnackBar(Activity activity, SnackBarData snackBar) {
        return createSnackBar(getActivityViewContainer(activity), snackBar);
    }

    public static Snackbar createSnackBar(View view, SnackBarData snackBar) {
        SnackBarData.SnackBarType snackBarType = snackBar.getSnackBarType();

        String description = null;
        String action = null;

        if (snackBarType != null) {
            if (snackBarType.getDescription() != -1) {
                description = view.getContext().getString(snackBarType.getDescription());
            }

            if (snackBarType.getActionTitle() != -1) {
                action = view.getContext().getString(snackBarType.getActionTitle());
            }
        }

        return createSnackBar(view, description, action, snackBarType.getDuration(),
            snackBarType.getBackgroundColor(), snackBar.getSnackBarListener());
    }

    public static Snackbar createSnackBar(Activity activity, String description, String action,
                                          int duration, int backgroundColor, final ISnackBarHandler snackBarHandler) {
        return createSnackBar(getActivityViewContainer(activity), description, action, duration,
            backgroundColor, snackBarHandler);
    }

    public static Snackbar createSnackBar(View view, String description, String action,
                                          int duration, int backgroundColor, final ISnackBarHandler snackBarHandler) {
        final Snackbar snackbar = Snackbar.make(view, description, duration);

        if (action != null && snackBarHandler != null) {
            snackbar.setAction(action, view1 -> {
                snackBarHandler.onActionClick();
                snackbar.dismiss();
            });
        }

        snackbar.setActionTextColor(Color.WHITE);

        View snackBarView = snackbar.getView();

        if (backgroundColor != -1) {
            snackBarView.setBackgroundColor(
                UIUtils.getColorFromRes(view.getContext(), backgroundColor));
        }

        TextView textView = (TextView) snackBarView.findViewById(
            android.support.design.R.id.snackbar_text);

        textView.setOnClickListener(v -> snackbar.dismiss());

        textView.setTextColor(Color.WHITE);

        return snackbar;
    }

    private static View getActivityViewContainer(Activity activity) {
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public interface ISnackBarHandler {
        void onActionClick();
    }
}
