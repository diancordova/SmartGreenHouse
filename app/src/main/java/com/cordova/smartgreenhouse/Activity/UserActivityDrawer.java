package com.cordova.smartgreenhouse.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.cordova.smartgreenhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public class UserActivityDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refDHT, refHumadity, refTemp,
            refHmd,refTumbuhan,refNama,refUrl,refPompaA,refPompaB,
            refStatusPompaA,refStatusPompsB,refPompaAisLoading,refPompaBisLoading,
            refNutrisi1,refNutrisi2;
    List<mPlant> listmPlant;
    ImageView fotoTumbuhan;
    private DatabaseReference databaseReference, databaseLog;
    CardView cvMonitor,cvControl,cvMetode,cvHelp;
    TextView textViewTemp,textViewHMD,textViewTumbuhan,textViewPompaA,textViewPompaB,nilai1,nilai2;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    Spinner spinner;
    List<String> list1;
    Switch switchA,switchB;
    SliderLayout sliderLayout;
    String dataTerpilih;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSettings.edit();
        editor.apply();

        setTitle("");
        database1 = FirebaseDatabase.getInstance().getReference();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        list1 = new ArrayList<String>();
        listmPlant = new ArrayList<mPlant>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_drawer);
        spinner = findViewById(R.id.lokasiSpinner);
//        spinner.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        fotoTumbuhan = findViewById(R.id.fotoTumbuhan);
        cvMonitor=findViewById(R.id.cvMonitor);
        cvControl=findViewById(R.id.cvControl);
        cvMetode=findViewById(R.id.cvMetode);
        cvHelp=findViewById(R.id.cvHelp);

        refPompaA = refHome.child("relay1");
        refPompaB = refHome.child("relay2");
        refStatusPompaA = refPompaA.child("status");
        refStatusPompsB = refPompaB.child("status");
        refPompaAisLoading = refPompaA.child("isLoading");
        refPompaBisLoading = refPompaB.child("isLoading");


        startService(new Intent(getApplicationContext(), MyService.class));
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :
        setSliderViews();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant mPlantTerpilih=listmPlant.get(i);
                mPlant pl =  new mPlant(mPlantTerpilih.name, mPlantTerpilih.firstValue, mPlantTerpilih.secondValue
                        ,mPlantTerpilih.uid, mPlantTerpilih.url);
                editor.putString("nama",mPlantTerpilih.name );
                editor.apply();
                database1.child("pilihTumbuhan").setValue(pl);
                Toast.makeText(UserActivityDrawer.this,"Tumbuhan terpilih adalah "+mPlantTerpilih.name, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cvMonitor.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MonitoringActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });
        cvControl.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ControllingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });

        cvMetode.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MetodeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });
        cvHelp.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        HistoriActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("plant");
        databaseLog = FirebaseDatabase.getInstance().getReference().child("log");
        firebaseAuth = FirebaseAuth.getInstance();

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        adapterPlant = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, list1);
        adapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterPlant);
        spinnerStatus();
        refDHT = refHome.child("dht");
        refTemp = refDHT.child("temp");

        refHumadity = refHome.child("dht");
        refHmd = refHumadity.child("humidity");

        refTumbuhan = database.getReference("pilihTumbuhan");
        refNama = refTumbuhan.child("name");
//        DatabaseReference namanya = Datab.child("pi")
        refUrl = refTumbuhan.child("url");
        refNutrisi1 = refTumbuhan.child("firstValue");
        refNutrisi2 =refTumbuhan.child("secondValue");

        nilai1 = findViewById(R.id.nilaiPH);
        nilai2 = findViewById(R.id.status);
        textViewPompaA = findViewById(R.id.tvTumbuhan);
        textViewPompaB = findViewById(R.id.tvMetode);
        textViewTumbuhan = findViewById(R.id.tvMonitoring);
        monitorTumbuhan(refNama,refUrl,refNutrisi1,refNutrisi2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }








    private void monitorTumbuhan(final DatabaseReference refTumbuhanPilihan,final DatabaseReference refUrl,final DatabaseReference refNutrisi1,final DatabaseReference refNutrisi2) {
        showDialog();
        refTumbuhanPilihan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTumbuhan.setText(dataSnapshot.getValue() + " ");
                hideDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                Glide.with(getApplicationContext()).load(dataSnapshott.getValue()).placeholder(R.drawable.nophotos).apply(RequestOptions.circleCropTransform()).into(fotoTumbuhan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisi1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nilai1.setText(dataSnapshot.getValue()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refNutrisi2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nilai2.setText(dataSnapshot.getValue()+" PPM");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void spinnerStatus(){

        showDialog();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list1.clear();
                listmPlant.clear();
                int i=0;
                for (DataSnapshot plant : dataSnapshot.getChildren()) {
                    mPlant lP = plant.getValue(mPlant.class);
                    listmPlant.add(lP);
                    list1.add(lP.getName());
                    adapterPlant.notifyDataSetChanged();

                    String cookieName = mSettings.getString("nama", "Sawi Pakcoy");

                    Log.d("tumbuhan", cookieName);

                    if (lP.getName().equals(cookieName)){
                        spinner.setSelection(i);

                    }

                    i++;
                }
                hideDialog();
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
        } else if (id == R.id.settings) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            return true;
        } else if (id == R.id.log_out) {
            KonfirmasiLogout();
            return true;
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



        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://innovate2019.tinc.id/assets/uploads/greenhouse1.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    sliderView.setDescription("Implementasi Sprinkler guna mendinginkan suhu yang berada didalam Greenhouse ");
                    break;
                case 1:
                    sliderView.setImageUrl("https://innovate2019.tinc.id/assets/uploads/greenhouse2.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    sliderView.setDescription("Penggunaan teknik hidroponik pada tumbuhan sawi pakcoy");
                    break;
                case 2:
                    sliderView.setImageUrl("https://innovate2019.tinc.id/assets/uploads/greenhouse3.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    sliderView.setDescription("Implementasi Lampu UltraViolet guna meningkatkan mutu tumbuhan hidroponik  " );
                    break;
                case 3:
                    sliderView.setImageUrl("https://innovate2019.tinc.id/assets/uploads/greenhouse4.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    sliderView.setDescription("Implementasi Sistem Panel BOX" );
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            final int finalI = i;

            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {

                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.setMessage("Loading ....");
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
