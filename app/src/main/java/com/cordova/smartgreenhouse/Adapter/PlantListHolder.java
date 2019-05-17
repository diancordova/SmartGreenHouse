package com.cordova.smartgreenhouse.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cordova.smartgreenhouse.Models.Plant;
import com.cordova.smartgreenhouse.R;

public  class PlantListHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    public View mView;
    public Plant my = new Plant();
    public  TextView firstValue;
    public  TextView secondValue;
    public  TextView name;


        View view;

        public PlantListHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            firstValue = (TextView) itemView.findViewById(R.id.row_fname);
            secondValue = (TextView) itemView.findViewById(R.id.row_first_value);
            name = (TextView) itemView.findViewById(R.id.row_nama);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            linearLayout = itemView.findViewById(R.id.row_plant_lists);


            view = itemView.findViewById(R.id.row_plant_lists);

        }

    }

