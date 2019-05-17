package com.cordova.smartgreenhouse.Activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cordova.smartgreenhouse.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlAirActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refRelay, refStatus;

    ToggleButton btnToggle;
    TextView textEstadoPulsador;
    Switch switchOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_air);
        refRelay = refHome.child("relay");
        refStatus = refRelay.child("status");



        btnToggle = (ToggleButton)  findViewById(R.id.toggleButton);
        btnToggle.setTextOn("NYALA");
        btnToggle.setTextOff("MATI");
        switchOnOff = findViewById(R.id.switch1);

        textEstadoPulsador = (TextView) findViewById(R.id.textViewPulsador);

        controlRelay(refStatus, btnToggle);


    }

    private void controlRelay(final DatabaseReference refRelay, final ToggleButton toggle_btn) {
        toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                refRelay.setValue(isChecked);
            }
        });
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                refRelay.setValue(isChecked);
            }
        });

        refRelay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala  = (Boolean) dataSnapshot.getValue();
                switchOnOff.setChecked(menyala);
                toggle_btn.setChecked(menyala);
                if(menyala){
                    toggle_btn.setTextOn("NYALA");
                    switchOnOff.setChecked(menyala);
                    Snackbar snackbar = Snackbar
                            .make(switchOnOff, "Pompa A Menyala!", Snackbar.LENGTH_LONG)
                            .setAction("Sistem Automasi Mati Sementara", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                } else {
                    toggle_btn.setTextOff("MATI");
                    switchOnOff.setChecked(false);
                    switchOnOff.setChecked(menyala);
                    Snackbar snackbar = Snackbar
                            .make(switchOnOff, "Pompa Air Mati", Snackbar.LENGTH_LONG)
                            .setAction("Sistem Automasi Nyala Kembali", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) { }

        });
    }
}
