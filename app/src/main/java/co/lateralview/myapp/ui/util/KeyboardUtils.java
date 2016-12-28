package co.lateralview.myapp.ui.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by julianfalcionelli on 11/15/16.
 */
public class KeyboardUtils
{
    public static void hideSoftKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity)
    {
        if (!isKeyboardVisible(activity))
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            activity.getWindow().getDecorView().requestFocus();
            inputMethodManager.showSoftInput(activity.getWindow().getDecorView(), 0);
        }
    }

    public static boolean isKeyboardVisible(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.isAcceptingText();
    }

}
