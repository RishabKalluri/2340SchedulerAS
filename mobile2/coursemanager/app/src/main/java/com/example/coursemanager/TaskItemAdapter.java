package com.example.simpletodo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.simpletodo.TaskItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskItemAdapter extends ArrayAdapter<TaskItem> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public TaskItemAdapter(Context context, ArrayList<TaskItem> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItem taskItem = getItem(position); // Get the item at this position
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
        }

        TaskItem currentItem = getItem(position);


        // Get the "Delete" button and set its click listener
        Button deleteButton = convertView.findViewById(R.id.btnDeleteItem);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the item
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });


        TextView tvDescription = convertView.findViewById(R.id.textViewTaskDescription);
        TextView tvDatetime = convertView.findViewById(R.id.textViewTaskDatetime);
        TextView tvClassLabel = convertView.findViewById(R.id.textViewTaskClass);
        CheckBox cbDone = convertView.findViewById(R.id.checkBoxDone);

        // Set the text for description and datetime
        tvDescription.setText(taskItem.getDescription());
        tvDatetime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(taskItem.getDatetime()));

        // Check for null or empty class label
        TextView classLabelView = convertView.findViewById(R.id.textViewTaskClass);

        // Set the class label and color
        if (taskItem.getClassLabel() != null && !taskItem.getClassLabel().isEmpty()) {
            classLabelView.setText(taskItem.getClassLabel());
            classLabelView.setTextColor(Color.RED); // Set text color to red
            classLabelView.setVisibility(View.VISIBLE); // Make sure it's visible
        } else {
            classLabelView.setVisibility(View.GONE); // Hide if no class label
        }
        // Set the checked status of the checkbox
        cbDone.setChecked(taskItem.isDone());

        // Checkbox click listener
        cbDone.setOnClickListener(view -> {
            boolean newState = !taskItem.isDone();
            taskItem.setDone(newState);
            notifyDataSetChanged();
        });

        return convertView;
    }

}

