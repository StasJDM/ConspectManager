package com.spappstudio.conspectmanager.objects;

public class Conspect {

    public int id;
    public int n_photos;
    public String name;
    public String subject;
    public String date;
    public String about;
    public String first_image_path;

    public Conspect(int id, int n_photos, String name, String subject, String date, String about, String first_image_path) {
        this.id = id;
        this.n_photos = n_photos;
        this.name = name;
        this.subject = subject;
        this.date = date;
        this.about = about;
        this.first_image_path = first_image_path;
    }
}
