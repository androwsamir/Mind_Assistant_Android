package com.example.mindassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mindassistant.Database.MindAssistantDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class Tasks extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter adapter;
    private FloatingActionButton fab;
    public static String tableName;
    CheckBox checkBox;


    private ArrayList<ToDoModel> taskList;

    MindAssistantDB db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Intent intent = getIntent();
        tableName = intent.getExtras().getString("tablename");

        Toast.makeText(this, tableName, Toast.LENGTH_SHORT).show();

        //addNewTask = new AddNewTask();
        //addNewTask.setTableName(tablename);
        //adapter.setTableName(tablename);
        //addNewTask.setTableName(tablename);

        taskList = new ArrayList<ToDoModel>();
        fab = findViewById(R.id.fab);

        db = new MindAssistantDB(this);
        db.openDatabase();

        //to hide the navigation bar
        getSupportActionBar().hide();
        //define the RecyclerView
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(db ,Tasks.this);
        tasksRecyclerView.setAdapter(adapter);

        if(tableName.equals("Completed")){
            fab.hide();
        }
        else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                }
            });
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.Get_lists_content(tableName);

        Collections.reverse(taskList);
        adapter.setTasks(taskList);



    }

    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
        taskList = db.Get_lists_content(tableName);
        //new tasks are on the top
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
        //update the recyclerView
        adapter.notifyDataSetChanged();
    }

    public String getTablename() {
        return this.tableName;
    }
}