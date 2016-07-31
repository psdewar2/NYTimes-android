package com.psd.nytimes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsFragment extends DialogFragment {
    String beginDate = "", beginDateDisplayed = "";
    EditText etBeginDate;
    Spinner spSortOrder;
    Button btnSave;
    CheckBox cbArts, cbFashion, cbSports;

    public SettingsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SettingsFragment newInstance(String title) {
        SettingsFragment sdf = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        sdf.setArguments(args);
        return sdf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Settings");
        getDialog().setTitle(title);

        //get shared preferences to populate settings
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //widgets
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        spSortOrder = (Spinner) view.findViewById(R.id.sortOrderSpinner);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);

        beginDate = sharedPreferences.getString("begin_date", "");
        beginDateDisplayed = sharedPreferences.getString("beginDateDisplayed", "");
        etBeginDate.setText(beginDateDisplayed);
        //listener to get calendar dialogs
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (sharedPreferences.getString("sort", "Oldest").equals("Oldest")) spSortOrder.setSelection(0);
        else spSortOrder.setSelection(1);

        Log.d("DEBUG", ":" + sharedPreferences.getBoolean("cbArtsChecked", false));

        cbArts.setChecked(sharedPreferences.getBoolean("cbArtsChecked", false));
        cbFashion.setChecked(sharedPreferences.getBoolean("cbFashionChecked", false));
        cbSports.setChecked(sharedPreferences.getBoolean("cbSportsChecked", false));

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBeginDate.getText().toString().equals("")) beginDate = etBeginDate.getText().toString();
                editor.putString("begin_date", beginDate);
                editor.putString("beginDateDisplayed", etBeginDate.getText().toString());
                editor.putString("sort", spSortOrder.getSelectedItem().toString());
                editor.putBoolean("cbArtsChecked", cbArts.isChecked());
                editor.putBoolean("cbFashionChecked", cbFashion.isChecked());
                editor.putBoolean("cbSportsChecked", cbSports.isChecked());
                editor.apply(); //or commit will work here
                dismiss();
            }
        });
    }

    //for begin date
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            //for shared preferences to be used with New York Times API
            SimpleDateFormat dbSdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
            beginDate = dbSdf.format(myCalendar.getTime());

            //displayed for readability
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            beginDateDisplayed = sdf.format(myCalendar.getTime());

            etBeginDate.setText(beginDateDisplayed);
        }

    };

}
