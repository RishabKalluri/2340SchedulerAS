package com.example.coursemanager.services;

import java.util.Date;

public class Task {
    private String taskName;
    private Date dueDate;
    private String course;
    private boolean isComplete;

    public Task(String taskName, Date dueDate, String course) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.course = course;
        this.isComplete = false;
    }
    public String getTaskName() {
        return taskName;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public String getCourse() {
        return course;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}