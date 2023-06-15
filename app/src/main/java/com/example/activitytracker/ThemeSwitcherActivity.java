package com.example.activitytracker;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeSwitcherActivity {
    private boolean isOn;
    private SharedPreferences sharedPreferences;
    Context mContext;

    public ThemeSwitcherActivity(Context context, boolean isOn) {
        this.isOn = isOn;
        this.mContext = context;

        sharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("appTheme", isOn);
        editor.commit();
    }
    public ThemeSwitcherActivity(Context context) {
        this.mContext = context;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
