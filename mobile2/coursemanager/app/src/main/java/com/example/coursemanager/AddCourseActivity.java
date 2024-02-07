package com.example.coursemanager;

import androidx.appcompat.app.AppCompatActivity;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.CourseDao;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.room.Room;
import com.example.coursemanager.services.Course;

public class AddCourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private CourseDao courseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_course);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "course-database").allowMainThreadQueries().build();
        courseDao = db.courseDao();

        final EditText courseName = findViewById(R.id.course_name);
        final EditText time = findViewById(R.id.time);
        final EditText instructor = findViewById(R.id.instructor);
        final EditText daysOfWeek = findViewById(R.id.days_of_week);
        final EditText classSection = findViewById(R.id.class_section);
        final EditText location = findViewById(R.id.location);

        Button addButton = findViewById(R.id.add_button);
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
                finish();
            }
        });
    }
}