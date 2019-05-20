package com.cordova.smartgreenhouse.Activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

public class ControlNutrisiActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refMetode = database.getReference("metode");
    DatabaseReference refPH,refNilaiPh,refTDS,refNilaiNutrisi,refWaterLevel,refNilaiWaterLevel,
            refSuhuAir,refNilaiSuhuAir,refNutrisi,refStatusPH,refStatusNutrisi,refStatusWaterLevel,refStatusSuhuAir,refStatusMetode;
    TextView textViewPH,textViewNutrisi,textViewWaterLevel,textViewTumbuhan,textViewSuhuAir,
            textStatusPH,textStatusNutrisi,textStatusWaterLevel,textStatusSuhuAir;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    List<String> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_nutrisi);


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
        refNilaiWaterLevel = refWaterLevel.child("value");
        refStatusWaterLevel = refWaterLevel.child("status");
        //------------NILAI DATA FIREBASE DARI SENSOR SUHU AIR-----------//
        refSuhuAir = refHome.child("dht");
        refNilaiSuhuAir = refSuhuAir.child("temp");
        refStatusSuhuAir = refSuhuAir.child("status");


        textViewPH = (TextView) findViewById(R.id.tvPh1);
        textViewNutrisi = (TextView) findViewById(R.id.tvNutrisi1);
        textViewWaterLevel = (TextView) findViewById(R.id.tvWaterLevel1);
        textViewSuhuAir = (TextView) findViewById(R.id.tvSuhuAir1);
        textViewTumbuhan= (TextView) findViewById(R.id.tvTumbuhan);

        textStatusPH = (TextView) findViewById(R.id.tvPH);
        textStatusNutrisi = (TextView) findViewById(R.id.tvNutrisi);
        textStatusWaterLevel = (TextView) findViewById(R.id.tvWaterLevel);
        textStatusSuhuAir = (TextView) findViewById(R.id.tvSuhuAir);



        monitorTumbuhan(refStatusMetode,textViewTumbuhan);
        monitorPH(refNilaiPh, textViewPH);
        monitorStatusPH(refStatusPH, textStatusPH);
        monitorNutrisi(refNilaiNutrisi,textViewNutrisi);
        monitorStatusNutrisi(refStatusNutrisi,textStatusNutrisi);
        monitorWaterLevel(refNilaiWaterLevel,textViewWaterLevel);
        monitorStatusWaterLevel(refStatusWaterLevel,textStatusWaterLevel);
        monitorSuhuAir(refNilaiSuhuAir,textViewSuhuAir);
        monitorStatusSuhuAir(refStatusSuhuAir,textStatusSuhuAir);

    }

    private void monitorTumbuhan(final DatabaseReference refTumbuhanPilihan, final TextView textViewTumbuhan) {
        refTumbuhanPilihan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTumbuhan.setText(dataSnapshot.getValue() + " ");
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
                textViewWaterLevel.setText(dataSnapshot.getValue() + " CM");
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
                    textStatusWaterLevel.setText("Kurang");
                    textStatusWaterLevel.setBackgroundColor(Color.parseColor("#f44242"));
                }else if(nilai==1){
                    textStatusWaterLevel.setText("Normal");
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
}
