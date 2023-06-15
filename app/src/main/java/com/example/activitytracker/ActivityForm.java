package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ActivityForm extends AppCompatActivity{

    // Initialize Variables
    private TextView actFormTitle, actFormSubTask, actFormRepeatTask, actFormDueDate, actFormTimeDue, actFormNotes, actNotesList;
    private EditText actNotes;
    private Switch actRepeat;
    private RadioGroup rgRepeatTask;
    private RadioButton radioActivityRepeatButton;
    private RadioButton rbHourly, rbDaily, rbMonthly, rbYearly;
    private Button actDueDate, actTimeDue, btnAddNotes, btnEditNotes, btnSaveNotes, btncreateTask;
    private String radioText;
    private boolean switchChecked = false;
    private int taskHour, taskMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        // All fields that is positioned on the left the form
        actFormTitle = findViewById(R.id.actFormTitle);
        actFormRepeatTask = findViewById(R.id.actFormRepeatTask);
        actFormDueDate = findViewById(R.id.actFormDueDate);
        actFormTimeDue = findViewById(R.id.actFormTimeDue);
        actFormNotes = findViewById(R.id.actFormNotes);
        actNotesList = findViewById(R.id.actNotesList);

        // Fields for time and due date, position in the right of form
        actTimeDue = findViewById(R.id.actTimeDue);
        actDueDate = findViewById(R.id.actDue);

        // Field for the repeat task
        rgRepeatTask = findViewById(R.id.rgRepeatTask);
        actRepeat = findViewById(R.id.actRepeat);
        rbHourly = findViewById(R.id.rbHourly);
        rbDaily = findViewById(R.id.rbDaily);
        rbMonthly = findViewById(R.id.rbMonthly);
        rbYearly = findViewById(R.id.rbYearly);

        // Fields for Notes
        btnAddNotes = findViewById(R.id.btnAddNotes);
        // Fields for create task button
        btncreateTask = findViewById(R.id.btnCreateTask);

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        // We use the SimpleDateFormat to create a customize time
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("hh:mm:a");
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("MMMM-dd-yyyy");
        String currentTime = simpleDateFormatTime.format(calendar.getTime());
        String currentDate = simpleDateFormatDate.format(calendar.getTime());
        actTimeDue.setText(currentTime); // set the customize current time
        actDueDate.setText(currentDate);

        /**
         * Activity Time Reminder Picker
         * if the created task will remind the user when the due date is near
         */
        actTimeDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ActivityForm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                // get the value of hourOfDay and minute, then store it in taskHour and taskMinute
                                taskHour = hourOfDay;
                                taskMinute = minute;
                                // use the calendar to set the time and minute
                                calendar.set(0, 0, 0, taskHour, taskMinute);
                                // set selected time and on text view
                                actTimeDue.setText(DateFormat.format("hh:mm:a", calendar));
                            }
                            // default value of time
                        }, 12, 0, false
                );
                // display the previous selected time
                timePickerDialog.updateTime(hour, minutes);
                timePickerDialog.show();
            }
        });

        /**
         * Due Date Picker
         * When is the due date of the created activity
         */
        actDueDate.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivityForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month;
                        // Date Format in the text view
                        String date = day+"/"+month+"/"+year;
                        // set the format in the text view
                        calendar.set(year, month, day);
                        actDueDate.setText(DateFormat.format("MMMM-dd-yyyy", calendar));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        /**
         * Repeat Task Picker
         * if the create activity will be repeated
         */

        ThemeSwitcherActivity themeSwitcherActivity = new ThemeSwitcherActivity(ActivityForm.this);
        SharedPreferences shared = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean isOn = shared.getBoolean("appTheme", themeSwitcherActivity.isOn());
        System.out.println(isOn);

        if(isOn){
            Drawable butttonDrawable = rbHourly.getBackground();
            butttonDrawable = DrawableCompat.wrap(butttonDrawable);
            DrawableCompat.setTint(butttonDrawable, getResources().getColor(R.color.yellowDark));
            rbHourly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            rbHourly.setTextColor(ActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            rbDaily.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            rbDaily.setTextColor(ActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            rbMonthly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            rbMonthly.setTextColor(ActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            rbYearly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            rbYearly.setTextColor(ActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));

        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        actRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                // if switch is disabled, it will also disable the button
                if(isChecked == false){
                    switchChecked = false;
                    rbHourly.setEnabled(false);
                    rbDaily.setEnabled(false);
                    rbMonthly.setEnabled(false);
                    rbYearly.setEnabled(false);
                    Toast.makeText(ActivityForm.this, "Repeat Task Disabled", Toast.LENGTH_SHORT).show();

                // if switch is enabled, it will also enabled the button
                }else {
                    switchChecked = true;
                    rbHourly.setEnabled(true);
                    rbDaily.setEnabled(true);
                    rbMonthly.setEnabled(true);
                    rbYearly.setEnabled(true);
                    Toast.makeText(ActivityForm.this, "Repeat Task Enabled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesSettings();
            }
        });
        DatabaseHelper activitiesdb = new DatabaseHelper(ActivityForm.this);
        btncreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if the switch is enabled
                if (switchChecked == false){
                    radioText = "No Text";
                }else {
                    // get the selected value of the radio button from radio group
                    int selectedId = rgRepeatTask.getCheckedRadioButtonId();
                    // find the radio button by returned id
                    radioActivityRepeatButton = (RadioButton) findViewById(selectedId);
                    // get the string value of selected radio button and store it in radioText
                    radioText = radioActivityRepeatButton.getText().toString();
                }
                if(actNotesList.getText().toString().equals(null)){
                    String notes = "No notes";
                    actNotesList.setText(notes);
                }
                Activities activities = new Activities(-1, false, actFormTitle.getText().toString(), actTimeDue.getText().toString(), switchChecked, radioText, actDueDate.getText().toString(), actNotesList.getText().toString());
                activitiesdb.addOne(activities);
                Intent returnIntent = new Intent();
                setResult(ActivityForm.RESULT_OK, returnIntent);
                finish();
            }
        });

    }

   private void notesSettings(){

       actNotesList = findViewById(R.id.actNotesList);
       actNotes = findViewById(R.id.actNotes);
       btnSaveNotes = findViewById(R.id.btnSave);
       btnEditNotes = findViewById(R.id.btnEdit);

       if(!actNotesList.getText().toString().equals("")){
           actNotes.setText(actNotesList.getText().toString());
           actNotesList.setText("");
       }

       actFormNotes.setVisibility(View.GONE);
       actNotes.setVisibility(View.VISIBLE);
       btnAddNotes.setVisibility(View.GONE);
       btnEditNotes.setVisibility(View.GONE);
       btnSaveNotes.setVisibility(View.VISIBLE);

       btnEditNotes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!actNotesList.getText().toString().equals("")){
                   actNotes.setText(actNotesList.getText().toString());
                   actNotesList.setText("");
               }
               actFormNotes.setVisibility(View.GONE);
               actNotes.setVisibility(View.VISIBLE);
               btnAddNotes.setVisibility(View.GONE);
               btnEditNotes.setVisibility(View.GONE);
               btnSaveNotes.setVisibility(View.VISIBLE);
           }
       });

       btnSaveNotes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               closeKeyPad();
               if (!actNotes.getText().toString().equals("")){
                   actNotesList.setText(actNotes.getText().toString());
                   actNotes.setText("");
               }
               if (!actNotesList.getText().toString().equals("")){
                   btnAddNotes.setVisibility(View.GONE);
                   btnEditNotes.setVisibility(View.VISIBLE);
               }else{
                   btnAddNotes.setVisibility(View.VISIBLE);
                   btnEditNotes.setVisibility(View.GONE);
               }
               actFormNotes.setVisibility(View.VISIBLE);
               actNotesList.setVisibility(View.VISIBLE);
               actNotes.setVisibility(View.GONE);
               btnSaveNotes.setVisibility(View.GONE);
           }
       });

       actNotesList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!actNotesList.getText().toString().equals("")){
                   actNotes.setText(actNotesList.getText().toString());
                   actNotesList.setText("");
               }
               actFormNotes.setVisibility(View.GONE);
               actNotes.setVisibility(View.VISIBLE);
               btnAddNotes.setVisibility(View.GONE);
               btnEditNotes.setVisibility(View.GONE);
               btnSaveNotes.setVisibility(View.VISIBLE);
           }
       });

   }

    private void closeKeyPad(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}