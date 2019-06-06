package com.cordova.smartgreenhouse;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.cordova.smartgreenhouse.Activity.ActivityMonitorGreenHouse;
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
        Uri suaraNotif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String CHANNEL_ID = "my_channel_01";
        Intent intent = new Intent(getApplicationContext(), ActivityMonitorGreenHouse.class);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Notifikasi", importance);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Notifikasi")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(12)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setChannelId(CHANNEL_ID)
                .setContentText(keterangan + " Menyala");

        builder.setOngoing(false);
        builder.setSound(suaraNotif);

        notificationManager.notify(1, builder.build()

        );
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(mChannel);
//        }


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

