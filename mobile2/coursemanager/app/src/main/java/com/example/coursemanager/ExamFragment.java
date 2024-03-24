package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.Exam;
import com.example.coursemanager.services.ExamDao;

import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends Fragment {
    private AppDatabase db;
    private ExamDao examDao;
    private List<Exam> examList;
    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "exam-database")
                .fallbackToDestructiveMigration()
                .build();
        examDao = db.examDao();

        recyclerView = view.findViewById(R.id.recyclerView);


        View.OnClickListener onEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putInt("examPosition", position);
            }
        };


        View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Exam examToDelete = examList.get(position);
                examDao.deleteExam(examToDelete);
                examList.remove(position);

            }
        };
        examAdapter = new ExamAdapter(new ArrayList<>(), examDao, onEditClickListener, onDeleteClickListener);
        recyclerView.setAdapter(examAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addExamFragment);
            }
        });

        Button button = view.findViewById(R.id.exam_to_course_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_examFragment_to_courseFragment);
            }
        });

        loadExams();



        return view;
    }

    private void loadExams() {
        examDao.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exam>>() {
            @Override
            public void onChanged(List<Exam> exams) {
                examList = exams;
                examAdapter.setExams(examList);
                examAdapter.notifyDataSetChanged();
            }
        });
    }
}