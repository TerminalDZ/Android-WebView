package com.iba.myappname.notifications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Persisted record of a single push notification received by the app.
 * Backed by Room — see {@link NotificationDao} and {@link NotificationDatabase}.
 */
@Entity(tableName = "notifications")
public final class StoredNotification {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String title;

    @NonNull
    public String body;

    @Nullable
    public String deepLink;

    public long timestamp;

    public boolean read;

    public StoredNotification(@NonNull String title,
                              @NonNull String body,
                              @Nullable String deepLink,
                              long timestamp) {
        this.title = title;
        this.body = body;
        this.deepLink = deepLink;
        this.timestamp = timestamp;
        this.read = false;
    }
}
