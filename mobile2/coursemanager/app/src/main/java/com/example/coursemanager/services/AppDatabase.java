package com.example.coursemanager.services;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;

@Database(entities = {Course.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
}