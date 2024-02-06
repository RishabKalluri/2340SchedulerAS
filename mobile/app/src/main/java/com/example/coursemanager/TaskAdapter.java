package com.example.coursemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemanager.services.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (taskList != null && !taskList.isEmpty()) {
            Task task = taskList.get(position);
            holder.taskName.setText(task.getTaskName());
            // Bind other task details to the UI elements here
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    // Method to update the list and refresh the RecyclerView
    public void updateTasks(List<Task> tasks) {
        taskList = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;

        public TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.taskName);
            // Initialize other views here
        }
    }
}
