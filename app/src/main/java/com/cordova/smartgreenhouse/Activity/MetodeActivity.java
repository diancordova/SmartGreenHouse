package com.cordova.smartgreenhouse.Activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class MetodeActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("metode");
    DatabaseReference refNutrisiKurang, refMaxKurangAir, refMaxKurangNutrisi, refMaxKurangNutrisiDanAir, refNutrisiNormal, refPhNetral, refNormal, refNutrisiLebih, refPhAsam, refPhBasa, refMaxNormal, refKurangNutrisi, refKurangAir, refLebihNurtrisi, refBobot, refStatus;
    TextView tvNutrisiKurang, tvBobot, tvStatus, tvNutrisiNormal, tvNutrisiLebih, tvPhAsam, tvPhNetral, tvPhBasa, tvMaxKurangNutrisi, tvMaxKurangAir, tvMaxNormal, tvMaxKurangNutrisidanAir;

    private NotificationManager notificationManager;
    private DatabaseReference database1;
    private ProgressDialog pDialog;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_, humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode);
        database1 = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        refNutrisiKurang = refHome.child("nutrisiKurang");
        refNutrisiNormal = refHome.child("nutrisiNormal");
        refNutrisiLebih = refHome.child("nutrisiLebih");
        refPhAsam = refHome.child("phAsam");
        refPhNetral = refHome.child("phNetral");
        refPhBasa = refHome.child("phBasa");
        refMaxNormal = refHome.child("maxNormal");
        refMaxKurangAir = refHome.child("maxKurangAir");
        refMaxKurangNutrisi = refHome.child("maxKurangNutrisi");
        refMaxKurangNutrisiDanAir = refHome.child("maxKurangNutrisiDanAir");
        refBobot = refHome.child("bobot");
        refStatus = refHome.child("keterangan");

        tvNutrisiKurang = findViewById(R.id.tvNutrisiKurang);
        tvNutrisiLebih = findViewById(R.id.tvNutrisiLebih);
        tvNutrisiNormal = findViewById(R.id.tvNutrisiNormal);
        tvPhAsam = findViewById(R.id.tvPHAsam);
        tvPhNetral = findViewById(R.id.tvPHNetral);
        tvPhBasa = findViewById(R.id.tvPHAsam);
        tvMaxNormal = findViewById(R.id.tvNormal);
        tvMaxKurangAir = findViewById(R.id.tvKurangAir);
        tvMaxKurangNutrisi = findViewById(R.id.tvKurangNutrisi);
        tvMaxKurangNutrisidanAir = findViewById(R.id.tvKurangNutrisidanAir);
        tvBobot = findViewById(R.id.bobot);
        tvStatus = findViewById(R.id.tvStatus);

        monitoringMetode(refNutrisiKurang, refNutrisiNormal, refNutrisiLebih, refPhAsam, refPhNetral, refPhBasa, refMaxNormal, refMaxKurangAir,
                refMaxKurangNutrisi, refMaxKurangNutrisiDanAir, refBobot, refStatus);

    }

    private void monitoringMetode(final DatabaseReference refNutrisiKurang, final DatabaseReference refNutrisiNormal, final DatabaseReference refNutrisiLebih, final DatabaseReference refPhAsam, final DatabaseReference refPhNetral, final DatabaseReference refPhBasa, final DatabaseReference refMaxNormal,
                                  final DatabaseReference refMaxKurangAir, final DatabaseReference refMaxKurangNutrisi, final DatabaseReference refMaxKurangNutrisiDanAir, final DatabaseReference refBobot, final DatabaseReference refStatus) {
        refNutrisiKurang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvNutrisiKurang.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisiNormal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvNutrisiNormal.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisiLebih.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvNutrisiLebih.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refPhAsam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvPhAsam.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refPhNetral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvPhNetral.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refMaxNormal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvMaxNormal.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refMaxKurangAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvMaxKurangAir.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refMaxKurangNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvMaxKurangNutrisi.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refMaxKurangNutrisiDanAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvMaxKurangNutrisidanAir.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refBobot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float nilai = Float.parseFloat(dataSnapshot.getValue().toString());
                String nilai2 = String.format("%.2f", nilai);
                tvBobot.setText(nilai2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tvStatus.setText(dataSnapshot.getValue() + " ");
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

    @Override
    public void onBackPressed() {
        this.finish();

    }
}