package com.example.mindassistant;

public class importantCompletedModel {
    private int id, status, favorite;
    private String task, time, date, repeat, tableId, tableName;


    public importantCompletedModel() {
    }

    public importantCompletedModel(int id, String task, String time, String date, String repeat, int status, int favorite, String tableId, String tableName) {
        this.id = id;
        this.task = task;
        this.time = time;
        this.date = date;
        this.repeat = repeat;
        this.status = status;
        this.favorite = favorite;
        this.tableId = tableId;
        this.tableName = tableName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
