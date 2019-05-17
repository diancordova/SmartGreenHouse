package com.cordova.smartgreenhouse.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.cordova.smartgreenhouse.Adapter.HistoryAdapter;
import com.cordova.smartgreenhouse.Models.mHistory;
import com.cordova.smartgreenhouse.R;
import com.cordova.smartgreenhouse.RecyclerView.ClickListener;
import com.cordova.smartgreenhouse.RecyclerView.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HistoriActivity extends AppCompatActivity {
    private static final String TAG = HistoriActivity.class.getSimpleName();
    private List<mHistory> listHistory;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter historyAdapter;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_plant);

        databaseReference = FirebaseDatabase.getInstance().getReference("history");
        mRecycleView = findViewById(R.id.recycler_view);

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



    private void DataCalling() {
        databaseReference.orderByChild("jam").addValueEventListener(new ValueEventListener() {

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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(HistoriActivity.this, UserActivityDrawer.class);
        startActivity(i);
    }
}
