package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {
    private AppDatabase db;
    private CourseDao courseDao;
    private List<Course> courseList;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "course-database").build();
        courseDao = db.courseDao();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseAdapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(courseAdapter);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCourseActivity.class);
                startActivity(intent);
            }
        });        loadCourses();

        return view;
    }

    private void loadCourses() {
        courseDao.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseList = courses;
                courseAdapter.setCourses(courseList);
                courseAdapter.notifyDataSetChanged();
            }
        });
    }
}