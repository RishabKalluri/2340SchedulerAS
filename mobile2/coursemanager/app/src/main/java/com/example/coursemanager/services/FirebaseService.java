package com.example.coursemanager.services;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseService {
    private static FirebaseFirestore db;

    public static FirebaseFirestore getDb() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }
}