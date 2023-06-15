package com.example.activitytracker;

public class ActivityEnd {
    private int id;
    private int numAct;
    private String dateEndAct;

    public ActivityEnd(int id, int numAct, String dateEndAct) {
        this.id = id;
        this.numAct = numAct;
        this.dateEndAct = dateEndAct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumAct() {
        return numAct;
    }

    public void setNumAct(int numAct) {
        this.numAct = numAct;
    }

    public String getDateEndAct() {
        return dateEndAct;
    }

    public void setDateEndAct(String dateEndAct) {
        this.dateEndAct = dateEndAct;
    }
}
