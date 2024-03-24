package com.example.coursemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTaskFragment extends Fragment {
    private AppDatabase db;
    private TaskDao taskDao;
    private CourseDao courseDao;
    private Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "task-database").allowMainThreadQueries().build();
        taskDao = db.taskDao();
        courseDao = db.courseDao();

        final EditText taskName = view.findViewById(R.id.task_name);
        final EditText dueDate = view.findViewById(R.id.due_date);
        final Spinner courseSpinner = view.findViewById(R.id.course_spinner);

        List<Course> courses = courseDao.getAllCoursesSync();
        List<String> courseNames = new ArrayList<>();
        for (Course course : courses) {
            courseNames.add(course.getCourseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dueDate);
            }
        };

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = taskName.getText().toString();
                String date = dueDate.getText().toString();
                String courseName = courseSpinner.getSelectedItem().toString();

                if (name.isEmpty()) {
                    taskName.setError("Task name is required");
                    return;
                }
                if (date.isEmpty()) {
                    dueDate.setError("Due date is required");
                    return;
                }
                if (courseName.isEmpty()) {
                    return;
                }

                Task task = new Task();
                task.setTaskName(name);
                task.setDueDate(date);
                task.setCourse(courseDao.getCourseByName(courseName).getId());

                taskDao.insertTask(task);
            }
        });

        return view;
    }

    private void updateLabel(EditText dueDate) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dueDate.setText(sdf.format(calendar.getTime()));
    }
}