package com.example.coursemanager;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemanager.services.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }
    public void setCourses(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.time.setText(course.getTime());
        holder.instructor.setText(course.getInstructor());
        holder.daysOfWeek.setText(course.getDaysOfWeek());
        holder.classSection.setText(course.getClassSection());
        holder.location.setText(course.getLocation());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView time;
        public TextView instructor;
        public TextView daysOfWeek;
        public TextView classSection;
        public TextView location;
        public Button deleteButton;

        public CourseViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.courseName);
            time = view.findViewById(R.id.time);
            instructor = view.findViewById(R.id.instructor);
            daysOfWeek = view.findViewById(R.id.daysOfWeek);
            classSection = view.findViewById(R.id.classSection);
            location = view.findViewById(R.id.location);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }
}