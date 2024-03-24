package com.example.coursemanager.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;

@Database(entities = {Course.class, Exam.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
    public abstract ExamDao examDao();
}