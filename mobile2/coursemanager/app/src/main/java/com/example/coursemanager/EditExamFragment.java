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
import com.example.coursemanager.services.Exam;
import com.example.coursemanager.services.ExamDao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditExamFragment extends Fragment {
    private AppDatabase db;
    private ExamDao examDao;
    private Exam examToEdit;
    final Calendar calendar = Calendar.getInstance();

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

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                                    updateLabel(date);
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

    private void updateLabel(EditText date) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(calendar.getTime()));
    }
}