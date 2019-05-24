package com.cordova.smartgreenhouse.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cordova.smartgreenhouse.Controller.SQLiteHandler;
import com.cordova.smartgreenhouse.Controller.SessionManager;
import com.cordova.smartgreenhouse.Models.mPlant;
import com.cordova.smartgreenhouse.MyService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.cordova.smartgreenhouse.R;

import java.util.ArrayList;
import java.util.List;

public class UserActivityDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refDHT, refHumadity, refTemp,
            refHmd,refTumbuhan,refNama,refUrl,refPompaA,refPompaB,
            refStatusPompaA,refStatusPompsB,refPompaAisLoading,refPompaBisLoading;
    List<mPlant> listmPlant;
    ImageView fotoTumbuhan;
    private DatabaseReference databaseReference, databaseLog;
    TextView textViewTemp,textViewHMD,textViewTumbuhan,textViewPompaA,textViewPompaB;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    Spinner spinner;
    List<String> list1;
    Switch switchA,switchB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        database1 = FirebaseDatabase.getInstance().getReference();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        list1 = new ArrayList<String>();
        listmPlant = new ArrayList<mPlant>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_drawer);
        spinner = findViewById(R.id.lokasiSpinner);
        fotoTumbuhan = (ImageView) findViewById(R.id.foto1);
//        switchA=findViewById(R.id.switchUltraviolet);
//        switchB=findViewById(R.id.switchInsaknet);

        refPompaA = refHome.child("relay1");
        refPompaB = refHome.child("relay2");
        refStatusPompaA = refPompaA.child("status");
        refStatusPompsB = refPompaB.child("status");
        refPompaAisLoading = refPompaA.child("isLoading");
        refPompaBisLoading = refPompaB.child("isLoading");

