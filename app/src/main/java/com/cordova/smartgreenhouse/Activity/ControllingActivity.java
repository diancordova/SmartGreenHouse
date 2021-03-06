package com.cordova.smartgreenhouse.Activity;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class ControllingActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private NotificationManager notificationManager;
    private DatabaseReference database1;
    DatabaseReference refHome = database.getReference("sensor");
    DatabaseReference refHome1 = database.getReference("control");
    DatabaseReference refDHT, refIpPublicValue, refTemp, refHmd, refPhotoDioda, refTumbuhan, refStatus, refStatusAir,
            refUrl, refStatusLampuUV, refSetting, refStatusPhotoDioda, refLampuUV, refSpringkle, refStatusSprinkle,
            refAir, refIpPublic, refNutrisii, refStatusManual, refManual, refStatusNutrisi, refUV, refDioda, refName, refIsLoading, refIsLoading1, refNilaiPhotoDioda, refPH, refNilaiPh, refStatusPH, refNutrisi, refNilaiNutrisi, refNilaiSuhu;
    private ProgressDialog pDialog;
    Switch switchOnOffUV,switchOnOffSprinkle,switchOnOffAir,switchOnOffNutrisi;
    TextView tvNilaiSuhu,tvNilaiIntenstitas,tvNilaiPH,tvNilaiNutrisi;
    TextView textViewStatusNutrisi,textViewStatusAir,textViewStatusUV,textViewStatusSprinkle,tvMonitoring;
    TextView cameraStatus;
    ImageView fotoTumbuhan;
    WebView webviewCam;
    private FirebaseAuth firebaseAuth;
    private SessionManager session;
    private String temperature_,humadity;
    private SQLiteHandler db;
    private ArrayAdapter<String> adapterPlant;
    Spinner spinnerAuto;
    List<String> list1;
    private ArrayAdapter<String> adapterAuto;
    SharedPreferences mSettings;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
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
        refNilaiPhotoDioda = refPhotoDioda.child("value");
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
        refManual = refHome1.child("manual");
        refStatusManual = refManual.child("status");

        refSetting = database.getReference("setting");
        refIpPublic = refSetting.child("ip_public");
        refIpPublicValue = refIpPublic.child("value");

        webviewCam = findViewById(R.id.webviewCam);
        webviewCam.getSettings().setSupportZoom(true);
        webviewCam.getSettings().setBuiltInZoomControls(true);
        webviewCam.getSettings().setDisplayZoomControls(false);


        //------------NILAI DATA FIREBASE DARI SENSOR PH-----------//
        refPH = refHome.child("ph");
        refNilaiPh = refPH.child("value");
        //------------NILAI DATA FIREBASE DARI SENSOR TDS-----------//
        refNutrisi = refHome.child("tds");
        refNilaiNutrisi = refNutrisi.child("value");



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvNilaiSuhu = findViewById(R.id.nilaiSuhu);
        tvNilaiIntenstitas = findViewById(R.id.nilaiIntensitas);
        tvNilaiPH = findViewById(R.id.nilaiPH);
        tvNilaiNutrisi = findViewById(R.id.nilaiNutrisi);
        textViewStatusAir =findViewById(R.id.tvStatusAir);
        textViewStatusNutrisi =findViewById(R.id.tvStatusNutrisi);
        textViewStatusUV =findViewById(R.id.tvStatusUltraviolet);
        textViewStatusSprinkle =findViewById(R.id.tvStatusSpringkler);
        tvMonitoring = findViewById(R.id.tvMonitoring);
        fotoTumbuhan = findViewById(R.id.fotoTumbuhan);
        cameraStatus = findViewById(R.id.cameraStatus);

        //inisiasisasi SWITCH
        switchOnOffAir = findViewById(R.id.switchAir);
        switchOnOffNutrisi = findViewById(R.id.switchNutrisi);
        switchOnOffUV = findViewById(R.id.switchUltraviolet);
        switchOnOffSprinkle = findViewById(R.id.switchSpringkler);

        list1 = new ArrayList<String>();
        spinnerAuto = findViewById(R.id.spinnerAuto);
        adapterAuto = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, list1);
        spinnerAuto.setAdapter(adapterAuto);

        list1.add("Automatis");
        list1.add("Manual");
        adapterAuto.notifyDataSetChanged();
        adapterAuto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        monitoring(refNilaiNutrisi, tvNilaiNutrisi, refNilaiSuhu, tvNilaiSuhu, refNilaiPh, tvNilaiPH, refNilaiPhotoDioda, tvNilaiIntenstitas, refStatusManual);
        monitorTumbuhan(refUrl, refName);
        monitorCamera(refIpPublicValue);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        status();


    }

    private void monitorCamera(DatabaseReference refIpPublicValue) {
        refIpPublicValue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                webviewCam.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        webviewCam.setVisibility(View.VISIBLE);
                        cameraStatus.setText("Camera");
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Canera Tidak Tersedia");

                        Log.d("FREEBROWSER", "----------------- ERROR deprecated ---------------");
                    }

                    @Override
                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Canera Tidak Tersedia");

                        Log.d("FREEBROWSER", "----------------- ERROR " + rerr.getDescription() + " ---------------");
                        // Redirect to deprecated method, so you can use it in all SDK versions
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                    }

                    @Override
                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Canera Tidak Tersedia");
                        Log.d("FREEBROWSER", "----------------- ERROR " + " ---------------");
                        onReceivedError(view, errorResponse.getStatusCode(), errorResponse.getReasonPhrase(), request.getUrl().toString());
                    }
                });
                webviewCam.loadUrl("http://" + dataSnapshot.getValue().toString() + ":8081");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refIpPublicValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                webviewCam.setWebViewClient((new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        webviewCam.setVisibility(View.VISIBLE);
                        cameraStatus.setText("Camera");

                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Camera Tidak Tersedia");
                        Log.d("FREEBROWSER", "----------------- ERROR deprecated ---------------");
                    }

                    @Override
                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Camera Tidak Tersedia");

                        Log.d("FREEBROWSER", "----------------- ERROR " + rerr.getDescription() + " ---------------");
                        // Redirect to deprecated method, so you can use it in all SDK versions
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                    }

                    @Override
                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                        webviewCam.setVisibility(View.GONE);
                        cameraStatus.setText("Camera Tidak Tersedia");

                        Log.d("FREEBROWSER", "----------------- ERROR " + " ---------------");
                        onReceivedError(view, errorResponse.getStatusCode(), errorResponse.getReasonPhrase(), request.getUrl().toString());
                    }
                }));
                webviewCam.loadUrl("http://" + dataSnapshot.getValue().toString() + ":8081");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void status() {
        refStatusManual.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString() == "true") {
                    spinnerAuto.setSelection(1);
                } else {
                    spinnerAuto.setSelection(0);
                }
                spinnerAuto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position == 0) {
                            refStatusManual.setValue(false);
                        } else {
                            refStatusManual.setValue(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        refStatusAir.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                if (menyala) {
                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Nyala");

                } else {
                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Mati");
                }
                statusAir();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refStatusNutrisi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                if (menyala) {
                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Nyala");

                } else {

                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Mati");
                }
                statusNutrisi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refStatusLampuUV.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                if (menyala) {
                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Nyala");

                } else {

                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Mati");
                }
                statusLampuUV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refStatusSprinkle.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                if (menyala) {
                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Nyala");

                } else {

                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Mati");
                }
                statusSprinkle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void controlling(final DatabaseReference refStatusAir, final Switch switchOnOffAir, final TextView textViewStatusAir, final DatabaseReference refStatusNutrisi, final Switch switchOnOffNutrisi, final TextView textViewStatusNutrisi, final DatabaseReference refStatusLampuUV, final Switch switchOnOffUV, final TextView textViewStatusUV, final DatabaseReference refStatusSprinkle, final Switch switchOnOffSprinkle, final TextView textViewStatusSprinkle) {
    }

    void statusAir() {
        refStatusAir.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                switchOnOffAir.setOnCheckedChangeListener(null);
                if (menyala) {
                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Nyala");

                } else {

                    switchOnOffAir.setChecked(menyala);
                    textViewStatusAir.setText("Mati");
                }
                switchOnOffAir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (spinnerAuto.getSelectedItemPosition() == 0) {
                            Toast.makeText(ControllingActivity.this, "Mode Anda Harus Manual", Toast.LENGTH_SHORT).show();
                            switchOnOffAir.setChecked(!switchOnOffAir.isChecked());
                        } else {
                            switchOnOffAir.setChecked(switchOnOffAir.isChecked());
                            refStatusAir.setValue(isChecked);
                            if (isChecked) {
                                textViewStatusAir.setText("Nyala");
                                switchOnOffAir.setChecked(true);
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffAir, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Pompa Air Menyala", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            } else {
                                textViewStatusAir.setText("Mati");
                                switchOnOffAir.setChecked(false);
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Pompa Air Mati", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void statusNutrisi() {
        refStatusNutrisi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                switchOnOffNutrisi.setOnCheckedChangeListener(null);
                if (menyala) {
                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Nyala");

                } else {

                    switchOnOffNutrisi.setChecked(menyala);
                    textViewStatusNutrisi.setText("Mati");
                }

                switchOnOffNutrisi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (spinnerAuto.getSelectedItemPosition() == 0) {
                            Toast.makeText(ControllingActivity.this, "Mode Anda Harus Manual", Toast.LENGTH_SHORT).show();
                            switchOnOffNutrisi.setChecked(!switchOnOffNutrisi.isChecked());
                        } else {
                            switchOnOffNutrisi.setChecked(switchOnOffNutrisi.isChecked());
                            refStatusNutrisi.setValue(isChecked);
                            if (isChecked) {
                                textViewStatusNutrisi.setText("Nyala");
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffNutrisi, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Pompa Nutrisi Menyala", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            } else {
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
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void statusLampuUV() {
        refStatusLampuUV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                switchOnOffUV.setOnCheckedChangeListener(null);
                if (menyala) {
                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Nyala");

                } else {

                    switchOnOffUV.setChecked(menyala);
                    textViewStatusUV.setText("Mati");
                }
                switchOnOffUV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (spinnerAuto.getSelectedItemPosition() == 0) {
                            Toast.makeText(ControllingActivity.this, "Mode Anda Harus Manual", Toast.LENGTH_SHORT).show();
                            switchOnOffUV.setChecked(!switchOnOffUV.isChecked());
                        } else {
                            switchOnOffUV.setChecked(switchOnOffUV.isChecked());
                            refStatusLampuUV.setValue(isChecked);
                            if (isChecked) {
                                textViewStatusUV.setText("Nyala");
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffUV, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Lampu UV Menyala", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            } else {
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
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void statusSprinkle() {
        refStatusSprinkle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean menyala = (Boolean) dataSnapshot.getValue();
                switchOnOffSprinkle.setOnCheckedChangeListener(null);
                if (menyala) {
                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Nyala");

                } else {

                    switchOnOffSprinkle.setChecked(menyala);
                    textViewStatusSprinkle.setText("Mati");
                }
                switchOnOffSprinkle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (spinnerAuto.getSelectedItemPosition() == 0) {
                            Toast.makeText(ControllingActivity.this, "Mode Anda Harus Manual", Toast.LENGTH_SHORT).show();
                            switchOnOffSprinkle.setChecked(!switchOnOffSprinkle.isChecked());
                        } else {
                            switchOnOffSprinkle.setChecked(switchOnOffSprinkle.isChecked());
                            refStatusSprinkle.setValue(isChecked);
                            if (isChecked) {
                                textViewStatusSprinkle.setText("Nyala");
                                Snackbar snackbar = Snackbar
                                        .make(switchOnOffSprinkle, "Informasi", Snackbar.LENGTH_LONG)
                                        .setAction("Springkler Menyala", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });

                                snackbar.show();
                            } else {
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
                    }


                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void monitoring(DatabaseReference refNilaiNutrisi, final TextView tvNilaiNutrisi, DatabaseReference refNilaiSuhu, final TextView tvNilaiSuhu, DatabaseReference refNilaiPh, final TextView tvNilaiPH, DatabaseReference refNilaiPhotoDioda, final TextView tvNilaiIntenstitas, final DatabaseReference refStatusManual) {
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
        refStatusManual.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("cordovaganteng", "ganti");
                if (dataSnapshot.getValue().toString() == "true") {
                    spinnerAuto.setSelection(1);
                } else {
                    spinnerAuto.setSelection(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void monitorTumbuhan(final DatabaseReference refUrl, final DatabaseReference refName) {
        refUrl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                Glide.with(getApplicationContext()).load(dataSnapshott.getValue()).placeholder(R.drawable.nophotos).apply(RequestOptions.circleCropTransform()).into(fotoTumbuhan);
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
            pDialog.setMessage("Loading ....");
        pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
//class WebviewCamku extends WebViewClient{
//
//
//        @Override
//        @TargetApi(Build.VERSION_CODES.M)
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
////            super.onReceivedError(view, request, error);
//            final Uri uri = request.getUrl();
//            handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
//        }
//
//        @SuppressWarnings("deprecation")
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////            super.onReceivedError(view, errorCode, description, failingUrl);
//            final Uri uri = Uri.parse(failingUrl);
//            handleError(view, errorCode, description, uri);
//        }
//
//        private void handleError(WebView view, int errorCode, String description, final Uri uri) {
//            final String host = uri.getHost();// e.g. "google.com"
//            final String scheme = uri.getScheme();// e.g. "https"
//            // TODO: logic
//        }
//    }