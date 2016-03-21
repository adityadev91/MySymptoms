package com.adityadevg.mysymptoms.PickerFragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.adityadevg.mysymptoms.R;

import java.util.Calendar;

/**
 * Created by adityadev on 3/8/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static final int ADJUST_MONTH_INDEX = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        //Create and return a new instance of TimePickerDialog
        return new DatePickerDialog(getActivity(),this,year,month,date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView dateOfOccurrence_tv = (TextView) getActivity().findViewById(R.id.tv_dateOfOccurrence);

        monthOfYear = monthOfYear + ADJUST_MONTH_INDEX;

        String str_monthOfYear = String.valueOf(monthOfYear);
        String str_dayOfMonth = String.valueOf(dayOfMonth);

        if (monthOfYear < 10)
            str_monthOfYear = "0" + monthOfYear;
        if (dayOfMonth < 10)
            str_dayOfMonth = "0" + dayOfMonth;

        dateOfOccurrence_tv.setText(year+"-"+str_monthOfYear+"-"+str_dayOfMonth);

        DialogFragment setDateTimeFragment = new TimePickerFragment();
        setDateTimeFragment.show(getFragmentManager(),getString(R.string.time_picker));
    }
}
