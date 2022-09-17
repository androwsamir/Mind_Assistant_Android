package com.example.mindassistant.Database;

public class Lists {
    private int id;
    private String text;
    private int img;

    public Lists(String text, int img){
        this.text = text;
        this.img = img;
    }

    public Lists(int id, String text, int img) {
        this.id = id;
        this.text = text;
        this.img = img;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

}
