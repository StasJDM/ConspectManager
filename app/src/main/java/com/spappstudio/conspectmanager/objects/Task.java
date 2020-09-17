package com.spappstudio.conspectmanager.objects;

import java.util.ArrayList;

public class Task {

    public int id;
    public String title;
    public String subject;
    public String dateOfCreate;
    public String deadline;
    public String text;
    public int checkListCount;
    public int conspectsCount;
    public Boolean isDone;

    private ArrayList<Conspect> conspects;
    private ArrayList<CheckListItem> checkList;

    public Task(int id,
                String title,
                String subject,
                String dateOfCreate,
                String deadline,
                String text,
                int checkListCount,
                int conspectsCount,
                int isDone) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.dateOfCreate = dateOfCreate;
        this.deadline = deadline;
        this.text = text;
        this.checkListCount = checkListCount;
        this.conspectsCount = conspectsCount;
        this.isDone = intToBool(isDone);
    }

    public void addCheckList(ArrayList<CheckListItem> checkList) {
        this.checkList = checkList;
    }

    public ArrayList<CheckListItem> getCheckList() {
        return checkList;
    }

    public void addConspects(ArrayList<Conspect> conspects) {
        this.conspects = conspects;
    }

    public ArrayList<Conspect> getConspects() {
        return conspects;
    }

    public int getIsDone() {
        return isDone ? 1 : 0;
    }

    private Boolean intToBool(int t) {
        return t == 1;
    }
}
