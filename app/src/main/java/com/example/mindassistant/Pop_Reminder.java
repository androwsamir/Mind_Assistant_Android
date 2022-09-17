package com.example.mindassistant;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class Pop_Reminder extends Activity {
    String userTime,userDate;
    Button done;
    TextView t1,t2;
    TimePicker timePicker;
    DatePicker datePicker;
    int hours,minutes;
    String userTime24;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupreminder);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.9), (int)(height*0.9));

        t1 = (TextView) findViewById(R.id.timeshow);
        t2 = (TextView) findViewById(R.id.dateshow);
        timePicker = (TimePicker) findViewById(R.id.timepicker);
        timePicker.setIs24HourView(false);
        datePicker = (DatePicker) findViewById(R.id.datapicker);
        done = (Button) findViewById(R.id.done);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                userTime = FormatTime(hourOfDay, minute);
                userTime24 = hourOfDay + ":" + minute;
                t1.setText(userTime);
            }
        });
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userDate = year + "-" + monthOfYear + "-" + dayOfMonth;
                t2.setText(userDate);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.putExtra("time", userTime24);
                intent.putExtra("date", userDate);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });



    }

    public String FormatTime(int hour, int minute)
    {
        String time;
        time = "";
        String formattedMinute;
        if (minute / 10 == 0)
        {
            formattedMinute = "0" + minute;
        }
        else
        {
            formattedMinute = "" + minute;
        }
        if (hour == 0)
        {
            time = "12" + ":" + formattedMinute + " AM";
        }
        else if (hour < 12)
        {
            time = hour + ":" + formattedMinute + " AM";
        }
        else if (hour == 12)
        {
            time = "12" + ":" + formattedMinute + " PM";
        }
        else
        {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }
}
