package co.lateralview.myapp.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    public static final String TAG = DatePickerDialogFragment.class.getSimpleName();

    public interface DatePickerInterface
    {
        void onDateSelected(Date date);
    }

    private DatePickerInterface mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        Date expirationDate = calendar.getTime();

        if (mCallback != null)
        {
            mCallback.onDateSelected(expirationDate);
        }
    }

    public void setListener(DatePickerInterface datePickerInterface)
    {
        mCallback = datePickerInterface;
    }
}