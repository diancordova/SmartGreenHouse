package com.cordova.smartgreenhouse.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.cordova.smartgreenhouse.Adapter.HistoryAdapter;
import com.cordova.smartgreenhouse.Models.History;
import com.cordova.smartgreenhouse.Models.mHistory;
import com.cordova.smartgreenhouse.R;
import com.cordova.smartgreenhouse.RecyclerView.ClickListener;
import com.cordova.smartgreenhouse.RecyclerView.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class HistoriActivity<GraphView> extends AppCompatActivity {
    private static final String TAG = HistoriActivity.class.getSimpleName();
    private List<mHistory> listHistory;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter historyAdapter;
    DatabaseReference databaseReference, nutrisi;
    com.jjoe64.graphview.GraphView graphView;
    LineGraphSeries series;
    ProgressDialog pDialog;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);
//        nilaimaximal();
        databaseReference = FirebaseDatabase.getInstance().getReference("history");
        mRecycleView = findViewById(R.id.recycler_view);
        graphView = findViewById(R.id.graphView);
        GridLabelRenderer glr = graphView.getGridLabelRenderer();
        glr.setPadding(64);
        series= new LineGraphSeries();
        series.setColor(getResources().getColor(R.color.colorPrimary));
        graphView.addSeries(series);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setYAxisBoundsManual(true);
//        graphView.getViewport().setMinY(300);
//        graphView.getViewport().setMaxY(3000);
        graphView.getViewport().setScalable(true);

        listHistory = new ArrayList<>();
        mRecycleView.setHasFixedSize(true);
        historyAdapter = new HistoryAdapter(this,listHistory);
        layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        DataCalling();
        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecycleView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    protected void onStart() {
        super.onStart();
        showDialog();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideDialog();
                DataPoint[] dp=new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;
                int terbesar = 0;
                int terkecil = 0;

                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    History history = myDataSnapshot.getValue(History.class);
                    if (index == 0) {
                        terkecil = history.getNutrisi();
                    } else {
                        if (history.getNutrisi() < terkecil) {
                            terkecil = history.getNutrisi();
                        }
                    }
                    if (history.getNutrisi() > terbesar) {
                        terbesar = history.getNutrisi();
                    }
                    dp[index] = new DataPoint(index + 1, history.getNutrisi());
                    index++;
                }
                graphView.getViewport().setMinY(terkecil);
                graphView.getViewport().setMaxY(terbesar);
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DataCalling() {
        databaseReference.orderByChild("id").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHistory.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    mHistory lP = user.getValue(mHistory.class);
                    listHistory.add(lP);
                    mRecycleView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
