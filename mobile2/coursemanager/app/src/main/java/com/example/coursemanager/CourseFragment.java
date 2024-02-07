package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        loadCourses();

        return view;
    }

    private void loadCourses() {
        new LoadCoursesTask().execute();
    }

    private class LoadCoursesTask extends AsyncTask<Void, Void, List<Course>> {
        @Override
        protected List<Course> doInBackground(Void... voids) {
            return courseDao.getAllCourses();
        }

        @Override
        protected void onPostExecute(List<Course> courses) {
            courseList = courses;
            courseAdapter.setCourses(courseList);
            courseAdapter.notifyDataSetChanged();
        }
    }

    private void showPopup(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_course) {
                    addCourse();
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }

    private void addCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Course");

        final EditText inputName = new EditText(getActivity());
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputName.setHint("Course Name");

        final EditText inputDescription = new EditText(getActivity());
        inputDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        inputDescription.setHint("Course Description");

        final EditText inputStartDate = new EditText(getActivity());
        inputStartDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        inputStartDate.setHint("Start Date (yyyy-mm-dd)");

        final EditText inputEndDate = new EditText(getActivity());
        inputEndDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        inputEndDate.setHint("End Date (yyyy-mm-dd)");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputCourseName = new EditText(getActivity());
        inputCourseName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputCourseName.setHint("Course Name");
        layout.addView(inputCourseName);

        final EditText inputTime = new EditText(getActivity());
        inputTime.setInputType(InputType.TYPE_CLASS_DATETIME);
        inputTime.setHint("Time");
        layout.addView(inputTime);

        final EditText inputInstructor = new EditText(getActivity());
        inputInstructor.setInputType(InputType.TYPE_CLASS_TEXT);
        inputInstructor.setHint("Instructor");
        layout.addView(inputInstructor);

        final EditText inputDaysOfWeek = new EditText(getActivity());
        inputDaysOfWeek.setInputType(InputType.TYPE_CLASS_TEXT);
        inputDaysOfWeek.setHint("Days of Week");
        layout.addView(inputDaysOfWeek);

        final EditText inputClassSection = new EditText(getActivity());
        inputClassSection.setInputType(InputType.TYPE_CLASS_TEXT);
        inputClassSection.setHint("Class Section");
        layout.addView(inputClassSection);

        final EditText inputLocation = new EditText(getActivity());
        inputLocation.setInputType(InputType.TYPE_CLASS_TEXT);
        inputLocation.setHint("Location");
        layout.addView(inputLocation);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String courseName = inputCourseName.getText().toString();
                String time = inputTime.getText().toString();
                String instructor = inputInstructor.getText().toString();
                String daysOfWeek = inputDaysOfWeek.getText().toString();
                String classSection = inputClassSection.getText().toString();
                String location = inputLocation.getText().toString();

                if (!courseName.isEmpty() && !time.isEmpty() && !instructor.isEmpty() && !daysOfWeek.isEmpty() && !classSection.isEmpty() && !location.isEmpty()) {
                    Course newCourse = new Course();
                    newCourse.setCourseName(courseName);
                    newCourse.setTime(time);
                    newCourse.setInstructor(instructor);
                    newCourse.setDaysOfWeek(daysOfWeek);
                    newCourse.setClassSection(classSection);
                    newCourse.setLocation(location);

                    new AsyncTask<Course, Void, Void>() {
                        @Override
                        protected Void doInBackground(Course... courses) {
                            courseDao.insertCourse(courses[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            // Reload the courses from the database to update the RecyclerView
                            loadCourses();
                        }
                    }.execute(newCourse);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}