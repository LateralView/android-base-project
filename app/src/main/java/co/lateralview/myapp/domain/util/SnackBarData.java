package co.lateralview.myapp.domain.util;

import android.support.design.widget.Snackbar;

import java.io.Serializable;

import co.lateralview.myapp.R;
import co.lateralview.myapp.ui.util.SnackBarHelper;

public class SnackBarData implements Serializable
{
    public enum SnackBarType implements Serializable
    {
        DUMMY(-1, -1, -1, Snackbar.LENGTH_INDEFINITE),
        CONNECTION_ERROR(R.string.snackBarConnectionError_description,
                R.string.snackBarConnectionError_action, R.color.red, Snackbar.LENGTH_INDEFINITE),
        NO_INTERNET(R.string.snackBarNoInternet_description, -1, R.color.red,
                Snackbar.LENGTH_INDEFINITE);

        private int mDescription, mActionTitle, mBackgroundColor, mDuration;

        SnackBarType(int description, int actionTitle, int backgroundColor, int duration)
        {
            mDescription = description;
            mActionTitle = actionTitle;
            mBackgroundColor = backgroundColor;
            mDuration = duration;
        }

        public int getDescription()
        {
            return mDescription;
        }

        public int getActionTitle()
        {
            return mActionTitle;
        }

        public int getBackgroundColor()
        {
            return mBackgroundColor;
        }

        public int getDuration()
        {
            return mDuration;
        }
    }

    private SnackBarType mSnackBarType;
    private SnackBarHelper.ISnackBarHandler mSnackBarListener;

    public SnackBarData(SnackBarType mSnackBarType)
    {
        this(mSnackBarType, null);
    }

    public SnackBarData(SnackBarType mSnackBarType,
            SnackBarHelper.ISnackBarHandler mSnackBarListener)
    {
        this.mSnackBarType = mSnackBarType;
        this.mSnackBarListener = mSnackBarListener;
    }

    public SnackBarType getSnackBarType()
    {
        return mSnackBarType;
    }

    public void setSnackBarType(SnackBarType mSnackBarType)
    {
        this.mSnackBarType = mSnackBarType;
    }

    public SnackBarHelper.ISnackBarHandler getSnackBarListener()
    {
        return mSnackBarListener;
    }

    public void setSnackBarListener(SnackBarHelper.ISnackBarHandler mSnackBarListener)
    {
        this.mSnackBarListener = mSnackBarListener;
    }
}


