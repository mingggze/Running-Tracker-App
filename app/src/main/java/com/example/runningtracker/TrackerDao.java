package com.example.runningtracker;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrackerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Running running);

    @Query("DELETE FROM running_table")
    void deleteAll();

    @Query("DELETE FROM running_table WHERE id=:id")
    void delete(int id);

    @Query("SELECT * FROM running_table")
    LiveData<List<Running>> getRunning();

    @Query("SELECT * FROM running_table")
    Cursor getCursorAll();

//    @Query("UPDATE running_table SET info=:info WHERE order_id = :id")
//    void update(String info, int id);
}
