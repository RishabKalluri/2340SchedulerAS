package com.example.coursemanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.Exam;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;
import com.example.coursemanager.services.ExamDao;

import java.util.List;
import java.util.concurrent.Executors;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Exam> examList;
    private static View.OnClickListener onEditClickListener;
    private View.OnClickListener onDeleteClickListener;
    private ExamDao examDao;

    public ExamAdapter(List<Exam> examList, ExamDao examDao, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
        this.examList = examList;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.examDao = examDao;
    }
    public void setExams(List<Exam> examList) {
        this.examList = examList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);
        return new ExamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        holder.examName.setText("Exam: " + exam.getExamName());
        holder.courseName.setText("Course: " + exam.getCourseName());
        holder.time.setText("Time: " + exam.getTime());
        holder.date.setText("Date: " + exam.getDate());
        holder.location.setText("Location: " + exam.getLocation());

        holder.deleteButton.setTag(position);
        holder.editButton.setTag(position);
        holder.editButton.setOnClickListener(onEditClickListener);

        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Exam examToDelete = examList.get(currentPosition);

            Executors.newSingleThreadExecutor().execute(() -> {
                examDao.deleteExam(examToDelete);

                // Run on UI thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    examList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                });
            });
        });
        holder.editButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Bundle bundle = new Bundle();
            Exam examToEdit = examList.get(currentPosition);
            bundle.putInt("position", currentPosition);
            onEditClickListener.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        public TextView examName, courseName, time, date, location;
        public Button deleteButton, editButton;

        public ExamViewHolder(View view) {
            super(view);
            examName = view.findViewById(R.id.examName);
            courseName = view.findViewById(R.id.course_name);
            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
            location = view.findViewById(R.id.location);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
            editButton.setOnClickListener(onEditClickListener);
        }
    }
}