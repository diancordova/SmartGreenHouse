package com.cordova.smartgreenhouse.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cordova.smartgreenhouse.Controller.SQLiteHandler;
import com.cordova.smartgreenhouse.Controller.SessionManager;
import com.cordova.smartgreenhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MonitoringActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refMetode = database.getReference("metode");
    DatabaseReference refPH, refNilaiPh, refTDS, refNilaiNhisutrisi, refWaterLevel, refNilaiWaterLevel,
            refSuhuAir, refNilaiSuhuAir, refNutrisi, refStatusPH, refStatusNutrisi, refStatusWaterLevel, refStatusSuhuAir, refStatusMetode, refTumbuhan, refNilaiNutrisi,
    refNama,refUrl,refNutrisi1,refNutrisi2;
    TextView textViewPH,textViewNutrisi,textViewWaterLevel,textViewTumbuhan,textViewSuhuAir,textViewTumbuhan1,
            textStatusPH,textStatusNutrisi,textStatusWaterLevel,textStatusSuhuAir,textViewNilai1,textViewNilai2;
    ImageView fotoTumbuhan2;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    List<String> list1;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        setTitle("Monitoring");

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);


        refStatusMetode= refMetode.child("keterangan");
        //------------NILAI DATA FIREBASE DARI SENSOR PH-----------//
        refPH = refHome.child("ph");
        refNilaiPh = refPH.child("value");
        refStatusPH = refPH.child("status");
        //------------NILAI DATA FIREBASE DARI SENSOR TDS-----------//
        refNutrisi = refHome.child("tds");
        refNilaiNutrisi = refNutrisi.child("value");
        refStatusNutrisi = refNutrisi.child("status");
        //------------NILAI DATA FIREBASE DARI SENSOR WATER LEVEL-----------//
        refWaterLevel = refHome.child("ph");
        refStatusWaterLevel = refWaterLevel.child("status");
        //------------NILAI DATA FIREBASE DARI SENSOR SUHU AIR-----------//
        refSuhuAir = refHome.child("dht");
        refNilaiSuhuAir = refSuhuAir.child("temp");
        refNilaiWaterLevel = refSuhuAir.child("humidity");
        refStatusSuhuAir = refSuhuAir.child("status");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        refTumbuhan = database.getReference("pilihTumbuhan");
        refNama = refTumbuhan.child("name");
        refUrl = refTumbuhan.child("url");
        refNutrisi1 = refTumbuhan.child("firstValue");
        refNutrisi2 =refTumbuhan.child("secondValue");


        textViewPH = findViewById(R.id.tvPh1);
        textViewNutrisi = findViewById(R.id.tvNutrisi1);
        textViewWaterLevel = findViewById(R.id.tvWaterLevel1);
        textViewSuhuAir = findViewById(R.id.tvSuhuAir1);
        textViewTumbuhan = findViewById(R.id.tvMonitoring);
        textViewTumbuhan1 = findViewById(R.id.tvTumbuhan);
        textViewNilai1 = findViewById(R.id.nilaiPH);
        textViewNilai2 = findViewById(R.id.status);
        fotoTumbuhan2=findViewById(R.id.fotoTumbuhan);


        textStatusPH = findViewById(R.id.tvPH);
        textStatusNutrisi = findViewById(R.id.value1);
        textStatusWaterLevel = findViewById(R.id.tvWaterLevel);
        textStatusSuhuAir = findViewById(R.id.tvSuhuAir);



        monitorTumbuhan(refStatusMetode,refNama,refNutrisi1,refNutrisi2,refUrl,textViewTumbuhan,textViewTumbuhan1,textViewNilai1,textViewNilai2);
        monitorPH(refNilaiPh, textViewPH);
        monitorStatusPH(refStatusPH, textStatusPH);
        monitorNutrisi(refNilaiNutrisi,textViewNutrisi);
        monitorStatusNutrisi(refStatusNutrisi,textStatusNutrisi);
        monitorWaterLevel(refNilaiWaterLevel,textViewWaterLevel);
        monitorStatusWaterLevel(refStatusWaterLevel,textStatusWaterLevel);
        monitorSuhuAir(refNilaiSuhuAir,textViewSuhuAir);
        monitorStatusSuhuAir(refStatusSuhuAir,textStatusSuhuAir);



    }
    @Override
    public void onBackPressed() {
        this.finish();

    }
    private void monitorTumbuhan(final DatabaseReference refStatusMetode,final DatabaseReference refNama,
                                 final DatabaseReference refNutrisi1,final DatabaseReference refNutrisi2,final DatabaseReference refUrl,
                                 final TextView textViewTumbuhan,final TextView textViewTumbuhan1,final TextView textViewNilai1 ,final TextView textViewNilai2) {
        refStatusMetode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTumbuhan.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNama.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTumbuhan1.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisi1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewNilai1.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisi2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewNilai2.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                Glide.with(getApplicationContext()).load(dataSnapshott.getValue()).placeholder(R.drawable.loading).apply(RequestOptions.circleCropTransform()).into(fotoTumbuhan2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void monitorPH(final DatabaseReference refNilaiPh, final TextView textViewPH) {
        refNilaiPh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewPH.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void monitorStatusPH(final DatabaseReference refStatusPH, final TextView textStatusPH) {
        refStatusPH.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Keasaman",dataSnapshot.getValue().toString());
                int nilai =  ((Long)dataSnapshot.getValue()).intValue();
                if(nilai==0){
                    textStatusPH.setText("Asam");
                    textStatusPH.setBackgroundColor(Color.parseColor("#f44242"));
                }else if(nilai==1){
                    textStatusPH.setText("Netral");
                    textStatusPH.setBackgroundColor(Color.parseColor("#82ea3c"));
                }else{
                    textStatusPH.setText("Basa");
                    textStatusPH.setBackgroundColor(Color.parseColor("#28fffb"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void monitorNutrisi(final DatabaseReference refNilaiNutrisi, final TextView textViewNutrisi) {
        refNilaiNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewNutrisi.setText(dataSnapshot.getValue() + " PPM");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void monitorStatusNutrisi(final DatabaseReference refStatusNutrisi, final TextView textStatusNutrisi) {
        refStatusNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nilai =  ((Long)dataSnapshot.getValue()).intValue();
                if(nilai==0){
                    textStatusNutrisi.setText("Kurang");
                    textStatusNutrisi.setBackgroundColor(Color.parseColor("#f44242"));
                }else if(nilai==1){
                    textStatusNutrisi.setText("Normal");
                    textStatusNutrisi.setBackgroundColor(Color.parseColor("#82ea3c"));
                }else{
                    textStatusNutrisi.setText("Lebih");
                    textStatusNutrisi.setBackgroundColor(Color.parseColor("#f44242"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void monitorWaterLevel(final DatabaseReference refNilaiWaterLevel, final TextView textViewWaterLevel) {
        refNilaiWaterLevel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textViewWaterLevel.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void monitorStatusWaterLevel(final DatabaseReference refStatusWaterLevel,final TextView textStatusWaterLevel) {
        refStatusWaterLevel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("suhu",dataSnapshot.getValue().toString());
                int nilai =  ((Long)dataSnapshot.getValue()).intValue();
                if(nilai==0){
                    textStatusWaterLevel.setText("Terang");
                    textStatusWaterLevel.setBackgroundColor(Color.parseColor("#82ea3c" +
                            ""));
                }else if(nilai==1){
                    textStatusWaterLevel.setText("Terang");
                    textStatusWaterLevel.setBackgroundColor(Color.parseColor("#82ea3c"));
                }else{
                    textStatusWaterLevel.setText("Lebih");
                    textStatusWaterLevel.setBackgroundColor(Color.parseColor("#f44242"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void monitorSuhuAir(final DatabaseReference refSuhuAir, final TextView  textViewSuhuAir) {

        refSuhuAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                textViewSuhuAir.setText(dataSnapshot.getValue() + " °C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    private void monitorStatusSuhuAir(final DatabaseReference refStatusSuhuAir, final TextView textStatusSuhuAir) {
        refStatusSuhuAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Suhu",dataSnapshot.getValue().toString());
                int nilai =  ((Long)dataSnapshot.getValue()).intValue();
                if(nilai==0){
                    textStatusSuhuAir.setText("Panas");
                    textStatusSuhuAir.setBackgroundColor(Color.parseColor("#f44242"));
                }else if(nilai==1){
                    textStatusSuhuAir.setText("Normal");
                    textStatusSuhuAir.setBackgroundColor(Color.parseColor("#82ea3c"));
                }else{
                    textStatusSuhuAir.setText("Dingin");
                    textStatusSuhuAir.setBackgroundColor(Color.parseColor("#28fffb"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Log.d("tutup", "tutup");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
