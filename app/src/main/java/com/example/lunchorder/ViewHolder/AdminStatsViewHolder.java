package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class AdminStatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView adminstatslobbynumber, adminstatslobbymame, adminstatslobbyprice;
    public ItemClickListner listner;

    public AdminStatsViewHolder(@NonNull View itemView) {
        super(itemView);

        adminstatslobbynumber = (TextView) itemView.findViewById(R.id.adminstatslobbynumber);
        adminstatslobbymame = (TextView) itemView.findViewById(R.id.adminstatslobbymame);
        adminstatslobbyprice = (TextView) itemView.findViewById(R.id.adminstatslobbyprice);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }

}
