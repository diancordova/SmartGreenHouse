package com.cordova.smartgreenhouse.Activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.cordova.smartgreenhouse.Controller.SQLiteHandler;
import com.cordova.smartgreenhouse.Controller.SessionManager;
import com.cordova.smartgreenhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MetodeActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refHome1 = database.getReference("control");
    DatabaseReference refDHT, refTemp, refHmd, refPhotoDioda, refTumbuhan, refStatus, refStatusAir,
            refUrl, refStatusLampuUV, refStatusPhotoDioda, refLampuUV, refSpringkle, refStatusSprinkle,
            refAir, refNutrisii, refStatusManual, refStatusNutrisi, refUV, refDioda, refName, refIsLoading, refIsLoading1, refNilaiPhotoDioda, refPH, refNilaiPh, refStatusPH, refNutrisi, refNilaiNutrisi, refNilaiSuhu;
    Switch switchOnOffUV, switchOnOffSprinkle, switchOnOffAir, switchOnOffNutrisi;
    TextView tvNilaiSuhu, tvNilaiIntenstitas, tvNilaiPH, tvNilaiNutrisi;
    TextView textViewStatusNutrisi, textViewStatusAir, textViewStatusUV, textViewStatusSprinkle, tvMonitoring;
    ImageView fotoTumbuhan;
    List<String> list1;
    private NotificationManager notificationManager;
    private DatabaseReference database1;
    private ProgressDialog pDialog;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_, humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;

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

    }
}
