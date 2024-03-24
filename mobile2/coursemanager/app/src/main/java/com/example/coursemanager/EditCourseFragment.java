package com.example.coursemanager;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import java.util.Calendar;

public class EditCourseFragment extends Fragment {
    private AppDatabase db;
    private CourseDao courseDao;
    private Course courseToEdit;
    final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_course, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "course-database").allowMainThreadQueries().build();
        courseDao = db.courseDao();

        courseToEdit = (Course) getArguments().getSerializable("courseToEdit");

        final EditText courseName = view.findViewById(R.id.course_name);
        final EditText time = view.findViewById(R.id.time);
        final EditText instructor = view.findViewById(R.id.instructor);
        final EditText daysOfWeek = view.findViewById(R.id.days_of_week);
        final EditText classSection = view.findViewById(R.id.class_section);
        final EditText location = view.findViewById(R.id.location);

        courseName.setText(courseToEdit.getCourseName());
        time.setText(courseToEdit.getTime());
        instructor.setText(courseToEdit.getInstructor());
        daysOfWeek.setText(courseToEdit.getDaysOfWeek());
        classSection.setText(courseToEdit.getClassSection());
        location.setText(courseToEdit.getLocation());

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    time.setText(String.format("%02d:%02d", hourOfDay, minute));
                                }
                            }, hour, minute, true);
                    timePickerDialog.show();
                }
            }
        });

        Button editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseToEdit.setCourseName(courseName.getText().toString());
                courseToEdit.setTime(time.getText().toString());
                courseToEdit.setInstructor(instructor.getText().toString());
                courseToEdit.setDaysOfWeek(daysOfWeek.getText().toString());
                courseToEdit.setClassSection(classSection.getText().toString());
                courseToEdit.setLocation(location.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        courseDao.updateCourse(courseToEdit);
                    }
                }).start();
            }
        });

        return view;
    }
}