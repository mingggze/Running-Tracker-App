package com.example.runningtracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {

    public MyService() {
    }
    RemoteCallbackList<MyBinder> remoteCallbackList = new RemoteCallbackList<>();

    private MyLocationListener locationListener;
    private LocationManager locationManager;
    private Date startTime;
    private float totaldistance;
    private Date pausetime;
    private int millis;
    private int mins;
    private float distance;
    private int time;

    public void onStartCommand() {
        if (locationListener==null){
            Log.d("comp3018", "in");
            try {
                startTime = Calendar.getInstance().getTime();
                locationListener = new MyLocationListener();
                locationManager =
                        (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        500, // minimum time interval between updates
                        1, // minimum distance between updates, in metres
                        locationListener);
            } catch (SecurityException e) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_LONG).show();
                }
                Log.d("comp3018", e.toString());
            }
            startNotification(0);        }

    }

    public void stopLocationManager(boolean stop){
//        check if the session is paused or stopped
        if(stop){
            totaldistance =0;
            mins = 0;
            stopNotification();
            final int n = remoteCallbackList.beginBroadcast();
            for (int i=0; i<n; i++) {
                remoteCallbackList.getBroadcastItem(i).callback.completeEvent(distance/1000,getDurationString(time),startTime);
            }
            remoteCallbackList.finishBroadcast();

        }
        if (locationListener != null){
            locationManager.removeUpdates(locationListener);
        }
        locationListener= null;

    }

    public void pauseLocationManager(){
        if(locationListener!=null){
            totaldistance+=locationListener.getDistance();
            pausetime = Calendar.getInstance().getTime();
            millis = (int) (pausetime.getTime() - startTime.getTime());
            mins += (millis/(1000));
            Log.d("mins", String.valueOf(mins));
            stopLocationManager(false);
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder implements IInterface
    {
        @Override
        public IBinder asBinder() {
            return this;
        }

        public void onStartCommand() {
            MyService.this.onStartCommand();
        }
        public void stopLocationManager() {
            MyService.this.stopLocationManager(true);
        }

        public void registerCallback(ICallBack callback) {
            this.callback = callback;
            remoteCallbackList.register(MyBinder.this);
        }

        public void unregisterCallback(ICallBack callback) {
            remoteCallbackList.unregister(MyBinder.this);
        }

        ICallBack callback;

        public void pauseLocationManager() { MyService.this.pauseLocationManager(); }
    }

    public void stopNotification(){

        stopForeground(true);
    }

    public void startNotification(float dist){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new Notification.Builder(this,"1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Running...")
                .setContentText(String.format("%.2f", dist)+"km")
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel("1","MyService", NotificationManager.IMPORTANCE_DEFAULT);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        doCallbacks();
    }

    public void doCallbacks() {
        new Thread(() ->{
            while (true){
                if(locationListener!=null){
                    distance = totaldistance+ locationListener.getDistance();
                    Date currentTime = Calendar.getInstance().getTime();
                    time = (int) (mins + (currentTime.getTime()-startTime.getTime()) /1000);
                    startNotification(distance/1000);
                    final int n = remoteCallbackList.beginBroadcast();
                    float roundDistance = (float) (Math.round((distance/1000) * 100.0) / 100.0);
                    for (int i=0; i<n; i++) {
//                        using callback to transfer data to MainActivity
                        remoteCallbackList.getBroadcastItem(i).callback.counterEvent(roundDistance,getDurationString(time));
                    }
                    remoteCallbackList.finishBroadcast();
                }
//
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public String getDurationString(long seconds) {
        return seconds >= 3600
                ? String.format("%d:%02d:%02d", seconds / 3600, seconds % 3600 / 60, seconds % 60)
                : String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
