package com.example.activitytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLSyntaxErrorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Initialize Constant variables
    private static final String ACTIVITY_TABLE = "ACTIVITY_TABLE";
    private static final String ACTIVITY_ID = "ACTIVITY_ID";
    private static final String ACTIVITY_CHECKED = "ACTIVITY_CHECKED";
    private static final String ACTIVITY_TITLE = "ACTIVITY_TITLE";
    private static final String ACTIVITY_CURR_TIME = "ACTIVITY_CURR_TIME";
    private static final String ACTIVITY_SWITCH = "ACTIVITY_SWITCH";
    private static final String ACTIVITY_REPEAT = "ACTIVITY_REPEAT";
    private static final String ACTIVITY_DUE = "ACTIVITY_DUE";
    private static final String ACTIVITY_NOTES = "ACTIVITY_NOTES";

    private static final String ACTIVITY_COMPLETE_TABLE = "ACTIVITY_COMPLETE_TABLE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "activities.db", null, 1);
    }
    /**
     * Declaration of the database when it is empty or first time to be created
     * In this method, we can see the format of sql when creating the table, "CREATE TABLE"
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String creationTable = "CREATE TABLE " + ACTIVITY_TABLE + " (" + ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACTIVITY_CHECKED + " BOOL," + ACTIVITY_TITLE + " TEXT," + ACTIVITY_CURR_TIME + " TEXT,"  + ACTIVITY_SWITCH + " BOOL," + ACTIVITY_REPEAT + " TEXT," + ACTIVITY_DUE + " TEXT," + ACTIVITY_NOTES + " TEXT)";
        String numActivitiesTable = "CREATE TABLE "+ACTIVITY_COMPLETE_TABLE+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, completeAct INTEGER, completeDate TEXT)";
        sqLiteDatabase.execSQL(numActivitiesTable);
        sqLiteDatabase.execSQL(creationTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + ACTIVITY_TABLE);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + ACTIVITY_COMPLETE_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean addOne(Activities activities){

        SQLiteDatabase studentsdb = this.getWritableDatabase(); // Use Writtable to write in database

        ContentValues cv = new ContentValues(); // Content Values is the catcher of the incoming data where it pass in the value to the Students class
        cv.put(ACTIVITY_CHECKED,  activities.isActivityChecked());
        cv.put(ACTIVITY_TITLE,  activities.getActivityTitle());
        cv.put(ACTIVITY_CURR_TIME,  activities.getActivityCurrTime());
        cv.put(ACTIVITY_SWITCH,  activities.isActivitySwitchChecked());
        cv.put(ACTIVITY_REPEAT,  activities.getActivityRepeat());
        cv.put(ACTIVITY_DUE,  activities.getActivityDueDate());
        cv.put(ACTIVITY_NOTES,  activities.getActivityNotes());

        // Here we check if the given values are null or not, if null we will return false, if not null we will write it in the database
        long insert = studentsdb.insert(ACTIVITY_TABLE, null, cv);
        if(insert == -1){
            return  false;
        }else {
            return true;
        }
    }
    public ArrayList<Activities> getEveryone(){

        ArrayList<Activities> activities = new ArrayList<>(); // Initialize an Students class Array

        String queryString = "SELECT * FROM " + ACTIVITY_TABLE; // sql logic from calling the contents in STUDENT_TABLE, save it in queryString
        SQLiteDatabase db = this.getReadableDatabase(); // use ReadableDatabase(); to read the database contents

        Cursor cursor =  db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){ // checker if the STUDENT_TABLE IS NULL
            do{
                // fields format here should be the same in the STUDENT_TABLE format
                int actId = cursor.getInt(0);
                boolean checkedBox = (cursor.getInt(1) == 1 ? true: false);
                String actTitle = cursor.getString(2);
                String currTime = cursor.getString(3);
                boolean switchChecked = (cursor.getInt(4) == 1 ? true: false);
                String actRepeat = cursor.getString(5);
                String actDue = cursor.getString(6);
                String actNotes = cursor.getString(7);

                Activities newStud = new Activities(actId, checkedBox, actTitle, currTime, switchChecked, actRepeat, actDue, actNotes); // everytime the cursor is loop, add it to the Students constructor
                activities.add(newStud); // then add the whole constructor data to the students ArrayList and repeat


            }while (cursor.moveToNext()); // loop will end if there's no next line in the database
        }else {

        }
        // Database and cursor must be close after it did its purpose
        cursor.close();
        db.close();
        return activities;
    }
    public boolean updateOne(Activities activities){
        SQLiteDatabase studentsdb = this.getWritableDatabase(); // Use Writtable to write in database
        ContentValues cv = new ContentValues(); // Content Values is the catcher of the incoming data where it pass in the value to the Students class
        String id = String.valueOf(activities.getActivityId());
        cv.put(ACTIVITY_CHECKED,  activities.isActivityChecked());
        cv.put(ACTIVITY_TITLE,  activities.getActivityTitle());
        cv.put(ACTIVITY_CURR_TIME,  activities.getActivityCurrTime());
        cv.put(ACTIVITY_SWITCH,  activities.isActivitySwitchChecked());
        cv.put(ACTIVITY_REPEAT,  activities.getActivityRepeat());
        cv.put(ACTIVITY_DUE,  activities.getActivityDueDate());
        cv.put(ACTIVITY_NOTES,  activities.getActivityNotes());

        studentsdb.update(ACTIVITY_TABLE, cv, "ACTIVITY_ID = ?", new String[] {id});
        return true;
    }

    public boolean deleteOne(Activities activities){
        SQLiteDatabase activitiesdb = this.getWritableDatabase();
        String queryString = " DELETE FROM " + ACTIVITY_TABLE + " WHERE " + ACTIVITY_ID + "=" + activities.getActivityId();
        Cursor cursor = activitiesdb.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }
    public boolean completeAct(int numAct, String date){
        int completeAct = 0;
        String completeDate = null;
        String queryNum = "SELECT * FROM " + ACTIVITY_COMPLETE_TABLE; // sql logic from calling the contents in STUDENT_TABLE, save it in queryString
        SQLiteDatabase db = this.getReadableDatabase(); // use ReadableDatabase(); to read the database contents
        Cursor cursor =  db.rawQuery(queryNum, null);

        if (cursor.moveToFirst()){ // checker if the STUDENT_TABLE IS NULL
            do{
                // fields format here should be the same in the STUDENT_TABLE format
                int actId = cursor.getInt(0);
                completeAct = Integer.parseInt(cursor.getString(1));
                completeDate = cursor.getString(2);


            }while (cursor.moveToNext()); // loop will end if there's no next line in the database
        }else {

        }
        cursor.close();
        db.close();

        SQLiteDatabase completedb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        completeAct += numAct;
        Calendar calendar = Calendar.getInstance();
        // We use the SimpleDateFormat to create a customize time
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("MM-yyyy");

        try {
            Date tableDate = simpleDateFormatDate.parse(date);
        }catch (ParseException e){

        }

        completeDate = date;
        cv.put("completeAct",completeAct);
        cv.put("completeDate", completeDate);
        System.out.println(completeAct);
        System.out.println(completeDate);

        long insert = completedb.insert(ACTIVITY_COMPLETE_TABLE, null, cv);
        if(insert == -1){
            return  false;
        }else {
            return true;
        }


    }
    public ArrayList<ActivityEnd> getDates(){

        ArrayList<ActivityEnd> activityEnd = new ArrayList<>(); // Initialize an Students class Array

        String queryString = "SELECT * FROM " + ACTIVITY_COMPLETE_TABLE; // sql logic from calling the contents in STUDENT_TABLE, save it in queryString
        SQLiteDatabase db = this.getReadableDatabase(); // use ReadableDatabase(); to read the database contents

        Cursor cursor =  db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){ // checker if the STUDENT_TABLE IS NULL
            do{
                // fields format here should be the same in the STUDENT_TABLE format
                int actId = cursor.getInt(0);
                int compAct = cursor.getInt(1);
                String compDateTab = cursor.getString(2);


                ActivityEnd compDate = new ActivityEnd(actId, compAct, compDateTab); // everytime the cursor is loop, add it to the Students constructor
                activityEnd.add(compDate); // then add the whole constructor data to the students ArrayList and repeat


            }while (cursor.moveToNext()); // loop will end if there's no next line in the database
        }else {

        }
        // Database and cursor must be close after it did its purpose
        cursor.close();
        db.close();
        return activityEnd;
    }
}
