package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "course-database")
                .fallbackToDestructiveMigration()
                .build();
        courseDao = db.courseDao();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addCourseFragment);
            }
        });
        View.OnClickListener onEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Course courseToEdit = courseList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("coursePosition", position);
                bundle.putSerializable("courseToEdit", courseToEdit);

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_courseFragment_to_editCourseFragment, bundle);


            }
        };

        View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Course courseToDelete = courseList.get(position);
                courseDao.deleteCourse(courseToDelete);
                courseList.remove(position);

            }
        };

        courseAdapter = new CourseAdapter(new ArrayList<>(), courseDao, onEditClickListener, onDeleteClickListener);        recyclerView.setAdapter(courseAdapter);
        Button button = view.findViewById(R.id.nav_to_exam_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_courseFragment_to_examFragment);
            }
        });
        loadCourses();

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