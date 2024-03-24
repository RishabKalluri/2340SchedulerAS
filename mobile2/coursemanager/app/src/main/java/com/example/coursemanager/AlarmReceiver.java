package com.example.coursemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskDescription = intent.getStringExtra("task_description");
        showNotification(context, taskDescription);
    }

    private void showNotification(Context context, String taskDescription) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("TASK_REMINDER", "Task Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TASK_REMINDER")
                .setContentTitle("Task Reminder")
                .setContentText(taskDescription)
                .setSmallIcon(R.mipmap.ic_launcher); // Set an appropriate icon

        notificationManager.notify(1, builder.build());
    }
}
