package com.spappstudio.conspectmanager.objects;

public class CheckListItem {

    public int id;
    public String text;
    public Boolean isChecked;

    public CheckListItem(String text, int isChecked) {
        this.id = -1;
        this.text= text;
        this.isChecked = isChecked == 1;
    }

    public CheckListItem(int id, String text, int isChecked) {
        this.id = id;
        this.text= text;
        this.isChecked = isChecked == 1;
    }
}
