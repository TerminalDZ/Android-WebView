package com.iba.myappname.notifications;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    long insert(StoredNotification notification);

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC LIMIT 200")
    List<StoredNotification> recent();

    @Query("SELECT COUNT(*) FROM notifications WHERE read = 0")
    int unreadCount();

    @Query("UPDATE notifications SET read = 1 WHERE id = :id")
    void markRead(long id);

    @Query("UPDATE notifications SET read = 1")
    void markAllRead();

    @Query("DELETE FROM notifications")
    void clear();
}
