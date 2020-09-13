package com.kp.wheelsdiary.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import com.kp.wheelsdiary.MainActivity;
import com.kp.wheelsdiary.R;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    public static int notifId = 1;

    public static final String NOT_CHANNEL = "NOT_CHANNEL";
    public NotificationService() {
        super("MyIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            createNotificationChannel();

            NotificationCompat.Builder builder = notifyAboutUpcomingTasks();
            if (builder != null) {
                notificationManager.notify(notifId++, builder.build());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        if (alarmManager != null) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+90000, pendingIntent);
        }
    }

    private NotificationCompat.Builder notifyAboutUpcomingTasks() {
        int counter = WheelTaskService.countUpcomingTasks();
        if (counter > 0) {

            String text = "Your cars have " + counter + " upcoming task";
            if (counter > 1) {
                text += "s.";
            } else {
                text += ".";
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOT_CHANNEL)
                    .setSmallIcon(R.drawable.ic_car_for_notifi)
                    .setContentTitle("Reminder for car tasks")
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            return builder;
        }
        return null;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Notification channel about upcoming tasks";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOT_CHANNEL, (CharSequence) NOT_CHANNEL, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
