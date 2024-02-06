package com.example.coursemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.coursemanager.services.Course;

public class EditCourseFragment extends Fragment {
    private Course clickedCourse;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_course, container, false);

        // Get the clicked course from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            clickedCourse = (Course) bundle.getSerializable("clickedCourse");
        }

        // Populate the TextViews with the details of the clicked course
        TextView courseNameTextView = view.findViewById(R.id.courseNameTextView);
        TextView courseTimeTextView = view.findViewById(R.id.courseTimeTextView);
        TextView courseInstructorTextView = view.findViewById(R.id.courseInstructorTextView);
        TextView daysOfWeekTextView = view.findViewById(R.id.daysOfWeekTextView);
        TextView professorTextView = view.findViewById(R.id.professorTextView);
        TextView classSectionTextView = view.findViewById(R.id.classSectionTextView);
        TextView locationTextView = view.findViewById(R.id.locationTextView);
        TextView roomNumberTextView = view.findViewById(R.id.roomNumberTextView);

        if (clickedCourse != null) {
            courseNameTextView.setText(clickedCourse.getCourseName());
            courseTimeTextView.setText(clickedCourse.getTime());
            courseInstructorTextView.setText(clickedCourse.getInstructor());
            daysOfWeekTextView.setText(clickedCourse.getDaysOfWeek());
            professorTextView.setText(clickedCourse.getProfessor());
            classSectionTextView.setText(clickedCourse.getClassSection());
            locationTextView.setText(clickedCourse.getLocation());
            roomNumberTextView.setText(clickedCourse.getRoomNumber());
        }

        return view;
    }
}