package com.cordova.smartgreenhouse.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.cordova.smartgreenhouse.Controller.SQLiteHandler;
import com.cordova.smartgreenhouse.Controller.SessionManager;
import com.cordova.smartgreenhouse.Models.mPlant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.cordova.smartgreenhouse.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refDHT, refHumadity, refTemp, refHmd,refName;
    private DatabaseReference databaseReference, databaseLog;
    private StorageReference storageReference;
    private ArrayAdapter<String> adapterPlant;
    ToggleButton btnToggle;
    TextView textViewTemp,textViewHMD, textViewPlant;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private FirebaseAuth firebaseAuth;
    Spinner spinner;
    private SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("plant");
        databaseLog = FirebaseDatabase.getInstance().getReference().child("log");
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        setTitle("Real");
        refDHT = refHome.child("-LY0QTRSNhlYug_7Ev_k");
        refTemp = refDHT.child("firstValue");
        refName = refDHT.child("name");

        refHumadity = refHome.child("-LY0QTRSNhlYug_7Ev_k");
        refHmd = refHumadity.child("secondValue");

        textViewPlant = findViewById(R.id.textViewPlant);
        textViewTemp = (TextView) findViewById(R.id.textViewTemperature);
        textViewHMD = (TextView) findViewById(R.id.textViewHumidity);
        monitorTemperature(refTemp, textViewTemp);
        monitorHMD(refHmd,textViewHMD);
        monitorName(refName,textViewPlant);
    }





    private void monitorName(final DatabaseReference refName_, final TextView textViewPlant) {
        refName_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double temp = (Double) dataSnapshot.getValue();
                textViewPlant.setText("Lettuce");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }
    //
private void monitorTemperature(final DatabaseReference refTemp_, final TextView textViewTemp) {
    refTemp_.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Double temp = (Double) dataSnapshot.getValue();
            textViewTemp.setText("1200");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) { }
    });

}
    private void monitorHMD(final DatabaseReference refHmd_, final TextView textViewHMD) {
        refHmd_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double humidity = (Double) dataSnapshot.getValue();
                textViewHMD.setText("1400");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }
}