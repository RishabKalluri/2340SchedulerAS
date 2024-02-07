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

public class AddExamFragment extends Fragment {
    private AppDatabase db;
    private ExamDao examDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_exam, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "exam-database").allowMainThreadQueries().build();
        examDao = db.examDao();

        final EditText examName = view.findViewById(R.id.exam_name);
        final EditText courseName = view.findViewById(R.id.course_name);
        final EditText time = view.findViewById(R.id.time);
        final EditText date = view.findViewById(R.id.date);
        final EditText location = view.findViewById(R.id.location);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examNameText = examName.getText().toString();
                String courseNameText = courseName.getText().toString();
                String timeText = time.getText().toString();
                String dateText = date.getText().toString();
                String locationText = location.getText().toString();

                Exam exam = new Exam();
                exam.setExamName(examNameText);
                exam.setCourseName(courseNameText);
                exam.setTime(timeText);
                exam.setDate(dateText);
                exam.setLocation(locationText);

                examDao.insertExam(exam);
            }
        });

        return view;
    }
}