package com.example.simpletodo;

import java.util.Date;

public class TaskItem {
    private String description;
    private Date datetime;
    private boolean isDone;

    private String classLabel;

    public TaskItem(String description, Date datetime, String classLabel) {
        this.description = description;
        this.datetime = datetime;
        this.isDone = false;
        this.classLabel = classLabel;

    }


    public String getDescription() {
        return description;
    }

    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
    }
    public Date getDatetime() {
        return datetime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


}
