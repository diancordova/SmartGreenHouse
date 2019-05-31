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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class ControllingActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private NotificationManager notificationManager;
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refHome1 = database.getReference("control");
    DatabaseReference refDHT, refTemp, refHmd,refPhotoDioda,refTumbuhan,refStatus,refStatusAir,
            refUrl,refStatusLampuUV,refStatusPhotoDioda,refLampuUV,refSpringkle,refStatusSprinkle,
            refAir,refNutrisii,refStatusManual,refStatusNutrisi,refUV,refDioda,refName,refIsLoading,refIsLoading1,refNilaiPhotoDioda,refPH,refNilaiPh,refStatusPH,refNutrisi,refNilaiNutrisi,refNilaiSuhu;
    private ProgressDialog pDialog;
    Switch switchOnOffUV,switchOnOffSprinkle,switchOnOffAir,switchOnOffNutrisi;
    TextView tvNilaiSuhu,tvNilaiIntenstitas,tvNilaiPH,tvNilaiNutrisi;
    TextView textViewStatusNutrisi,textViewStatusAir,textViewStatusUV,textViewStatusSprinkle,tvMonitoring;
    ImageView fotoTumbuhan;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    List<String> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlling);
        database1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //------------NILAI DATA FIREBASE DARI SENSOR DHT-----------//
        refDHT = refHome.child("dht");
        refNilaiSuhu = refDHT.child("temp");
        refHmd = refDHT.child("humidity");
        //------------NILAI DATA FIREBASE DARI SENSOR photoDida-----------//
        refPhotoDioda = refHome.child("photoDioda");
        refNilaiPhotoDioda = refPhotoDioda.child("nilai");
        //------------STATUS DATA FIREBASE DARI NYALA LAMPU Ultra Violet-----------//
        refLampuUV = refHome.child("relay");
        refStatusLampuUV = refLampuUV.child("status");
        //------------STATUS DATA FIREBASE DARI NYALANYA SPRINKLE-----------//
        refSpringkle = refHome1.child("relaySpringkler");
        refStatusSprinkle = refSpringkle.child("status");
        refAir = refHome1.child("relayPompaAir");
        refStatusAir = refAir.child("status");
        refNutrisii = refHome1.child("relayPompaA");
        refStatusNutrisi = refNutrisii.child("status");
        refUV = refHome1.child("relayLampuUV");
        refStatusLampuUV = refUV.child("status");
        refDioda = refHome1.child("relayLampuUV");
        refStatusPhotoDioda = refDioda.child("status");
        refTumbuhan = database.getReference("pilihTumbuhan");
        refUrl = refTumbuhan.child("url");
        refName = refTumbuhan.child("name");

        //------------NILAI DATA FIREBASE DARI SENSOR PH-----------//
        refPH = refHome.child("ph");
        refNilaiPh = refPH.child("value");
        //------------NILAI DATA FIREBASE DARI SENSOR TDS-----------//
        refNutrisi = refHome.child("tds");
        refNilaiNutrisi = refNutrisi.child("value");



        //Inisialisasi object TextView
        tvNilaiSuhu = (TextView) findViewById(R.id.nilaiSuhu);
        tvNilaiIntenstitas = (TextView) findViewById(R.id.nilaiIntensitas);
        tvNilaiPH = (TextView) findViewById(R.id.nilaiPH);
        tvNilaiNutrisi = (TextView) findViewById(R.id.nilaiNutrisi);
        textViewStatusAir =findViewById(R.id.tvStatusAir);
        textViewStatusNutrisi =findViewById(R.id.tvStatusNutrisi);
        textViewStatusUV =findViewById(R.id.tvStatusUltraviolet);
        textViewStatusSprinkle =findViewById(R.id.tvStatusSpringkler);
        tvMonitoring = findViewById(R.id.tvMonitoring);
        fotoTumbuhan = findViewById(R.id.fotoTumbuhan);

        //inisiasisasi SWITCH
        switchOnOffAir = findViewById(R.id.switchAir);
        switchOnOffNutrisi = findViewById(R.id.switchNutrisi);
        switchOnOffUV = findViewById(R.id.switchUltraviolet);
        switchOnOffSprinkle = findViewById(R.id.switchSpringkler);



        monitoring(refNilaiNutrisi, tvNilaiNutrisi,refNilaiSuhu,tvNilaiSuhu,refNilaiPh,tvNilaiPH,refNilaiPhotoDioda,tvNilaiIntenstitas);
        monitorTumbuhan(refUrl,refName,tvMonitoring);
        controlling(refStatusAir,switchOnOffAir,textViewStatusAir,
                refStatusNutrisi,switchOnOffNutrisi,textViewStatusNutrisi,
                refStatusLampuUV,switchOnOffUV,textViewStatusUV,
                refStatusSprinkle,switchOnOffSprinkle,textViewStatusSprinkle);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    private void controlling(final DatabaseReference refStatusAir, final Switch switchOnOffAir, final TextView textViewStatusAir, final DatabaseReference refStatusNutrisi, final Switch switchOnOffNutrisi, final TextView textViewStatusNutrisi, final DatabaseReference refStatusLampuUV, final Switch switchOnOffUV, final TextView textViewStatusUV, final DatabaseReference refStatusSprinkle, final Switch switchOnOffSprinkle, final TextView textViewStatusSprinkle) {
        switchOnOffAir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                refStatusAir.setValue(isChecked);
                if(isChecked){
                    textViewStatusAir.setText("Nyala");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffAir, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Air Menyala", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }else{
                    textViewStatusAir.setText("Mati");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffAir, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Air Mati", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }
            }
        });

        refStatusAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){
                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Nyala");

                } else {

                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        switchOnOffNutrisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                refStatusNutrisi.setValue(isChecked);
                if(isChecked){
                    textViewStatusNutrisi.setText("Nyala");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffNutrisi, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Pompa Nutrisi Menyala", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }else{
                    textViewStatusNutrisi.setText("Mati");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffNutrisi, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Pompa Nutrisi Mati", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }
            }
        });

        refStatusNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){
                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Nyala");

                } else {

                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        switchOnOffUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                refStatusLampuUV.setValue(isChecked);
                if(isChecked){
                    textViewStatusUV.setText("Nyala");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Lampu UV Menyala", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }else{
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
        });

        refStatusLampuUV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                if(menyala){
                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Nyala");

                } else {

                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        switchOnOffSprinkle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                refStatusSprinkle.setValue(isChecked);
                if(isChecked){
                    textViewStatusSprinkle.setText("Nyala");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffSprinkle, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Springkler Menyala", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }else{
                    textViewStatusSprinkle.setText("Mati");
                    Snackbar snackbar = Snackbar
                            .make(switchOnOffSprinkle, "Informasi", Snackbar.LENGTH_LONG)
                            .setAction("Springkler Mati", new View.OnClickListener() {
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

                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void monitoring(DatabaseReference refNilaiNutrisi, final TextView tvNilaiNutrisi, DatabaseReference refNilaiSuhu, final TextView tvNilaiSuhu, DatabaseReference refNilaiPh, final TextView tvNilaiPH, DatabaseReference refNilaiPhotoDioda, final TextView tvNilaiIntenstitas) {
        showDialog();
        refNilaiNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideDialog();
                tvNilaiNutrisi.setText(dataSnapshot.getValue() + " PPM");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });refNilaiSuhu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNilaiSuhu.setText(dataSnapshot.getValue() + " °C");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });refNilaiPh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNilaiPH.setText(dataSnapshot.getValue() + " H");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });refNilaiPhotoDioda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNilaiIntenstitas.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void monitorTumbuhan(DatabaseReference url, DatabaseReference refUrl, final TextView tvMonitoring) {
        showDialog();
        refUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                Glide.with(getApplicationContext()).load(dataSnapshott.getValue()).placeholder(R.drawable.nophotos).apply(RequestOptions.circleCropTransform()).into(fotoTumbuhan);
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvMonitoring.setText(dataSnapshot.getValue().toString());
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
