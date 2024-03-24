package com.example.coursemanager.services;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Course.class,
        parentColumns = "id",
        childColumns = "courseId",
        onDelete = CASCADE))
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String taskName;
    private String dueDate;
    private int course;
    private boolean isComplete;

    public Task(String taskName, String dueDate, int courseId) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.course = courseId;
        this.isComplete = false;
    }

    public Task() {

    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int courseId) {
        this.course = courseId;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}