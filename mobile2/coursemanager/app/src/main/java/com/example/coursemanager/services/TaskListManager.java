package com.example.coursemanager.services;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TaskListManager {
    private static TaskListManager instance = null;
    private FirebaseFirestore db;
    private List<Task> taskList;

    private TaskListManager() {
        db = FirebaseService.getDb();
        taskList = new ArrayList<>();
    }

    public static TaskListManager getInstance() {
        if (instance == null) {
            instance = new TaskListManager();
        }
        return instance;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        db.collection("taskLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Task obj = document.toObject(Task.class);
                        tasks.add(obj);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return tasks;
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void removeTask(Task task) {
        taskList.remove(task);
    }

    public void addTaskList(String listName) {
        db.collection("taskLists").document(listName).set(new HashMap<>())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Task list added with name: " + listName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error adding task list");
                    }
                });
    }

    public void updateTaskList(String oldListName, String newListName) {
        db.collection("taskLists").document(oldListName).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addTaskList(newListName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating task list");
                    }
                });
    }

    public void deleteTaskList(String listName) {
        db.collection("taskLists").document(listName).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Task list deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error deleting task list");
                    }
                });
    }

    public void addTaskToTaskList(String listName, Task task) {
        db.collection("taskLists").document(listName).collection("tasks").add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Task added to list with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error adding task to list");
                    }
                });
    }

    public void updateTaskInTaskList(String listName, String taskId, Task task) {
        db.collection("taskLists").document(listName).collection("tasks").document(taskId).set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Task updated in list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating task in list");
                    }
                });
    }

    public void deleteTaskFromTaskList(String listName, String taskId) {
        db.collection("taskLists").document(listName).collection("tasks").document(taskId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Task deleted from list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error deleting task from list");
                    }
                });
    }
}