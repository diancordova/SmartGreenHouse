package com.cordova.smartgreenhouse.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cordova.smartgreenhouse.Models.mPlant;
import com.cordova.smartgreenhouse.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    List<mPlant> modelList;

    public CustomAdapter(Context context,List<mPlant> modelList){
        this.context = context;
        this.modelList = modelList;

    }


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tumbuhan, parent, false);
        CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder viewholder, final int position) {
        mPlant data = modelList.get(position);
        viewholder.nameP.setText(data.getName());
        viewholder.value1.setText(data.getFirstValue());
        viewholder.value2.setText(data.getSecondValue());
        Glide.with(context).load(data.getUrl()).placeholder(R.drawable.loading).apply(RequestOptions.circleCropTransform()).into(viewholder.imageP);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameP,value1,value2;
        public ImageView imageP;
        public ViewHolder(View itemView) {
            super(itemView);
            nameP = (TextView) itemView.findViewById(R.id.nameP);
            value1 = (TextView) itemView.findViewById(R.id.value1);
            value2 = (TextView) itemView.findViewById(R.id.value2);
            imageP = (ImageView) itemView.findViewById(R.id.imageP);
        }
    }
}