//        controlPompaA(refStatusPompaA);
//        controlPompaB(refStatusPompsB);

        startService(new Intent(getApplicationContext(), MyService.class));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant mPlantTerpilih=listmPlant.get(i);
                mPlant pl =  new mPlant(mPlantTerpilih.name, mPlantTerpilih.firstValue, mPlantTerpilih.secondValue
                        ,mPlantTerpilih.uid, mPlantTerpilih.url);
                database1.child("pilihTumbuhan").setValue(pl);
                Toast.makeText(UserActivityDrawer.this,"Tumbuhan terpilih adalah "+mPlantTerpilih.name, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("plant");
        databaseLog = FirebaseDatabase.getInstance().getReference().child("log");
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        adapterPlant = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list1);
        adapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterPlant);
        spinnerStatus();
        setTitle("RealTime Monitor");
        refDHT = refHome.child("dht");
        refTemp = refDHT.child("temp");

        refHumadity = refHome.child("dht");
        refHmd = refHumadity.child("humidity");

        refTumbuhan = database.getReference("pilihTumbuhan");
        refNama = refTumbuhan.child("name");
        refUrl = refTumbuhan.child("url");




        textViewTemp = (TextView) findViewById(R.id.tvPh1);
        textViewHMD = (TextView) findViewById(R.id.value2);
        textViewPompaA = findViewById(R.id.tvTumbuhan);
        textViewPompaB = findViewById(R.id.tvMetode);
        textViewTumbuhan = findViewById(R.id.tvMonitoring);
        monitorTumbuhan(refNama,refUrl,textViewTumbuhan);
        monitorTemperature(refTemp, textViewTemp);
        monitorHMD(refHmd,textViewHMD);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    private void controlPompaA(final DatabaseReference refStatusPompaA) {
        refStatusPompaA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean nyala = (Boolean) dataSnapshot.getValue();
                if (nyala){
                    textViewPompaA.setText("Nyala");
                }else{
                    textViewPompaA.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        switchA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked) {
//                    refStatusPompaA.setValue(isChecked);
//                    refPompaAisLoading.setValue(isChecked);
//                    refPompaAisLoading.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
//                            if(isLoading){
//                                showDialog();
//                            }else{
//                                hideDialog();
//                                textViewPompaA.setText("Nyala");
//                                Snackbar snackbar = Snackbar
//                                        .make(switchA, "Informasi", Snackbar.LENGTH_LONG)
//                                        .setAction("Pompa A Menyala", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                            }
//                                        });
//
//                                snackbar.show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }else{
//                    refStatusPompaA.setValue(isChecked);
//                    refPompaAisLoading.setValue(!isChecked);
//                    refPompaAisLoading.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
//                            if(isLoading){
//                                showDialog();
//                            }else{
//                                hideDialog();
//                                textViewPompaA.setText("Mati");
//                                Snackbar snackbar = Snackbar
//                                        .make(switchA, "Informasi", Snackbar.LENGTH_LONG)
//                                        .setAction("Pompa A MATI", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                            }
//                                        });
//
//                                snackbar.show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//
//                }
//            }
//        });

    }
    private void controlPompaB(final DatabaseReference refStatusPompsB) {
        refStatusPompsB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean nyala = (Boolean) dataSnapshot.getValue();
                if (nyala){
                    textViewPompaB.setText("Nyala");
                }else{
                    textViewPompaB.setText("Mati");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked) {
//                    refStatusPompsB.setValue(isChecked);
//                    refPompaBisLoading.setValue(isChecked);
//                    refPompaBisLoading.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
//                            if(isLoading){
//                                showDialog();
//                            }else{
//                                hideDialog();
//                                textViewPompaB.setText("Nyala");
//                                Snackbar snackbar = Snackbar
//                                        .make(switchB, "Informasi", Snackbar.LENGTH_LONG)
//                                        .setAction("Pompa B Menyala", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                            }
//                                        });
//
//                                snackbar.show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }else{
//                    refStatusPompsB.setValue(isChecked);
//                    textViewPompaB.setText("Mati");
//                    refPompaBisLoading.setValue(!isChecked);
//                    refPompaBisLoading.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Boolean isLoading = (Boolean) dataSnapshot.getValue();
//                            if(isLoading){
//                                showDialog();
//                            }else{
//                                hideDialog();
//                                textViewPompaB.setText("Mati");
//                                Snackbar snackbar = Snackbar
//                                        .make(switchB, "Informasi", Snackbar.LENGTH_LONG)
//                                        .setAction("Pompa B Mati", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                            }
//                                        });
//
//                                snackbar.show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//
//                }
//            }
//        });
    }

    private void monitorTumbuhan(final DatabaseReference refTumbuhanPilihan,final DatabaseReference refUrl, final TextView textViewTumbuhan) {
        refTumbuhanPilihan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTumbuhan.setText(dataSnapshot.getValue() + " ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                Glide.with(getApplicationContext()).load(dataSnapshott.getValue()).placeholder(R.drawable.loading).apply(RequestOptions.circleCropTransform()).into(fotoTumbuhan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void monitorTemperature(final DatabaseReference refTemp_, final TextView textViewTemp) {
        refTemp_.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Double temp = (Double) dataSnapshot.getValue();
                textViewTemp.setText(dataSnapshot.getValue().toString() + " C");
                Log.d("UserActivityDrawer_155",dataSnapshot.getValue().toString());
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void spinnerStatus(){



        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list1.clear();
                listmPlant.clear();
                for (DataSnapshot plant : dataSnapshot.getChildren()) {
                    mPlant lP = plant.getValue(mPlant.class);
                    listmPlant.add(lP);
                    list1.add(lP.getName().toString());
                    adapterPlant.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_activity_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.DataTumbuhan) {
            return true;
        }else if (id == R.id.AddData) {
            return true;
        }else if (id == R.id.history) {
            return true;
        }else if (id==R.id.hidroPonik){
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void KonfirmasiLogout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Anda ingin logout?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Logout",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        logoutUser();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        firebaseAuth.signOut();

        Intent intent = new Intent(UserActivityDrawer.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {

            case R.id.DataTumbuhan:
                Intent h = new Intent(UserActivityDrawer.this, ListPlant.class);
                startActivity(h);
                break;
            case R.id.AddData:
                Intent i = new Intent(UserActivityDrawer.this,PlantActivity.class);
                startActivity(i);
                break;
            case R.id.history:
                Intent g = new Intent(UserActivityDrawer.this, HistoriActivity.class);
                startActivity(g);
                break;
            case R.id.controlling:
                Intent j = new Intent(UserActivityDrawer.this, ControlAirActivity.class);
                startActivity(j);
                break;
            case R.id.monitoring:
                Intent o = new Intent(UserActivityDrawer.this, ControlNutrisiActivity.class);
                startActivity(o);
                break;
            case R.id.greenHouse:
                Intent k = new Intent(UserActivityDrawer.this, ActivityMonitorGreenHouse.class);
                startActivity(k);
                break;
            case R.id.hidroPonik:
                Intent l = new Intent(UserActivityDrawer.this, ControlNutrisiActivity.class);
                startActivity(l);
                break;
            case R.id.metod:
                Intent n = new Intent(UserActivityDrawer.this, ActivityMonitorGreenHouse.class);
                startActivity(n);
                break;
            case R.id.aboout:
                Intent m = new Intent(UserActivityDrawer.this, ControlNutrisiActivity.class);
                startActivity(m);
                break;

            case R.id.logout:
                KonfirmasiLogout();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
