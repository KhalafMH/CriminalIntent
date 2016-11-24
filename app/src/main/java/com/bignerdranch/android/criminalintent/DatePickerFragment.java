package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private static final String EXTRA_DATE =
            DatePickerFragment.class.getPackage().getName() + ".date";
    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    public static Date getResult(Intent data) {
        return (Date) data.getSerializableExtra(EXTRA_DATE);
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_picker, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);

        Date argumentDate = (Date) getArguments().getSerializable(ARG_DATE);
        if (argumentDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(argumentDate);
            int year = cal.get(Calendar.YEAR);
            int monthOfYear = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            mDatePicker.init(year, monthOfYear, dayOfMonth, null);
        }

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.date_picker_title)
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int dayOfMonth = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();

                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        Intent data = new Intent();
        data.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
    }
}
