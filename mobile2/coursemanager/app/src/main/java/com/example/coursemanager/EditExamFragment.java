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
import com.example.coursemanager.services.Exam;
import com.example.coursemanager.services.ExamDao;

public class EditExamFragment extends Fragment {
    private AppDatabase db;
    private ExamDao examDao;
    private Exam examToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_exam, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "exam-database").allowMainThreadQueries().build();
        examDao = db.examDao();

        examToEdit = (Exam) getArguments().getSerializable("examToEdit");

        final EditText examName = view.findViewById(R.id.exam_name);
        final EditText courseName = view.findViewById(R.id.course_name);
        final EditText time = view.findViewById(R.id.time);
        final EditText date = view.findViewById(R.id.date);
        final EditText location = view.findViewById(R.id.location);

        examName.setText(examToEdit.getExamName());
        courseName.setText(examToEdit.getCourseName());
        time.setText(examToEdit.getTime());
        date.setText(examToEdit.getDate());
        location.setText(examToEdit.getLocation());

        Button editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examToEdit.setExamName(examName.getText().toString());
                examToEdit.setCourseName(courseName.getText().toString());
                examToEdit.setTime(time.getText().toString());
                examToEdit.setDate(date.getText().toString());
                examToEdit.setLocation(location.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        examDao.updateExam(examToEdit);
                    }
                }).start();
            }
        });

        return view;
    }
}