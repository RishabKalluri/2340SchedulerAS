package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.example.coursemanager.MainActivity;
import com.example.coursemanager.R;
import com.example.simpletodo.TaskItem;
import com.example.coursemanager.TaskItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class TodoActivity extends Activity {
    private ArrayList<TaskItem> items;
    private TaskItemAdapter itemsAdapter;
    private ListView lvItems;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        lvItems = findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new TaskItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        calendar = Calendar.getInstance();

        Button addDateTimeButton = findViewById(R.id.btnAddDateTime);
        addDateTimeButton.setOnClickListener(view -> {
            new DatePickerDialog(TodoActivity.this, (datePicker, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                new TimePickerDialog(TodoActivity.this, (timePicker, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        findViewById(R.id.btnSortByDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByDate();
            }
        });

        findViewById(R.id.btnSortByStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByStatus();
            }
        });

        findViewById(R.id.btnSortByClass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByClass();
            }
        });





    }


    @SuppressLint("ScheduleExactAlarm")
    private void scheduleNotification(TaskItem task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, com.example.simpletodo.AlarmReceiver.class);
        intent.putExtra("task_description", task.getDescription());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Ensure unique ID for each task

        // Schedule the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getDatetime());
        calendar.add(Calendar.MINUTE, -5); // Set 5 minutes before the task time

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
    private void sortByDate() {
        Collections.sort(items, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return o1.getDatetime().compareTo(o2.getDatetime());
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByClass() {
        Collections.sort(items, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                // Handling null or empty class labels by treating them as "zzz" so they end up at the bottom
                String class1 = o1.getClassLabel() == null || o1.getClassLabel().isEmpty() ? "zzz" : o1.getClassLabel();
                String class2 = o2.getClassLabel() == null || o2.getClassLabel().isEmpty() ? "zzz" : o2.getClassLabel();
                return class1.compareTo(class2);
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }


    private void sortByStatus() {
        Collections.sort(items, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return Boolean.compare(o1.isDone(), o2.isDone());
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }


    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewItem);
        EditText etClassLabel = findViewById(R.id.etClassLabel);
        String itemText = etNewItem.getText().toString();
        String classLabel = etClassLabel.getText().toString();
        if (!itemText.isEmpty()) {
            TaskItem newItem = new TaskItem(itemText, calendar.getTime(), classLabel); // Pass the class label here
            items.add(newItem);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
            etClassLabel.setText("");
        }
    }


}
