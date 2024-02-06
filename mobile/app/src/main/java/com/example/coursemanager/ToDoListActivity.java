package com.example.coursemanager;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coursemanager.services.Task;
import com.example.coursemanager.services.TaskListManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity {
    private static final String TAG = "ToDoListActivity";
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskListManager taskListManager;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        taskListManager = new TaskListManager();
        taskList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        loadTasks();
    }

    private void loadTasks() {
        taskListManager.getTasks()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            taskList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Task task = document.toObject(Task.class);
                                taskList.add(task);
                            }
                            taskAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}