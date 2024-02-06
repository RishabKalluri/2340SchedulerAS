package com.example.coursemanager.services;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
public class TaskListManager {
    private FirebaseFirestore db;

    public TaskListManager() {
        db = FirebaseService.getDb();
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