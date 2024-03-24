package com.example.coursemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Exam;
import com.example.coursemanager.services.ExamDao;
import java.util.Calendar;

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

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examNameText = examName.getText().toString();
                String courseNameText = courseName.getText().toString();
                String timeText = time.getText().toString();
                String dateText = date.getText().toString();
                String locationText = location.getText().toString();

                if (examNameText.isEmpty()) {
                    examName.setError("Exam name is required");
                    return;
                }
                if (courseNameText.isEmpty()) {
                    courseName.setError("Course name is required");
                    return;
                }
                if (timeText.isEmpty()) {
                    time.setError("Time is required");
                    return;
                }
                if (dateText.isEmpty()) {
                    date.setError("Date is required");
                    return;
                }
                if (locationText.isEmpty()) {
                    location.setError("Location is required");
                    return;
                }

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