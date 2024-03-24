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
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private static View.OnClickListener onEditClickListener;
    private View.OnClickListener onDeleteClickListener;
    private TaskDao taskDao;

    public TaskAdapter(List<Task> taskList, TaskDao taskDao, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
        this.taskList = taskList;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.taskDao = taskDao;
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
        holder.courseId.setText("Course ID: " + task.getCourseId());

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

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, dueDate, courseId;
        public Button deleteButton, editButton;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.task_name);
            dueDate = view.findViewById(R.id.due_date);
            courseId = view.findViewById(R.id.course_id);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
            editButton.setOnClickListener(onEditClickListener);
        }
    }
}