package com.cordova.smartgreenhouse.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cordova.smartgreenhouse.Models.mHistory;
import com.cordova.smartgreenhouse.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<mHistory> modelList;

    public HistoryAdapter(Context context,List<mHistory> modelList){
        this.context = context;
        this.modelList = modelList;

    }


    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer posisi = position + 1;
        mHistory data = modelList.get(position);
        holder.nomor.setText(posisi.toString());
        holder.tanggal.setText(data.getTanggal());
        holder.waktu.setText(data.getWaktu());
        holder.nutrisi.setText( Integer.toString(data.getNutrisi()));
        holder.ph.setText(Integer.toString(data.getPh()));
        holder.suhu.setText(Integer.toString(data.getSuhu()));

    }




    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggal, waktu, nutrisi, ph, suhu, nomor;
        public ViewHolder(View itemView) {
            super(itemView);
            nomor = itemView.findViewById(R.id.nomor);
            tanggal = itemView.findViewById(R.id.nameP);
            waktu = itemView.findViewById(R.id.nilaiPH);
            nutrisi = itemView.findViewById(R.id.text);
            ph = itemView.findViewById(R.id.nilai2);
            suhu = itemView.findViewById(R.id.value);
        }
    }
}
