package com.example.coursemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.coursemanager.services.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskFragment extends Fragment {
    private AppDatabase db;
    private TaskDao taskDao;
    private List<Task> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    private boolean isSortByNameEnabled = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDatabase.class, "task-database")
                .fallbackToDestructiveMigration()
                .build();
        taskDao = db.taskDao();

        recyclerView = view.findViewById(R.id.recyclerView);

        View.OnClickListener onEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Task taskToEdit = taskList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskToEdit", taskToEdit);

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_taskFragment_to_editTaskFragment, bundle);
            }
        };

        View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Task taskToDelete = taskList.get(position);
                taskDao.deleteTask(taskToDelete);
                taskList.remove(position);
                taskAdapter.notifyDataSetChanged(); // Notify the adapter about the removed item
            }
        };
        CourseDao courseDao = db.courseDao();

        taskAdapter = new TaskAdapter(taskList, taskDao, onEditClickListener, onDeleteClickListener);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addTaskFragment);
            }
        });

        Button button = view.findViewById(R.id.nav_to_course_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_taskFragment_to_courseFragment);
            }
        });
        Button examButton = view.findViewById(R.id.task_to_exam_button);
        examButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_taskFragment_to_examFragment);
            }
        });

        Button sortButton = view.findViewById(fjdkafdhjkfhdjkfhdajkfa); // add this button in your layout and replace the gibberish
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSortByName();
            }
        });


        loadTasks();

        return view;
    }

    private void loadTasks() {
        taskDao.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskList = tasks;
                taskAdapter.setTasks(taskList);
                taskAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sortByName() {
        if (taskList != null) {
            if (isSortByNameEnabled) {
                Collections.sort(taskList, new Comparator<Task>() {
                    @Override
                    public int compare(Task task1, Task task2) {
                        return task1.getTaskName().compareToIgnoreCase(task2.getTaskName());
                    }
                });
            }
            taskAdapter.notifyDataSetChanged();
        }
    }

    private void toggleSortByName() {
        isSortByNameEnabled = !isSortByNameEnabled;
        sortByName();
    }
}