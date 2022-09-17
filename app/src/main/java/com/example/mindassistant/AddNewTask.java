package com.example.mindassistant;

import static android.app.Activity.RESULT_OK;
import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mindassistant.Database.MindAssistantDB;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNewTask extends BottomSheetDialogFragment
{
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText ;
    private Button saveButton  ;
    private Calendar cal = Calendar.getInstance();
    private MindAssistantDB db ;
    private Button reminderButton,repeatButton;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int hours,minutes;
    private String userTime,userDate;
    private int hoursf,minutesf,yearf,monthf,dayf;

    /*public void setTableName(String tableName) {
        this.tableName = tableName;
    }*/

    public static AddNewTask newInstance()
    {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle saveButton)
    {
        View view =inflater.inflate(R.layout.new_task,container,false );
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }



    @Override
    public void onViewCreated(View view , Bundle savedInstanceState)
    {
        super.onViewCreated(view , savedInstanceState);
        Tasks tasks = new Tasks();
        //define the variables created above
        newTaskText = getView().findViewById(R.id.newTaskText);
        saveButton =getView().findViewById(R.id.newTaskButton);
        saveButton.setEnabled(false);

        db = new MindAssistantDB(getActivity());
        db.openDatabase();
        //check if we are trying to update or create a new task depending on that different fn on database is executed
        Boolean isUpdated = false ;
        final Bundle bundle = getArguments();
        if( bundle!=null)
        {
            isUpdated = true ;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            //check if the task is empty or not

        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().equals("") || s.toString().equals(" "))
                {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                    Toast.makeText(getContext(), "Task is empty !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Boolean finalIsUpdated = isUpdated;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdated)
                {
                    db.updateTask(tasks.getTablename(),bundle.getInt("id"),text);
                }
                else
                {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    task.setFavorite(0);
                    task.setTime(hoursf + ":" + minutesf);
                    task.setDate(yearf + "-" + monthf + "-" + dayf);
                    if(tasks.getTablename().equals("Important")){
                        task.setFavorite(1);
                        db.insert_list_content("Tasks", task);
                        //Toast.makeText(tasks, s, Toast.LENGTH_SHORT).show();
                    }
                    //System.out.println(tasks.getTablename());
                    db.insert_list_content(tasks.getTablename(), task);
                }
                dismiss();
            }
        });

        reminderButton = getView().findViewById(R.id.Reminder);
        reminderButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), Pop_Reminder.class),200);
                        /*selectDate();
                        selectTime();

                         */
                    }
                });
    }
    /*private void selectTime()
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minuts) {
                userTime = hours + ":" + minuts;
                reminderButton.setText(FormatTime(hours, minuts));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
    private void selectDate()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                userDate = day + "-" + (month+1) + "-" + year;
                reminderButton.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
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

     */

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK)
        {

        }
        else if (requestCode == 200 && resultCode == RESULT_OK)
        {
            String time = data.getExtras().getString("time");
            String date = data.getExtras().getString("date");
            //Toast.makeText(getActivity(), "your time is " + time + "\n" + "and your date is " + date, Toast.LENGTH_LONG).show();

            String[] times=time.split(":");
            String[] dates=date.split("-");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            int day = Integer.parseInt(dates[2]);
            int month = Integer.parseInt(dates[1]);
            int year = Integer.parseInt(dates[0]);
            hoursf = hour;
            minutesf = minute;
            yearf = year;
            monthf = month;
            dayf = day;

            Calendar c = Calendar.getInstance();
            c.set(yearf, monthf, dayf, hoursf, minutesf);

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(getActivity(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        Activity ac= getActivity();
        if(ac instanceof DialogCloseListener)
        {
            ((DialogCloseListener)ac).handleDialogClose(dialog);
        }

    }
}
