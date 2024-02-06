//package com.example.coursemanager;
//x`
//import android.os.Bundle;
//import android.util.Log;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.coursemanager.services.FirebaseService;
//import com.example.coursemanager.services.Task;
//import com.example.coursemanager.services.TaskListManager;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ToDoListActivity extends AppCompatActivity {
//    private static final String TAG = "ToDoListActivity";
//    private RecyclerView recyclerView;
//    private TaskAdapter taskAdapter;
//    private TaskListManager taskListManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_todo_list);
//
//        taskListManager = TaskListManager.getInstance();
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskAdapter = new TaskAdapter(taskListManager.getTasks());
//        recyclerView.setAdapter(taskAdapter);
//
//        loadTasks();
//    }
//
//    public List<Task> loadTasks() {
//        FirebaseFirestore db = FirebaseService.getDb();
//        List<Task> tasks = new ArrayList<>();
//        db.collection("taskLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Task obj = document.toObject(Task.class);
//                        tasks.add(obj);
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//        return tasks;
//    }
//}