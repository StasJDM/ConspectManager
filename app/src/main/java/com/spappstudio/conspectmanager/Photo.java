package com.spappstudio.conspectmanager;

public class Photo {

    public int id;
    public int conspect_id;
    public int number;
    public String path_to_img;
    public String note;

    public Photo(int id, int conspect_id, int number, String path_to_img, String note) {
        this.id = id;
        this.conspect_id = conspect_id;
        this.number = number;
        this.path_to_img = path_to_img;
        this.note = note;
    }
}
