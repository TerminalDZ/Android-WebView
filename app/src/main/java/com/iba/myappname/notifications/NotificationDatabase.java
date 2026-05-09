package com.iba.myappname.notifications;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {StoredNotification.class}, version = 1, exportSchema = false)
public abstract class NotificationDatabase extends RoomDatabase {

    private static final String DB_NAME = "iba_webview.db";

    private static volatile NotificationDatabase INSTANCE;

    public abstract NotificationDao notificationDao();

    @NonNull
    public static NotificationDatabase get(@NonNull Context context) {
        NotificationDatabase local = INSTANCE;
        if (local != null) return local;
        synchronized (NotificationDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                                context.getApplicationContext(),
                                NotificationDatabase.class,
                                DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }
}
