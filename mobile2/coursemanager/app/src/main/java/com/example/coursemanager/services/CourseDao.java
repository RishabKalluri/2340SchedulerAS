package com.example.coursemanager.services;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.coursemanager.services.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCourses();
    @Query("SELECT * FROM Course")
    List<Course> getAllCoursesSync();
    @Query("SELECT * FROM Course WHERE courseName = :name LIMIT 1")
    Course getCourseByName(String name);

    @Insert
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

}