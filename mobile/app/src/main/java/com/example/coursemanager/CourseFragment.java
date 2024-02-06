package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {
    private static final String TAG = "CourseActivity";
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private FirebaseFirestore db;
    private List<Course> courseList;
    private CourseManager courseManager;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_course, container, false);

        db = FirebaseFirestore.getInstance();
        courseList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseAdapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(courseAdapter);

        courseManager = new CourseManager();

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        loadCourses();

        return view;
    }

    private void loadCourses() {
        db.collection("courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            courseList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Course course = document.toObject(Course.class);
                                courseList.add(course);
                            }
                            courseAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void addCourse() {
        EditText courseNameEditText = view.findViewById(R.id.courseName);
        EditText courseTimeEditText = view.findViewById(R.id.courseTime);
        EditText courseInstructorEditText = view.findViewById(R.id.courseInstructor);

        String courseName = courseNameEditText.getText().toString();
        String courseTime = courseTimeEditText.getText().toString();
        String courseInstructor = courseInstructorEditText.getText().toString();

        Course course = new Course();
        course.setCourseName(courseName);
        course.setTime(courseTime);
        course.setInstructor(courseInstructor);

        courseManager.addCourse(course);

        // Refresh the course list
        loadCourses();
    }
}