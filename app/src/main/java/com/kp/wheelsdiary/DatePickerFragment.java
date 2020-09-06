package com.kp.wheelsdiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        EditText editText = getActivity().findViewById(R.id.dateScheduled);
        Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String date = dateFormat.format(c.getTime());
        System.out.println("Picked date: " + date);
        editText.setText(date);

    }
}
