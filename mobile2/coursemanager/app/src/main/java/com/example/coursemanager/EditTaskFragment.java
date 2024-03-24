package com.example.coursemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskFragment extends Fragment {
    private AppDatabase db;
    private TaskDao taskDao;
    private Task taskToEdit;
    final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "task-database").allowMainThreadQueries().build();
        taskDao = db.taskDao();

        taskToEdit = (Task) getArguments().getSerializable("taskToEdit");

        final EditText taskName = view.findViewById(R.id.task_name);
        final EditText dueDate = view.findViewById(R.id.due_date);
        final EditText courseId = view.findViewById(R.id.course_id);

        taskName.setText(taskToEdit.getTaskName());
        dueDate.setText(taskToEdit.getDueDate());
        courseId.setText(String.valueOf(taskToEdit.getCourseId()));

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

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

        Button editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskToEdit.setTaskName(taskName.getText().toString());
                taskToEdit.setDueDate(dueDate.getText().toString());
                taskToEdit.setCourseId(courseId.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        taskDao.updateTask(taskToEdit);
                    }
                }).start();


                Toast.makeText(v.getContext(), "Task Edited!", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void updateLabel(EditText dueDate) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dueDate.setText(sdf.format(calendar.getTime()));
    }
}