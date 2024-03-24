package com.example.coursemanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private List<Course> courseList;
    private TaskDao taskDao;
    private CourseDao courseDao;
    private View.OnClickListener onEditClickListener;
    private View.OnClickListener onDeleteClickListener;

    public TaskAdapter(List<Task> taskList, List<Course> courseList, TaskDao taskDao, CourseDao courseDao, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
        this.taskList = taskList;
        this.courseList = courseList;
        this.taskDao = taskDao;
        this.courseDao = courseDao;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setTasks(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText("Task: " + task.getTaskName());
        holder.dueDate.setText("Due Date: " + task.getDueDate());

        ArrayAdapter<Course> adapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, courseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.courseSpinner.setAdapter(adapter);

        int coursePosition = getCoursePosition(task.getCourseId());
        if (coursePosition != -1) {
            holder.courseSpinner.setSelection(coursePosition);
        }

        holder.deleteButton.setTag(position);
        holder.editButton.setTag(position);
        holder.editButton.setOnClickListener(onEditClickListener);

        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Task taskToDelete = taskList.get(currentPosition);

            Executors.newSingleThreadExecutor().execute(() -> {
                taskDao.deleteTask(taskToDelete);

                // Run on UI thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    taskList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                });
            });
        });
        holder.editButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Bundle bundle = new Bundle();
            Task taskToEdit = taskList.get(currentPosition);
            bundle.putInt("position", currentPosition);
            onEditClickListener.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private int getCoursePosition(int courseId) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getId() == courseId) {
                return i;
            }
        }
        return -1;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, dueDate;
        public Spinner courseSpinner;
        public Button deleteButton, editButton;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.task_name);
            dueDate = view.findViewById(R.id.due_date);
            courseSpinner = view.findViewById(R.id.course_spinner);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
        }
    }
}