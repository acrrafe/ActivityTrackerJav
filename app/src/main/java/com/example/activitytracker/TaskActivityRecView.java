package com.example.activitytracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskActivityRecView extends RecyclerView.Adapter<TaskActivityRecView.ViewHolder>{

    public static final String ACTIVITY_UPDATE_ID = "actId";

    Context mContext;
    private ArrayList<Activities> activities = new ArrayList<>();

    public TaskActivityRecView (Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activities_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.activityTitle.setText(activities.get(position).getActivityTitle());


            try {
                holder.actDueDate.setText(activities.get(position).getActivityDueDate());
                String dueDate = activities.get(position).getActivityDueDate();
                String dueTime = activities.get(position).getActivityCurrTime();
                // Date and Time Format
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("hh:mm:a");
                SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("MMMM-dd-yyyy");

                String currentTime = simpleDateFormatTime.format(calendar.getTime());
                String currentDate = simpleDateFormatDate.format(calendar.getTime());
                Date strDate = simpleDateFormatDate.parse(dueDate);
                Date strDates = simpleDateFormatDate.parse(currentDate);
                Date strTime = simpleDateFormatTime.parse(currentTime);
                Date strTimes = simpleDateFormatTime.parse(dueTime);

                // Check if the set Date and Time is already reach the due date
                if((strDate.compareTo(strDates) == 0) && (strTime.compareTo(strTimes) == 0 || strTimes.compareTo(strTime) < 0)){
                    holder.actDueDate.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                }else if(strDate.compareTo(strDates) < 0){
                    holder.actDueDate.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                }else{
                    ThemeSwitcherActivity themeSwitcherActivity = new ThemeSwitcherActivity(mContext);
                    SharedPreferences shared = mContext.getSharedPreferences("MODE", Context.MODE_PRIVATE);
                    boolean isOn = shared.getBoolean("appTheme", themeSwitcherActivity.isOn());
                    if(isOn){
                        holder.actDueDate.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    }else{
                        holder.actDueDate.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    }
                }
                notifyDataSetChanged();
            }catch (Exception e){

        }

        //Check Box Code
        holder.actvityCheckbox.setOnCheckedChangeListener(null);
        holder.actvityCheckbox.setChecked(activities.get(position).isActivityChecked());
        holder.actvityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.actvityCheckbox.isChecked()){
                    // Your checkbox last state
                    holder.actvityCheckbox.setSelected(activities.get(position).isActivityChecked());
                    DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                    databaseHelper.completeAct(1, activities.get(position).getActivityDueDate());
                    databaseHelper.deleteOne(activities.get(position));
                    activities.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Activity is Complete!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UpdateActivityForm.class);
                intent.putExtra(ACTIVITY_UPDATE_ID, activities.get(position).getActivityId());
                intent.putExtra("actCheckBox", activities.get(position).isActivityChecked());
                intent.putExtra("actTitle", activities.get(position).getActivityTitle());
                intent.putExtra("actCurrTime", activities.get(position).getActivityCurrTime());
                intent.putExtra("actSwitch", activities.get(position).isActivitySwitchChecked());
                intent.putExtra("actDue", activities.get(position).getActivityDueDate());
                intent.putExtra("actRepeat", activities.get(position).getActivityRepeat());
                intent.putExtra("actNotes", activities.get(position).getActivityNotes());
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return activities.size();
    }
    public void setActivity(ArrayList<Activities> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initialize Fields
        private CardView parent;
        private CheckBox actvityCheckbox;
        private TextView activityTitle, actDueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Call the Fields
            actvityCheckbox = itemView.findViewById(R.id.activityCheckBox);
            activityTitle = itemView.findViewById(R.id.activityTitle);
            actDueDate = itemView.findViewById(R.id.actDueDate);
            parent = itemView.findViewById(R.id.parent);
        }
    }

}
