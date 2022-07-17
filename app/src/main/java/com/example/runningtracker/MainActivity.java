package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MyService.MyBinder myService;
    private RunningViewModel viewModel;
    private TextView tv;
    private TextView tv2;

//    onCreate method starts foreground service immediately
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, 1);
        }
        startForegroundService(new Intent(this,MyService.class));
        this.bindService(new Intent(this, MyService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RunningViewModel.class);

    }

    public void onClickedStart(View v){
            myService.onStartCommand();


    }
    public void onClickedPause(View v){
            myService.pauseLocationManager();
//        }

    }
    public void onClickedStop(View v){
        myService.stopLocationManager();

    }

    public void onClickedRuns(View v){

        startActivity(new Intent(MainActivity.this, AllRuns.class));
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.d("g53mdp", "MainActivity onServiceConnected");
            myService = (MyService.MyBinder) service;
            myService.registerCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Log.d("g53mdp", "MainActivity onServiceDisconnected");
            myService.unregisterCallback(callback);
            myService = null;
        }

    };

    ICallBack callback = new ICallBack(){
        @Override
        public void counterEvent(final float counter,String duration) {
            runOnUiThread(new Runnable() {
//                updating running distance and duration in the textview
                @Override
                public void run() {
                    tv = findViewById(R.id.textView);
                    tv.setText(String.format("%.2f",counter)+ "km");

                    tv2 = findViewById(R.id.textView2);
                    tv2.setText(String.valueOf(duration));


                }
            });
        }


        @Override
        public void completeEvent(float distance, String duration,Date startTime) {
            Log.d("g53mdp", distance +","+ duration);

            final RunningAdapter adapter = new RunningAdapter(MainActivity.this);
            float roundDistance = (float) (Math.round(distance * 100.0) / 100.0);
//            inserting running object into database
            Running a = new Running(0,roundDistance,duration,"", null,String.valueOf(startTime));
            viewModel.insert(a);

        }
    };
}