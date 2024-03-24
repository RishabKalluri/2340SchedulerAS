package com.example.coursemanager;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

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
        final String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        final boolean[] checkedDays = new boolean[7];

        daysOfWeek.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                    final boolean[] checkedDays = new boolean[7];

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Select days of the week")
                            .setMultiChoiceItems(days, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    checkedDays[which] = isChecked;
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    StringBuilder selectedDays = new StringBuilder();
                                    for (int i = 0; i < checkedDays.length; i++) {
                                        if (checkedDays[i]) {
                                            selectedDays.append(days[i]).append(" ");
                                        }
                                    }
                                    daysOfWeek.setText(selectedDays.toString().trim());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        });
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = courseName.getText().toString().toUpperCase();
                String timeText = time.getText().toString();
                String instructorText = instructor.getText().toString();
                String daysOfWeekText = daysOfWeek.getText().toString();
                String classSectionText = classSection.getText().toString();
                String locationText = location.getText().toString();

                if (name.isEmpty()) {
                    courseName.setError("Course name is required");
                    return;
                }
                if (timeText.isEmpty()) {
                    time.setError("Time is required");
                    return;
                }
                if (instructorText.isEmpty()) {
                    instructor.setError("Instructor is required");
                    return;
                }
                if (daysOfWeekText.isEmpty()) {
                    daysOfWeek.setError("Days of week is required");
                    return;
                }
                if (classSectionText.isEmpty()) {
                    classSection.setError("Class section is required");
                    return;
                }
                if (locationText.isEmpty()) {
                    location.setError("Location is required");
                    return;
                }

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