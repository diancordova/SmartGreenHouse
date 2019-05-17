package com.cordova.smartgreenhouse.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cordova.smartgreenhouse.Adapter.PlantListHolder;

import com.cordova.smartgreenhouse.Models.Plant;
import com.cordova.smartgreenhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataList extends AppCompatActivity {
    private RecyclerView recycler_plant_lists;
    public FirebaseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        recycler_plant_lists = (RecyclerView) findViewById(R.id.recycler_plant_list);
        fetchProfileLists();
    }
    private void fetchProfileLists() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        recycler_plant_lists.setHasFixedSize(true);
        recycler_plant_lists.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirebaseRecyclerAdapter<Plant, PlantListHolder>(Plant.class, R.layout.row_list_plant, PlantListHolder.class, ref.child("plant")) {
            protected void populateViewHolder(final PlantListHolder viewHolder, final Plant model, final int position) {
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (!userid.matches(model.getUid())) {
                    viewHolder.mainLayout.setVisibility(View.VISIBLE);
                    viewHolder.linearLayout.setVisibility(View.VISIBLE);
                    viewHolder.firstValue.setText(model.getFirstValue());
                    viewHolder.secondValue.setText(model.getSecondValue());
                    viewHolder.name.setText(model.getNama());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = model.getUid();
                            Intent i=new Intent(DataList.this, UserActivity.class);
                            i.putExtra("user_id",id);
                            startActivity(i);
                        }
                    });


                }

            }


        };


        recycler_plant_lists.setAdapter(adapter);
    }

    private class FirebaseRecyclerAdapter<P, P1 extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
        public FirebaseRecyclerAdapter(Class<P> plantClass, int row_list_plant, Class<P1> plantListHolderClass, DatabaseReference plant) {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
