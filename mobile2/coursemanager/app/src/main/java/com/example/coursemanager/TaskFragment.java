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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.coursemanager.services.AppDatabase;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskDao;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    private AppDatabase db;
    private TaskDao taskDao;
    private List<Task> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

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
                taskAdapter.notifyDataSetChanged();
            }
        };

        taskAdapter = new TaskAdapter(new ArrayList<>(), taskDao, onEditClickListener, onDeleteClickListener);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addTaskFragment);
            }
        });

        Button button = view.findViewById(R.id.task_to_course_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_taskFragment_to_courseFragment);
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
}