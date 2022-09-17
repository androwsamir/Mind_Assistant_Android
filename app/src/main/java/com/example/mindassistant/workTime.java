package com.example.mindassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindassistant.Database.MindAssistantDB;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class workTime extends AppCompatActivity {
    TextView H;
    TextView M;
    TextView S;
    EditText shortBreak;
    EditText LongBreak;
    EditText workM;
    AppCompatButton start;
    AppCompatButton reset;
    int count=0,sl=0;
    public static String tableName;
    MindAssistantDB db;
    private long duration = 0;
    private boolean timerRunning = false ;
    int x=0,m;
    int sh,lo;
    boolean flag =false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time);
        Intent intent = getIntent();
        tableName = intent.getExtras().getString("tablename");
        H = findViewById(R.id.hours);
        M = findViewById(R.id.min);
        S = findViewById(R.id.Sec);
        workM=(EditText) findViewById(R.id.M1);
        shortBreak=(EditText)findViewById(R.id.Short);
        LongBreak=(EditText)findViewById(R.id.Long);
        Toast.makeText(this, workM.getText()+"", Toast.LENGTH_SHORT).show();
        start = findViewById(R.id.Start);
        reset = findViewById(R.id.Reset);
        findViewById(R.id.Start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    String s = start.getText().toString();
                    Toast.makeText(workTime.this, s + "", Toast.LENGTH_SHORT).show();


                    sh = Integer.parseInt(shortBreak.getText().toString());
                    lo = Integer.parseInt(LongBreak.getText().toString());
                    // sh *= 60;
                    // lo *= 60;
                    m = Integer.parseInt(workM.getText().toString());
                    // m *= 60;
                    if (count % 2 == 0) {
                        duration = m;
                    } else {
                        sl++;
                        if (sl % 3 == 0) {
                            duration = lo;
                        } else {
                            duration = sh;
                        }
                    }
                    if (!timerRunning ) {
                        timerRunning = true;
                        start.setVisibility(View.INVISIBLE);
                        reset.setVisibility(View.VISIBLE);
                        new CountDownTimer(duration * 1000, 1000) {
                            @Override
                            public void onTick(long millisUnitilFinished) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                     int x=reset.getVisibility();

                                        Toast.makeText(workTime.this, x+"", Toast.LENGTH_SHORT).show();
                                        if (!flag) {

                                        String time = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUnitilFinished),
                                                TimeUnit.MILLISECONDS.toMinutes(millisUnitilFinished) -
                                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUnitilFinished)),
                                                TimeUnit.MILLISECONDS.toSeconds(millisUnitilFinished) -
                                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUnitilFinished)), Locale.getDefault());
                                        String[] hms = time.split(":");
                                        H.setText(hms[0]);
                                        M.setText(hms[1]);
                                        S.setText(hms[2]);
                                    }
                                      //  flag=true;
                                    }
                                });
                            }

                            @Override
                            public void onFinish() {
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                Intent i = new Intent(workTime.this, AlertReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(workTime.this, 0, i, 0);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                                count++;
                                timerRunning = false;

                                start.callOnClick();
                            }
                        }.start();
                       // flag=true;
                    }


                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
               // int x= reset.getVisibility();
                //Toast.makeText(workTime.this, x+"", Toast.LENGTH_SHORT).show();
                reset.setVisibility(View.INVISIBLE);
                 //x= reset.getVisibility();
                //Toast.makeText(workTime.this, x+"", Toast.LENGTH_SHORT).show();
                start.setVisibility(View.VISIBLE);
                H.setText("00");
                M.setText("00");
                S.setText("00");
                workM.setText("00");
                shortBreak.setText("00");
                LongBreak.setText("00");
                timerRunning=false;
            }
        });
    }
}