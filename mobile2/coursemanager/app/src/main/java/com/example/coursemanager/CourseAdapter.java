package com.example.coursemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanager.services.Course;
import com.example.coursemanager.services.CourseDao;

import java.util.List;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private CourseDao courseDao;
    private final View.OnClickListener onEditClickListener;
    private final View.OnClickListener onDeleteClickListener;

    public CourseAdapter(List<Course> courseList, CourseDao courseDao, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
        this.courseList = courseList;
        this.courseDao = courseDao;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setCourses(List<Course> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view, onEditClickListener, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText("Name: " + course.getCourseName());
        holder.time.setText("Time: " + course.getTime());
        holder.instructor.setText("Instructor: " + course.getInstructor());
        holder.daysOfWeek.setText("Days: " + course.getDaysOfWeek());
        holder.classSection.setText("Section: " + course.getClassSection());
        holder.location.setText("Location: " + course.getLocation());

        holder.deleteButton.setTag(position);
        holder.editButton.setTag(position);
        holder.editButton.setOnClickListener(onEditClickListener);

        holder.deleteButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Course courseToDelete = courseList.get(currentPosition);

            Executors.newSingleThreadExecutor().execute(() -> {
                courseDao.deleteCourse(courseToDelete);

                // Run on UI thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    courseList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, courseList.size());
                });
            });
        });

        holder.editButton.setOnClickListener(v -> {
            int currentPosition = (int) v.getTag();
            Course courseToEdit = courseList.get(currentPosition);
            onEditClickListener.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName, time, instructor, daysOfWeek, classSection, location;
        public Button deleteButton, editButton;

        public CourseViewHolder(View view, View.OnClickListener onEditClickListener, View.OnClickListener onDeleteClickListener) {
            super(view);
            courseName = view.findViewById(R.id.courseName);
            time = view.findViewById(R.id.time);
            instructor = view.findViewById(R.id.instructor);
            daysOfWeek = view.findViewById(R.id.daysOfWeek);
            classSection = view.findViewById(R.id.classSection);
            location = view.findViewById(R.id.location);
            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
            editButton.setOnClickListener(onEditClickListener);
            deleteButton.setOnClickListener(onDeleteClickListener);
        }
    }
}