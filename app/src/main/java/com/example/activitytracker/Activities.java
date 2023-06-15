package com.example.activitytracker;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Activities implements Serializable {
    private int activityId;
    private boolean activityChecked;
    private String activityTitle;
    private String activityCurrTime;
    private boolean activitySwitchChecked;
    private String activityRepeat;
    private String activityDueDate;
    private String activityNotes;
    

    public Activities(int activityId, boolean activityChecked, String activityTitle, String activityCurrTime, boolean activitySwitchChecked, String activityRepeat, String activityDueDate, String activityNotes) {
        this.activityId = activityId;
        this.activityChecked = activityChecked;
        this.activityTitle = activityTitle;
        this.activityCurrTime = activityCurrTime;
        this.activitySwitchChecked = activitySwitchChecked;
        this.activityRepeat = activityRepeat;
        this.activityDueDate = activityDueDate;
        this.activityNotes = activityNotes;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public boolean isActivityChecked() {
        return activityChecked;
    }

    public void setActivityChecked(boolean activityChecked) {
        this.activityChecked = activityChecked;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityCurrTime() {
        return activityCurrTime;
    }

    public void setActivityCurrTime(String activityCurrTime) {
        this.activityCurrTime = activityCurrTime;
    }

    public boolean isActivitySwitchChecked() {
        return activitySwitchChecked;
    }

    public void setActivitySwitchChecked(boolean activitySwitchChecked) {
        this.activitySwitchChecked = activitySwitchChecked;
    }

    public String getActivityRepeat() {
        return activityRepeat;
    }

    public void setActivityRepeat(String activityRepeat) {
        this.activityRepeat = activityRepeat;
    }

    public String getActivityDueDate() {
        return activityDueDate;
    }

    public void setActivityDueDate(String activityDueDate) {
        this.activityDueDate = activityDueDate;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
    }

    @Override
    public String toString() {
        return "Activities{" +
                "activityId=" + activityId +
                ", activityChecked=" + activityChecked +
                ", activityTitle='" + activityTitle + '\'' +
                ", activityCurrTime='" + activityCurrTime + '\'' +
                ", activitySwitchChecked=" + activitySwitchChecked +
                ", activityRepeat='" + activityRepeat + '\'' +
                ", activityDueDate='" + activityDueDate + '\'' +
                ", activityNotes='" + activityNotes + '\'' +
                '}';
    }
}
