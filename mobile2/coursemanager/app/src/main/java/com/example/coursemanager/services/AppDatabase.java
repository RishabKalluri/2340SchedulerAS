package com.example.coursemanager.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;

@Database(entities = {Course.class, Exam.class, Task.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract ExamDao examDao();
    public abstract TaskDao taskDao();
}