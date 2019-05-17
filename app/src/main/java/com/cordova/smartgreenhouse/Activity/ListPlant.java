package com.cordova.smartgreenhouse.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cordova.smartgreenhouse.Adapter.CustomAdapter;
import com.cordova.smartgreenhouse.Models.mPlant;
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


public class ListPlant extends AppCompatActivity {
    private static final String TAG = ListPlant.class.getSimpleName();
    private List<mPlant> listPlant1;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter plantAdapter;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_plant);

        databaseReference = FirebaseDatabase.getInstance().getReference("plant");
        mRecycleView = findViewById(R.id.recycler_view);
        Toast.makeText(getApplicationContext(), "Tekan untuk menghapus", Toast.LENGTH_SHORT).show();

        listPlant1 = new ArrayList<>();
        mRecycleView.setHasFixedSize(true);
        plantAdapter = new CustomAdapter(this,listPlant1);
        layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        setTitle("List Plant");
        DataCalling();

        mRecycleView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecycleView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                mPlant m = listPlant1.get(position);
                Intent i = new Intent (getApplicationContext(),EditPlant.class);
                i.putExtra("nama", m.name);
                i.putExtra("value1",m.firstValue);
                i.putExtra("value2",m.secondValue);
                i.putExtra("uId",m.uid);
                i.putExtra("url",m.url);

                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {
                mPlant lP = listPlant1.get(position);
                KonfirmasiHapus(lP);
            }
        }));

    }

    private void KonfirmasiHapus(final mPlant lP) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Hapus Tumbuhan");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mPlant plant = new mPlant();
                        databaseReference.child(lP.getUid()).setValue(plant);
                        Toast.makeText(getApplicationContext(), "Terhapus", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void DataCalling() {
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPlant1.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    mPlant lP = user.getValue(mPlant.class);
                    listPlant1.add(lP);
                    mRecycleView.setAdapter(plantAdapter);
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
        Intent i=new Intent(ListPlant.this, UserActivityDrawer.class);
        startActivity(i);
    }
}
