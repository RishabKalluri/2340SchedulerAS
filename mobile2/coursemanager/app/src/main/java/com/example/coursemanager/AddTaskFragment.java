package com.example.coursemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.Toast;

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
        final EditText courseInput = view.findViewById(R.id.course_input);

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, month);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    updateLabel(dueDate);
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = taskName.getText().toString();
                String date = dueDate.getText().toString();
                String courseName = courseInput.getText().toString().toUpperCase();

                if (name.isEmpty()) {
                    taskName.setError("Task name is required");
                    return;
                }
                if (date.isEmpty()) {
                    dueDate.setError("Due date is required");
                    return;
                }

                Task task = new Task();
                task.setTaskName(name);
                task.setDueDate(date);

                if (!courseName.isEmpty()) {

                    Course course = courseDao.getCourseByName(courseName);
                    if (course != null) {
                        task.setCourseId(course.getId());
                    }
                }

                taskDao.insertTask(task);

                Toast.makeText(v.getContext(), "Task Added!", Toast.LENGTH_SHORT).show();
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