package com.example.mindassistant.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mindassistant.ToDoModel;
import com.example.mindassistant.importantCompletedModel;

import java.util.ArrayList;

public class MindAssistantDB extends SQLiteOpenHelper {
    public static final String DatabaseName = "MindAssistant.db";
    private SQLiteDatabase db;

    public MindAssistantDB(Context con){
        super(con, DatabaseName, null, 1);
    }

    public MindAssistantDB(Context con, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(con, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table list (id INTEGER primary key AUTOINCREMENT, name Text, image INTEGER, checked INTEGER)");
        sqLiteDatabase.execSQL("create table MyDay (id INTEGER primary key AUTOINCREMENT,task Text, time Text, date Text, repeat Text, status INTEGER, favorite INTEGER)");
        sqLiteDatabase.execSQL("create table Important (id INTEGER primary key AUTOINCREMENT,task Text, time Text, date Text, repeat Text, status INTEGER, favorite INTEGER)"); //tableId Text, tableName Text)");
        sqLiteDatabase.execSQL("create table Completed (id INTEGER primary key AUTOINCREMENT,task Text, time Text, date Text, repeat Text, status INTEGER, favorite INTEGER)"); //tableId Text, tableName Text)");
        sqLiteDatabase.execSQL("create table Tasks (id INTEGER primary key AUTOINCREMENT,task Text, time Text, date Text, repeat Text, status INTEGER, favorite INTEGER)");
        sqLiteDatabase.execSQL("create table MyTherapy (id INTEGER primary key AUTOINCREMENT,task Text, time Text, date Text, repeat Text, status INTEGER, favorite INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS list");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MyDay");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Important");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Completed");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Tasks");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS allTasks");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MyTherapy");

            onCreate(sqLiteDatabase);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public String insert_list_names(String name, int image, int checked)
    {
        SQLiteDatabase s = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("checked", checked);
        long re = s.insert("list", null, values);  // if it failed to insert --> re=-1

        if(re == -1){
            return "Error";
        }
        else{
            return "Done";
        }
    }

    public String insert_list_content(String tableName, ToDoModel task)
    {
        SQLiteDatabase s = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", task.getTask());
        values.put("time", task.getTime());
        values.put("date", task.getDate());
        values.put("repeat", task.getRepeat());
        values.put("status", task.getStatus());
        values.put("favorite", task.getFavorite());
        //values.put("tableId", task.getTableId());
        //values.put("tableName", task.getTableName());

        long re = s.insert(tableName, null, values);  // if it failed to insert --> re=-1

        if(re == -1){
            return "Error";
        }
        else{
            return "Done";
        }
    }

    public String insertImportantCompleted(String tableName, importantCompletedModel task){
        SQLiteDatabase s = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", task.getTask());
        values.put("time", task.getTime());
        values.put("date", task.getDate());
        values.put("repeat", task.getRepeat());
        values.put("status", task.getStatus());
        values.put("favorite", task.getFavorite());
        values.put("tableId", task.getTableId());
        values.put("tableName", task.getTableName());

        long re = s.insert(tableName, null, values);  // if it failed to insert --> re=-1

        if(re == -1){
            return "Error";
        }
        else{
            return "Done";
        }
    }

   /*public String insert_pomodoro_content(Pomodoro pomodoro) {
        SQLiteDatabase s = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", pomodoro.getTask());
        values.put("time", pomodoro.getTime());
        values.put("breakTime", pomodoro.getBreakTime());
        long re = s.insert("WorkTime", null, values);

        if (re == -1) {
            return "Error";
        }
        else {
            return "Done";
        }
    }*/

    public ArrayList<Lists> Get_lists_names()
    {
        ArrayList<Lists> arr = new ArrayList<Lists>();
        SQLiteDatabase s=this.getReadableDatabase();
        Cursor c=s.rawQuery("select * from list",null);

        c.moveToFirst();
        while (c.isAfterLast()==false)
        {
            arr.add(new Lists(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2)));
            c.moveToNext();
        }
        return arr;
    }

    @SuppressLint("Range")
    public ArrayList<ToDoModel> Get_lists_content(String tableName)
    {
        ArrayList<ToDoModel> arr = new ArrayList<ToDoModel>();
        SQLiteDatabase s = this.getReadableDatabase();
        Cursor c = s.rawQuery("select * from "+tableName,null);

        c.moveToFirst();
        while (c.isAfterLast()==false)
        {
            arr.add(new ToDoModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getInt(5),
                    c.getInt(6)
                    /*c.getString(7),
                    c.getString(8)*/));

            c.moveToNext();
        }
        return arr;
    }

    public ArrayList<importantCompletedModel> Get_ImportantCompleted_content(String tableName)
    {
        ArrayList<importantCompletedModel> arr = new ArrayList<importantCompletedModel>();
        SQLiteDatabase s = this.getReadableDatabase();
        Cursor c = s.rawQuery("select * from "+tableName,null);

        c.moveToFirst();
        while (c.isAfterLast()==false)
        {
            arr.add(new importantCompletedModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getInt(5),
                    c.getInt(6),
                    c.getString(7),
                    c.getString(8)));

            c.moveToNext();
        }
        return arr;
    }

    /*public ArrayList<Pomodoro> Get_pomodoro_content(){
        ArrayList<Pomodoro> arr = new ArrayList<Pomodoro>();
        SQLiteDatabase s = this.getReadableDatabase();
        Cursor c = s.rawQuery("select * from WorkTime",null);

        c.moveToFirst();
        while (c.isAfterLast()==false)
        {
            arr.add(new Pomodoro(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3)));

            c.moveToNext();
        }
        return arr;
    }*/

    public void updateListName(int id, String name,String old){
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.update("list", contentValues, "id=?", new String[] {String.valueOf(id)});
        System.out.println(old +" "+name);
        db.execSQL("ALTER TABLE "+old+" RENAME TO "+name);
    }

    public void updateStatus(String table_name, int id, int status)
    {
        ContentValues v = new ContentValues();
        v.put("status",status);
        db.update(table_name,v,"id=?", new String []{String.valueOf(id)});
    }

    public void updateTask(String table_name, int id , String task)
    {
        ContentValues v = new ContentValues();
        v.put("task",task);
        db.update(table_name,v,"id=?", new String []{String.valueOf(id)});
    }

    public void deleteTask(String table_name, int id)
    {
        db.delete(table_name,"id=?", new String []{String.valueOf(id)});
    }

    public void updateFavorite(String table_name, int id, int favorite){
        ContentValues v = new ContentValues();
        v.put("favorite",favorite);
        db.update(table_name,v,"id=?", new String []{String.valueOf(id)});
    }

    public void deleteList(int id, String listName){
        openDatabase();
        db.delete("list","id=?", new String []{String.valueOf(id)});
        db.execSQL("DROP TABLE IF EXISTS "+listName);
    }

}
