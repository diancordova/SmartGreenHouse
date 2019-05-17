package com.cordova.smartgreenhouse;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.cordova.smartgreenhouse.Activity.ActivityMonitorGreenHouse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyService extends Service {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refLampuUV,refStatusLampuUV;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        refLampuUV = refHome.child("relay");
        refStatusLampuUV = refLampuUV.child("status");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        monitorIntesitas(refStatusLampuUV);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        //do something
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void tampilNotification() {
        Uri suaraNotif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), ActivityMonitorGreenHouse.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Notifikasi")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(12)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentText("Pompa Nutrisi A Menyala");

        builder.setOngoing(false);
        builder.setSound(suaraNotif);

        notificationManager.notify(1, builder.build()

        );

    }
    private void monitorIntesitas(final DatabaseReference refStatusPhotoDioda_ ) {

        refStatusPhotoDioda_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean menyala = false;
                for (DataSnapshot refStatusPhotoDioda_ : dataSnapshot.getChildren()){}
                menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){
                    tampilNotification();
                }else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }


}
