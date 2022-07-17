package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "running_table")
public class Running {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private final float distance;
    private final String time;
    private final String info;
    private final String uri;
    private final String startTime;



    public Running(int id, float distance, String time, String info, String uri, String startTime){
        this.id =id;
        this.distance=distance;
        this.time=time;
        this.info = info;
        this.uri = uri;
        this.startTime=startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public String getInfo() {
        return info;
    }


    public String getUri() {
        return uri;
    }

    public String getStartTime() {
        return startTime;
    }
}
