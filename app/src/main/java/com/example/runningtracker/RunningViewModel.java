package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RunningViewModel extends AndroidViewModel {

    private final MyRepository repository;

    private final LiveData<List<Running>> allRunning;

    public RunningViewModel(Application application) {
        super(application);
        repository = new MyRepository(application);
        allRunning= repository.getAllRunning();
    }

    LiveData<List<Running>> getAllRunnings() { return allRunning; }
    public void insert(Running running) { repository.insert(running); }
    public void delete(int id) { repository.delete(id); }
}
