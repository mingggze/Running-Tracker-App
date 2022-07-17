package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyRepository {
    private final TrackerDao trackerDao;
    private final LiveData<List<Running>> allRunning;

    MyRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);

        trackerDao = db.trackerDao();
        allRunning = trackerDao.getRunning();
    }

    LiveData<List<Running>> getAllRunning() {
        return allRunning;
    }

    void insert(Running running) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackerDao.insert(running);
        });
    }

    void delete(int id) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            trackerDao.delete(id);
        });
    }


}
