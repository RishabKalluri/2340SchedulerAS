package com.example.coursemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;

public class AddCourseFragment extends Fragment {
    private AppDatabase db;
    private CourseDao courseDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "course-database").allowMainThreadQueries().build();
        courseDao = db.courseDao();

        final EditText courseName = view.findViewById(R.id.course_name);
        final EditText time = view.findViewById(R.id.time);
        final EditText instructor = view.findViewById(R.id.instructor);
        final EditText daysOfWeek = view.findViewById(R.id.days_of_week);
        final EditText classSection = view.findViewById(R.id.class_section);
        final EditText location = view.findViewById(R.id.location);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = courseName.getText().toString();
                String timeText = time.getText().toString();
                String instructorText = instructor.getText().toString();
                String daysOfWeekText = daysOfWeek.getText().toString();
                String classSectionText = classSection.getText().toString();
                String locationText = location.getText().toString();

                Course course = new Course();
                course.setCourseName(name);
                course.setTime(timeText);
                course.setInstructor(instructorText);
                course.setDaysOfWeek(daysOfWeekText);
                course.setClassSection(classSectionText);
                course.setLocation(locationText);

                courseDao.insertCourse(course);
            }
        });

        return view;
    }
}