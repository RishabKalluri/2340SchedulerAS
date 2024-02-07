package com.example.coursemanager.services;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface ExamDao {
    @Query("SELECT * FROM Exam")
    LiveData<List<Exam>> getAllExams();

    @Insert
    void insertExam(Exam exam);

    @Update
    void updateExam(Exam exam);

    @Delete
    void deleteExam(Exam exam);
}