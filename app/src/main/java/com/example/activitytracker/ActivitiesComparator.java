package com.example.activitytracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ActivitiesComparator implements Comparator<Activities> {
    @Override
    public int compare(Activities activities, Activities t1) {

        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("MMMM-dd-yyyy");
        Date strDate = null, strDate2 = null;
        try {
             strDate = simpleDateFormatDate.parse(activities.getActivityDueDate().toString());
             strDate2 = simpleDateFormatDate.parse(t1.getActivityDueDate().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (strDate.compareTo(strDate2)); // Compare each activities due dates

    }
}


