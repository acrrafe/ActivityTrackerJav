package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.activitytracker.TaskActivityRecView.ACTIVITY_UPDATE_ID;


public class UpdateActivityForm extends AppCompatActivity {
    // Initialize Variables

    private TextView updateActFormTitle, updateActFormRepeatTask, updateActFormDueDate, updateActFormTimeDue, updateActFormNotes, updateActNotesList;
    private EditText updateActNotes;
    private Switch updateActRepeat;
    private RadioGroup updateRgRepeatTask;
    private RadioButton updateRadioActivityRepeatButton;
    private RadioButton updaterbHourly, updaterbDaily, updaterbMonthly, updaterbYearly;
    private Button updateActDueDate, updateActTimeDue, updateBtnAddNotes, updateBtnEditNotes, updateBtnSaveNotes, updateBtnCreateTask;
    private String updateRadioText;
    private boolean updateSwitchChecked;
    private int updateTaskHour, updateTaskMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_form);
        // All fields that is positioned on the left the form
        updateActFormTitle = findViewById(R.id.updateActFormTitle);
        updateActFormRepeatTask = findViewById(R.id.updateActFormRepeatTask);
        updateActFormDueDate = findViewById(R.id.updateActFormDueDate);
        updateActFormTimeDue = findViewById(R.id.updateActFormTimeDue);
        updateActFormNotes = findViewById(R.id.updateActFormNotes);
        updateActNotesList = findViewById(R.id.updateActNotesList);

        // Fields for time and due date, position in the right of form
        updateActTimeDue = findViewById(R.id.updateActTimeDue);
        updateActDueDate = findViewById(R.id.updateActDue);

        // Field for the repeat task
        updateRgRepeatTask = findViewById(R.id.updateRgRepeatTask);
        updateActRepeat = findViewById(R.id.updateActRepeat);
        updaterbHourly = findViewById(R.id.updaterbHourly);
        updaterbDaily = findViewById(R.id.updaterbDaily);
        updaterbMonthly = findViewById(R.id.updaterbMonthly);
        updaterbYearly = findViewById(R.id.updaterbYearly);

        // Fields for Notes
        updateBtnAddNotes = findViewById(R.id.updateBtnAddNotes);
        // Fields for create task button
        updateBtnCreateTask = findViewById(R.id.updateBtnCreateTask);

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);

        // Get the Corresponding Attributes from Recyclerview
        Intent intent = getIntent();
        int actId = intent.getIntExtra(ACTIVITY_UPDATE_ID, -1);
        System.out.println(actId);
        Boolean actCheckedBox = getIntent().getExtras().getBoolean("actCheckBox");
        String actTitle = intent.getStringExtra("actTitle");
        String actCurrTime = intent.getStringExtra("actCurrTime");
        Boolean actSwitch = getIntent().getExtras().getBoolean("actSwitch");
        String actDue = intent.getStringExtra("actDue");
        String actRepeat = intent.getStringExtra("actRepeat");
        String actNotes = intent.getStringExtra("actNotes");

        updateActFormTitle.setText(actTitle);
        updateActTimeDue.setText(actCurrTime);
        updateActDueDate.setText(actDue);
        updateActNotesList.setText(actNotes);

        /**
         * Activity Time Reminder Picker
         * if the created task will remind the user when the due date is near
         */
        updateActTimeDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateActivityForm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                // get the value of hourOfDay and minute, then store it in taskHour and taskMinute
                                updateTaskHour = hourOfDay;
                                updateTaskMinute = minute;
                                // use the calendar to set the time and minute
                                calendar.set(0, 0, 0, updateTaskHour, updateTaskMinute);
                                // set selected time and on text view
                                updateActTimeDue.setText(DateFormat.format("hh:mm:a", calendar));
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
        updateActDueDate.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivityForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        // Date Format in the text view
                        String date = day+"/"+month+"/"+year;
                        // set the format in the text view
                        calendar.set(year, month, day);
                        updateActDueDate.setText(DateFormat.format("MMMM-dd-yyyy", calendar));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        /**
         * Repeat Task Picker
         * if the create activity will be repeated
         */
        ThemeSwitcherActivity themeSwitcherActivity = new ThemeSwitcherActivity(UpdateActivityForm.this);
        SharedPreferences shared = getSharedPreferences("MODE", MODE_PRIVATE);
        boolean isOn = shared.getBoolean("appTheme", themeSwitcherActivity.isOn());
        System.out.println(isOn);

        if(isOn){
            updaterbHourly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            updaterbHourly.setTextColor(UpdateActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            updaterbDaily.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            updaterbDaily.setTextColor(UpdateActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            updaterbMonthly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            updaterbMonthly.setTextColor(UpdateActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));
            updaterbYearly.setBackground(getResources().getDrawable(R.drawable.radio_repeat_selector_night));
            updaterbYearly.setTextColor(UpdateActivityForm.this.getResources().getColorStateList(R.drawable.radio_repeat_text_selector_night));

        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        updateActRepeat.setChecked(actSwitch);
        updateSwitchChecked = actSwitch;
        if(actSwitch){
            switch (actRepeat){
                case "Hourly":
                    updaterbHourly.setChecked(true);
                    break;
                case "Daily":
                    updaterbDaily.setChecked(true);
                    break;
                case "Monthly":
                    updaterbMonthly.setChecked(true);
                    break;
                case "Yearly":
                    updaterbYearly.setChecked(true);
                    break;
            }
        }else{

        }
        updateActRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                // if switch is disabled, it will also disable the button
                if(isChecked == false){
                    updaterbHourly.setEnabled(false);
                    updaterbDaily.setEnabled(false);
                    updaterbMonthly.setEnabled(false);
                    updaterbYearly.setEnabled(false);
                    Toast.makeText(UpdateActivityForm.this, "Repeat Task Disabled", Toast.LENGTH_SHORT).show();

                    // if switch is enabled, it will also enabled the button
                }else {
                    updaterbHourly.setEnabled(true);
                    updaterbDaily.setEnabled(true);
                    updaterbMonthly.setEnabled(true);
                    updaterbYearly.setEnabled(true);
                    Toast.makeText(UpdateActivityForm.this, "Repeat Task Enabled", Toast.LENGTH_SHORT).show();

                }
            }
        });

        updateBtnAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesSettings();
            }
        });
        DatabaseHelper activitiesdb = new DatabaseHelper(UpdateActivityForm.this);
        updateBtnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if the switch is enabled
                if (updateSwitchChecked == false){
                    updateRadioText = "No Text";
                }else {
                    // get the selected value of the radio button from radio group
                    int selectedId = updateRgRepeatTask.getCheckedRadioButtonId();
                    // find the radio button by returned id
                    updateRadioActivityRepeatButton = (RadioButton) findViewById(selectedId);
                    // get the string value of selected radio button and store it in radioText
                    updateRadioText = updateRadioActivityRepeatButton.getText().toString();
                }
                Activities activities = new Activities(actId, actCheckedBox, updateActFormTitle.getText().toString(), updateActTimeDue.getText().toString(), updateSwitchChecked, updateRadioText, updateActDueDate.getText().toString(), updateActNotesList.getText().toString());
                activitiesdb.updateOne(activities);

                Intent intent = new Intent(UpdateActivityForm.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void notesSettings(){

        updateActNotes = findViewById(R.id.updateActNotes);
        updateBtnSaveNotes = findViewById(R.id.updateBtnSave);
        updateBtnEditNotes = findViewById(R.id.updateBtnEdit);

        if(!updateActNotesList.getText().toString().equals("")){
            updateActNotes.setText(updateActNotesList.getText().toString());
            updateActNotesList.setText("");
        }

        updateActFormNotes.setVisibility(View.GONE);
        updateActNotes.setVisibility(View.VISIBLE);
        updateBtnAddNotes.setVisibility(View.GONE);
        updateBtnEditNotes.setVisibility(View.GONE);
        updateBtnSaveNotes.setVisibility(View.VISIBLE);

        updateBtnEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!updateActNotesList.getText().toString().equals("")){
                    updateActNotes.setText(updateActNotesList.getText().toString());
                    updateActNotesList.setText("");
                }
                updateActFormNotes.setVisibility(View.GONE);
                updateActNotes.setVisibility(View.VISIBLE);
                updateBtnAddNotes.setVisibility(View.GONE);
                updateBtnEditNotes.setVisibility(View.GONE);
                updateBtnSaveNotes.setVisibility(View.VISIBLE);
            }
        });

        updateBtnSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyPad();
                if (!updateActNotes.getText().toString().equals("")){
                    updateActNotesList.setText(updateActNotes.getText().toString());
                    updateActNotes.setText("");
                }
                if (!updateActNotesList.getText().toString().equals("")){
                    updateBtnAddNotes.setVisibility(View.GONE);
                    updateBtnEditNotes.setVisibility(View.VISIBLE);
                }else{
                    updateBtnAddNotes.setVisibility(View.VISIBLE);
                    updateBtnEditNotes.setVisibility(View.GONE);
                }
                updateActFormNotes.setVisibility(View.VISIBLE);
                updateActNotesList.setVisibility(View.VISIBLE);
                updateActNotes.setVisibility(View.GONE);
                updateBtnSaveNotes.setVisibility(View.GONE);
            }
        });

        updateActNotesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!updateActNotesList.getText().toString().equals("")){
                    updateActNotes.setText(updateActNotesList.getText().toString());
                    updateActNotesList.setText("");
                }
                updateActFormNotes.setVisibility(View.GONE);
                updateActNotes.setVisibility(View.VISIBLE);
                updateBtnAddNotes.setVisibility(View.GONE);
                updateBtnEditNotes.setVisibility(View.GONE);
                updateBtnSaveNotes.setVisibility(View.VISIBLE);
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