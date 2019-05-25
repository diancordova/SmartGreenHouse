package com.cordova.smartgreenhouse.Activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cordova.smartgreenhouse.Controller.SQLiteHandler;
import com.cordova.smartgreenhouse.Controller.SessionManager;
import com.cordova.smartgreenhouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class ControlAirActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private NotificationManager notificationManager;
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refDHT, refTemp, refHmd,refPhotoDioda,
            refStatusLampuUV,refStatusPhotoDioda,refLampuUV,refSpringkle,refStatusSprinkle,refManual,refStatusManual,refIsLoading,refIsLoading1;
    private ProgressDialog pDialog;
    Switch switchOnOffUV,switchOnOffSprinkle;
    TextView textViewTemp,textViewHMD,textViewStatusIntensitas,textViewStatusUV,textViewStatusSprinkle;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    List<String> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_air);
        database1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setTitle("Green House");

        //------------NILAI DATA FIREBASE DARI SENSOR DHT-----------//
        refDHT = refHome.child("dht");
        refTemp = refDHT.child("temp");
        refHmd = refDHT.child("humidity");
        //------------NILAI DATA FIREBASE DARI SENSOR photoDida-----------//
        refPhotoDioda = refHome.child("photoDioda");
        refStatusPhotoDioda = refPhotoDioda.child("status");
        //------------STATUS DATA FIREBASE DARI NYALA LAMPU Ultra Violet-----------//
        refLampuUV = refHome.child("relay");
        refStatusLampuUV = refLampuUV.child("status");
        //------------STATUS DATA FIREBASE DARI NYALANYA SPRINKLE-----------//
        refSpringkle = refHome.child("relaySprinkle");
        refStatusSprinkle = refSpringkle.child("status");
        refManual = refHome.child("relay");
        refStatusManual = refManual.child("manual");
        refIsLoading = refManual.child("isLoading");
        refIsLoading1 = refManual.child("isLoading1");

        //Inisialisasi object TextView
        textViewTemp = (TextView) findViewById(R.id.tvPH);
        textViewHMD = (TextView) findViewById(R.id.nilai2);
        textViewStatusIntensitas = (TextView) findViewById(R.id.tvMonitoring);
        textViewStatusUV = (TextView) findViewById(R.id.tvTumbuhan);
        textViewStatusSprinkle= (TextView) findViewById(R.id.tvMetode);
        switchOnOffUV = findViewById(R.id.switchUltraviolet);
        switchOnOffSprinkle = findViewById(R.id.switchInsaknet);

        monitorTemperature(refTemp, textViewTemp);
        monitorHMD(refHmd,textViewHMD);
        monitorIntesitas(refStatusPhotoDioda,textViewStatusIntensitas);
        controlLampuUV(refStatusLampuUV);
        controlSprinkle(refStatusSprinkle);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }


    private void monitorIntesitas(final DatabaseReference refStatusPhotoDioda_,final TextView textViewStatusIntensitas ) {

        refStatusPhotoDioda_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){

                    textViewStatusIntensitas.setText("Terang");
                }else{
                    textViewStatusIntensitas.setText("Gelap");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }



    private void monitorHMD(final DatabaseReference refHmd_, final TextView textViewHMD) {
        refHmd_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewHMD.setText(dataSnapshot.getValue() + " %");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    private void monitorTemperature(final DatabaseReference refTemp_, final TextView textViewTemp) {
        refTemp_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Double temp = (Double) dataSnapshot.getValue();
                textViewTemp.setText(dataSnapshot.getValue().toString() + " °C");
                Log.d("UserActivityDrawer_155",dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    //    fungsi untuk mengontrol lampu UV
    private void controlLampuUV(final DatabaseReference refStatusLampuUV) {
        switchOnOffUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    FirebaseMessaging.getInstance().isAutoInitEnabled();
                    FirebaseMessaging.getInstance().subscribeToTopic("weather")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = getString(R.string.msg_subscribed);
                                    if (!task.isSuccessful()) {
                                        msg = getString(R.string.msg_subscribed);
                                    }
                                    Log.d("ini:", msg);
                                    Toast.makeText(ControlAirActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });

                    refStatusManual.setValue(isChecked);
                    refStatusLampuUV.setValue(isChecked);
                    refIsLoading.setValue(isChecked);
                    refIsLoading.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
                            if(isLoading){
                                showDialog();
                            }else{
                                hideDialog();
                                textViewStatusUV.setText("Nyala");
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Lampu UV Menyala", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    refStatusManual.setValue(isChecked);
                    refStatusLampuUV.setValue(isChecked);
                    textViewStatusUV.setText("Mati");
                    refIsLoading1.setValue(!isChecked);
                    refIsLoading1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
                            if(isLoading){
                                showDialog();
                            }else{
                                hideDialog();
                                textViewStatusUV.setText("Mati");
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Lampu UV Mati", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }
        });

    }
    //    fungsi untuk mengontrol kipas angin
    private void controlSprinkle(final DatabaseReference refStatusSprinkle) {
        switchOnOffSprinkle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                refStatusSprinkle.setValue(isChecked);
                if(isChecked){

                    Snackbar snackbar = Snackbar
                            .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Sprinkle Menyala", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Sprinkle Mati", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }
            }
        });

        refStatusSprinkle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){
                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Nyala");

                } else {

                    switchOnOffSprinkle.setChecked(false);
                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
