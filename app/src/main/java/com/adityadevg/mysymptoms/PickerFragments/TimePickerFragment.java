package com.adityadevg.mysymptoms.PickerFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.adityadevg.mysymptoms.R;

import java.util.Calendar;

/**
 * Created by adityadev on 3/8/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String DEFAULT_VALUE_FOR_SECONDS = "00";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView timeOfOccurrence_tv = (TextView) getActivity().findViewById(R.id.tv_timeOfOccurrence);

        String str_hourOfDay = String.valueOf(hourOfDay);
        String str_minute = String.valueOf(minute);
        if (hourOfDay < 10)
            str_hourOfDay = "0" + hourOfDay;

        if (minute < 10)
            str_minute = "0" + minute;



        timeOfOccurrence_tv.setText(str_hourOfDay + ":" + str_minute + ":" + DEFAULT_VALUE_FOR_SECONDS);
    }
}
