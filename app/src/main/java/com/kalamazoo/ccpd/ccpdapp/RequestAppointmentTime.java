package com.kalamazoo.ccpd.ccpdapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class RequestAppointmentTime extends AppCompatActivity implements View.OnClickListener {
    final Calendar newCalendar = Calendar.getInstance();
    String kID = "k14rw01";
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private EditText selectDate;
    private EditText selectTime;
    private EditText enterMessage;
    private Switch addToCalendar;
    private Switch ccMeSwitch;
    private boolean correctDate = false;
    private boolean correctTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set variables to their proper views
        findViewsById();

        setDateField();
        setTimeField();


        // Floating action button used to submit form
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = enterMessage.getText().toString();
                String date = selectDate.getText().toString();
                String time = selectTime.getText().toString();
                if ((correctTime && correctDate) && !message.equals("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "career@kzoo.edu", null));
                    if (ccMeSwitch.isChecked()) {
                        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{kID + "@kzoo.edu"});
                    }
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New appointment requested by: " + kID + " on " + date + " at " + time);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "A new appointment has been requested by" + "\n" + kID +
                            "\nFor:\n" + date +
                            "\nAt:\n" + time +
                            "\nConcerning:\n" + message);
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    if (addToCalendar.isChecked()) {

                        Intent calIntent = new Intent(Intent.ACTION_EDIT);
                        calIntent.setType("vnd.android.cursor.item/event");
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, newCalendar.getTimeInMillis());
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, newCalendar.getTimeInMillis() + 30 * 60 * 1000);
                        calIntent.putExtra(CalendarContract.Events.TITLE, "Appointment with CCPD staff");
                        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Kalamazoo College Center for Career and Professional Development");
                        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Concerning: " + message);
                        startActivity(calIntent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Incomplete or invalid appointment request. Please make sure you have completed all fields correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
        A method used to link variables to their view respective view
     */
    private void findViewsById() {
        selectDate = (EditText) findViewById(R.id.dateSelect);
        selectDate.setInputType(InputType.TYPE_NULL);
        selectDate.requestFocus();


        selectTime = (EditText) findViewById(R.id.timeSelect);
        selectTime.setInputType(InputType.TYPE_NULL);

        enterMessage = (EditText) findViewById(R.id.message);

        addToCalendar = (Switch) findViewById(R.id.addToCalendar);
        ccMeSwitch = (Switch) findViewById(R.id.ccme);
    }

    private void setDateField() {
        //Create a new calendar object used to get current time
        selectDate.setOnClickListener(this);


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                if (newCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || newCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    selectDate.setText("The CCPD is not open on the weekend.");
                    correctDate = false;
                } else {
                    selectDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                    correctDate = true;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTimeField() {
        selectTime.setOnClickListener(this);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectTime.setText(formatTime(hourOfDay, minute));
                newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newCalendar.set(Calendar.MINUTE, minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), false);
    }

    public String formatTime(int hour, int minute) {
        int hourOfDay = hour;
        String dayOrNight = "AM";
        String leadingZero = "";
        if (hourOfDay < 8 || hourOfDay > 15) {
            correctTime = false;
            return "Please enter a time during business hours (8-4)";
        } else {
            if (hourOfDay > 11) {
                dayOrNight = "PM";
                if (hourOfDay > 12) {
                    hourOfDay = hourOfDay - 12;
                }
            }
            if (minute < 10) {
                leadingZero = "0";
            }
            correctTime = true;
            return hourOfDay + ":" + leadingZero + minute + dayOrNight;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == selectDate) {
            datePickerDialog.show();
            selectTime.requestFocus();
        } else if (view == selectTime) {
            timePickerDialog.show();
        }
    }

    /**
     * public boolean onCreateOptionsMenu(Menu menu) {
     * // Inflate the menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.menu_faqsubmission, menu);
     * return true;
     * }
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

