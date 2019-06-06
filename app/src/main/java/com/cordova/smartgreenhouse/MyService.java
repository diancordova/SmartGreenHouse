package com.cordova.smartgreenhouse;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.cordova.smartgreenhouse.Activity.ControllingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyService extends Service {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("control");
    DatabaseReference refLampuUV, refStatusLampuUV, refAir, refStatusAir, refSprinkler, refStatusSpringkler, refNutrisi, refStatusNutrisi;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        refLampuUV = refHome.child("relayLampuUV");
        refStatusLampuUV = refLampuUV.child("status");
        refAir = refHome.child("relayPompaAir");
        refStatusAir = refAir.child("status");
        refSprinkler = refHome.child("relaySpringkler");
        refStatusSpringkler = refSprinkler.child("status");
        refNutrisi = refHome.child("relayPompaA");
        refStatusNutrisi = refNutrisi.child("status");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        monitorNotifikasi(refStatusAir, refStatusNutrisi, refStatusLampuUV, refStatusSpringkler);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        //do something
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void tampilNotification(String keterangan) {
        Intent mIntent = new Intent(this, ControllingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromNotif", "iya");
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        //Intent ii = new Intent(mContext.getApplicationContext(), RootActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(keterangan + " Menyala ");
        bigText.setBigContentTitle("Notifikasi Baru");
//        bigText.setSummaryText(" Order Baru Tervalidasi");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Notifikasi Baru");
        mBuilder.setContentText(keterangan + " Nyala");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }


    private void monitorNotifikasi(DatabaseReference refStatusAir, DatabaseReference refStatusNutrisi, DatabaseReference refStatusLampuUV, final DatabaseReference refStatusSpringkler) {

        refStatusAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean menyala = false;
                for (DataSnapshot refStatusAir : dataSnapshot.getChildren()) {
                }
                menyala  = (Boolean) dataSnapshot.getValue();

                if(menyala){
                    String keterangan = "Pompa Air";
                    tampilNotification(keterangan);
                }else{

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        refStatusNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean menyala = false;
                for (DataSnapshot refStatusNutrisi : dataSnapshot.getChildren()) {
                }
                menyala = (Boolean) dataSnapshot.getValue();

                if (menyala) {
                    String keterangan = "Pompa Nutrisi";
                    tampilNotification(keterangan);
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refStatusLampuUV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean menyala = false;
                for (DataSnapshot refStatusLampuUV : dataSnapshot.getChildren()) {
                }
                menyala = (Boolean) dataSnapshot.getValue();

                if (menyala) {
                    String keterangan = "Lampu UV";
                    tampilNotification(keterangan);
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refStatusSpringkler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean menyala = false;
                for (DataSnapshot refStatusSpringkler : dataSnapshot.getChildren()) {
                }
                menyala = (Boolean) dataSnapshot.getValue();

                if (menyala) {
                    String keterangan = "Springkler";
                    tampilNotification(keterangan);
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}

