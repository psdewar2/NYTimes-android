package com.psd.nytimes.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.psd.nytimes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    String beginDate = "", beginDateDisplayed = "";
    EditText etBeginDate;
    Spinner spSortOrder;
    Button btnSave;
    CheckBox cbArts, cbFashion, cbSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get shared preferences to populate settings
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //widgets
        etBeginDate = (EditText) findViewById(R.id.etBeginDate);
        spSortOrder = (Spinner) findViewById(R.id.sortOrderSpinner);
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashion = (CheckBox) findViewById(R.id.cbFashion);
        cbSports = (CheckBox) findViewById(R.id.cbSports);

        beginDate = sharedPreferences.getString("begin_date", "");
        beginDateDisplayed = sharedPreferences.getString("beginDateDisplayed", "");
        etBeginDate.setText(beginDateDisplayed);
        //listener to get calendar dialogs
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SettingsActivity.this, date, myCalendar
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

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                editor.putString("begin_date", beginDate);
                editor.putString("beginDateDisplayed", beginDateDisplayed);
                editor.putString("sort", spSortOrder.getSelectedItem().toString());
                editor.putBoolean("cbArtsChecked", cbArts.isChecked());
                editor.putBoolean("cbFashionChecked", cbFashion.isChecked());
                editor.putBoolean("cbSportsChecked", cbSports.isChecked());
                editor.apply(); //or commit will work here
                int RES_CODE1 = 1;
                setResult(RES_CODE1, i);
                finish();
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

    //for news desk checkboxes
//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//
//        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.cbArts:
//                if (checked) cbArts.setChecked(true);
//                else cbArts.setChecked(false);
//                break;
//            case R.id.cbFashion:
//                if (checked) cbFashion.setChecked(true);
//                else cbFashion.setChecked(false);
//                break;
//            case R.id.cbSports:
//                if (checked) cbSports.setChecked(true);
//                else cbSports.setChecked(false);
//                break;
//        }
//    }
}
