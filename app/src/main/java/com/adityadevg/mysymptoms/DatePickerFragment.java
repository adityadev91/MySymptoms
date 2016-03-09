package com.adityadevg.mysymptoms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by adityadev on 3/8/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
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
        dateOfOccurrence_tv.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
        DialogFragment setDateTimeFragment = new TimePickerFragment();
        setDateTimeFragment.show(getFragmentManager(),getString(R.string.time_picker));
    }
}
