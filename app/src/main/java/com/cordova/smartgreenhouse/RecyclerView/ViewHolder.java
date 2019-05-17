package com.cordova.smartgreenhouse.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cordova.smartgreenhouse.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mNamePlant,mFirstValue,mSecondValue;
    public View mView;


    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return false;
            }
        });
        mNamePlant =itemView.findViewById(R.id.name_plant);
        mFirstValue=itemView.findViewById(R.id.value_first);
        mSecondValue=itemView.findViewById(R.id.value_second);
    }
    private ViewHolder.ClickListener mClickListener;

    public void setOnClickListener(ClickListener viewHolder) {
    }


    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void seOnClickListener(ClickListener mClickListener) {
        mClickListener = mClickListener;
    }
}
