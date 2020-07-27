package com.spappstudio.conspectmanager;

public class Conspect {

    public int id;
    public int n_photos;
    public String name;
    public String subject;
    public String date;
    public String about;

    public Conspect(int id, int n_photos, String name, String subject, String date, String about) {
        this.id = id;
        this.n_photos = n_photos;
        this.name = name;
        this.subject = subject;
        this.date = date;
        this.about = about;
    }
}
