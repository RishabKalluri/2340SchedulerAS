package com.example.coursemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private TaskDao taskDao;
    private View.OnClickListener onEditClickListener;
    private View.OnClickListener onDeleteClickListener;

    public TaskAdapter(List<Task> taskList, TaskDao taskDao, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
        this.taskList = new ArrayList<>();
        this.taskDao = taskDao;
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

        holder.deleteButton.setTag(position);
        holder.editButton.setTag(position);
        holder.editButton.setOnClickListener(onEditClickListener);

        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Task taskToDelete = taskList.get(currentPosition);
            taskDao.deleteTask(taskToDelete);
            taskList.remove(currentPosition);
            notifyItemRemoved(currentPosition);
        });

        holder.editButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Task taskToEdit = taskList.get(currentPosition);
            onEditClickListener.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, dueDate;
        public Button deleteButton, editButton;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.task_name);
            dueDate = view.findViewById(R.id.due_date);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
        }
    }
}