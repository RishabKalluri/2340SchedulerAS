package com.example.coursemanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simpletodo.TaskItem;
import com.example.coursemanager.TaskItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class TodoFragment extends Fragment {
    private ArrayList<TaskItem> items;
    private TaskItemAdapter itemsAdapter;
    private ListView lvItems;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_activity_main, container, false);

        lvItems = view.findViewById(R.id.lvItems);
        items = new ArrayList<>();
        itemsAdapter = new TaskItemAdapter(getActivity(), items);
        lvItems.setAdapter(itemsAdapter);

        calendar = Calendar.getInstance();

        Button addDateTimeButton = view.findViewById(R.id.btnAddDateTime);
        addDateTimeButton.setOnClickListener(v -> new DatePickerDialog(getActivity(), (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            new TimePickerDialog(getActivity(), (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        view.findViewById(R.id.btnSortByDate).setOnClickListener(v -> sortByDate());
        view.findViewById(R.id.btnSortByStatus).setOnClickListener(v -> sortByStatus());
        view.findViewById(R.id.btnSortByClass).setOnClickListener(v -> sortByClass());

        Button addButton = view.findViewById(R.id.btnAddItem); // Assuming you have an Add button with id btnAddItem
        addButton.setOnClickListener(this::onAddItem);

        return view;
    }

    private void sortByDate() {
        Collections.sort(items, Comparator.comparing(TaskItem::getDatetime));
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByClass() {
        Collections.sort(items, (o1, o2) -> {
            String class1 = o1.getClassLabel() == null || o1.getClassLabel().isEmpty() ? "zzz" : o1.getClassLabel();
            String class2 = o2.getClassLabel() == null || o2.getClassLabel().isEmpty() ? "zzz" : o2.getClassLabel();
            return class1.compareTo(class2);
        });
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByStatus() {
        Collections.sort(items, Comparator.comparing(TaskItem::isDone));
        itemsAdapter.notifyDataSetChanged();
    }

    public void onAddItem(View view) {
        View rootView = getView(); // Get the root view of the fragment to find child views
        if (rootView != null) {
            EditText etNewItem = rootView.findViewById(R.id.etNewItem);
            EditText etClassLabel = rootView.findViewById(R.id.etClassLabel);
            String itemText = etNewItem.getText().toString();
            String classLabel = etClassLabel.getText().toString();
            if (!itemText.isEmpty()) {
                TaskItem newItem = new TaskItem(itemText, calendar.getTime(), classLabel);
                items.add(newItem);
                itemsAdapter.notifyDataSetChanged();
                etNewItem.setText("");
                etClassLabel.setText("");
            }
        }
    }



}
