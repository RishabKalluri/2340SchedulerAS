package com.example.coursemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

public class EditTaskFragment extends Fragment {
    private AppDatabase db;
    private TaskDao taskDao;
    private Task taskToEdit;

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

        Button editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskToEdit.setTaskName(taskName.getText().toString());
                taskToEdit.setDueDate(dueDate.getText().toString());
                taskToEdit.setCourseId(Integer.parseInt(courseId.getText().toString()));

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
}