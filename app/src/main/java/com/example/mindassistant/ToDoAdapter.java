package com.example.mindassistant;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mindassistant.Database.MindAssistantDB;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>
{
    private ArrayList<ToDoModel> toDoList = new ArrayList<ToDoModel>();
    private ArrayList<importantCompletedModel> completedList = new ArrayList<importantCompletedModel>();
    private ArrayList<importantCompletedModel> Important = new ArrayList<importantCompletedModel>();
    private Tasks activity ;
    private MindAssistantDB db ;
    private Tasks tasks = new Tasks();



    /*public void setTableName(String tableName) {
        this.tableName = tableName;
    }*/

    public ToDoAdapter(MindAssistantDB db , Tasks activity) {
        this.activity = activity;
        this.db=db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent , false);
        return new ViewHolder(itemView);
    }

    public void deleteItem(int pos)
    {
        ToDoModel item = toDoList.get(pos);
        db.deleteTask(tasks.getTablename(),item.getId());
        toDoList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        db.openDatabase();
        ToDoModel item = toDoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.fav.setChecked(toBoolean(item.getFavorite()));

        //whenever we check or uncheck any task we need to update that is our database

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                completedList = db.Get_ImportantCompleted_content("Completed");
                if(compoundButton.isChecked())
                {
                    db.updateStatus(tasks.getTablename(),item.getId(),1);
                    boolean flag = false;
                    int id = 0;
                    for(int i =0 ;i<completedList.size();i++){
                        if(String.valueOf(item.getId()).equals(completedList.get(i).getTableId()) && completedList.get(i).getTableName().equals(tasks.getTablename())){
                            id = completedList.get(i).getId();
                            flag=true;
                            break;
                        }
                    }
                    if(!flag) {
                        importantCompletedModel item1 = new importantCompletedModel();
                        item1.setTableId(String.valueOf(item.getId()));
                        item1.setStatus(1);
                        item1.setDate(item.getDate());
                        item1.setTime(item.getTime());
                        item1.setRepeat(item.getRepeat());
                        item1.setTask(item.getTask());
                        item1.setTableName(tasks.getTablename());
                        item1.setFavorite(item.getFavorite());
                        db.insertImportantCompleted("Completed", item1);
                        completedList = db.Get_ImportantCompleted_content("Completed");
                        for (int i = 0; i < completedList.size(); i++) {
                            db.updateStatus("Completed", item.getId(), 1);
                        }
                    }
                }
                else
                {
                    db.updateStatus(tasks.getTablename(),item.getId(),0);
                    int id = 0;
                    for(int i =0 ;i<completedList.size();i++){
                        if(String.valueOf(item.getId()).equals(completedList.get(i).getTableId()) && completedList.get(i).getTableName().equals(tasks.getTablename())){
                            id = completedList.get(i).getId();
                            //Toast.makeText(activity, id+"", Toast.LENGTH_SHORT).show();
                            db.deleteTask("Completed", id);
                            break;
                        }
                    }

                }
            }
        });

        holder.fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Important = db.Get_ImportantCompleted_content("Important");
                if (buttonView.isChecked()) {
                    db.updateFavorite(tasks.getTablename(), item.getId(), 1);
                    boolean flag = false;
                    int id = 0;
                    for(int i =0 ;i<Important.size();i++){
                        if(String.valueOf(item.getId()).equals(Important.get(i).getTableId()) && Important.get(i).getTableName().equals(tasks.getTablename())){
                            id = Important.get(i).getId();
                            flag=true;
                            break;
                        }
                    }
                    if(!flag) {
                        importantCompletedModel item1 = new importantCompletedModel();
                        item1.setTableId(String.valueOf(item.getId()));
                        item1.setStatus(item.getStatus());
                        item1.setDate(item.getDate());
                        item1.setTime(item.getTime());
                        item1.setRepeat(item.getRepeat());
                        item1.setTask(item.getTask());
                        item1.setTableName(tasks.getTablename());
                        item1.setFavorite(1);
                        db.insertImportantCompleted("Important",item1);
                        /*Important = db.Get_ImportantCompleted_content("Important");
                        for (int i = 0; i < Important.size(); i++) {
                            db.updateFavorite("Important", Important.get(i).getId(), 1);
                        }*/
                    }

                }
                else {
                    db.updateFavorite(tasks.getTablename(),item.getId(),0);
                    int id = 0;
                    for(int i =0 ;i<Important.size();i++){
                        if(String.valueOf(item.getId()).equals(Important.get(i).getTableId()) && Important.get(i).getTableName().equals(tasks.getTablename())){
                            id = Important.get(i).getId();
                            //Toast.makeText(activity, id+"", Toast.LENGTH_SHORT).show();
                            db.deleteTask("Important", id);
                            break;
                        }
                    }
                }
            }
        });
    }

    private boolean toBoolean(int n )
    {
        return n!=0;
    }

    public void setTasks(ArrayList<ToDoModel> toDoList)
    {
        this.toDoList = toDoList;
        //to update the recyclerView
        notifyDataSetChanged();
        Toast.makeText(this.activity, "task is added", Toast.LENGTH_SHORT).show();
    }

    public void editItem(int pos)
    {
        ToDoModel item = toDoList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask f = new AddNewTask();
        f.setArguments(bundle);
        f.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }

    @Override
    public int getItemCount()
    {
        return toDoList.size();
    }

    public Context getContext() {
        return activity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox task ;
        ToggleButton fav;

        public ViewHolder( View itemView) {
            super(itemView);
            task=itemView.findViewById(R.id.todoCheckBox);
            fav=itemView.findViewById(R.id.favoriteBtn);
        }

    }

}
