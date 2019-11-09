package com.example.lunchorder.ViewHolder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class RollCallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView usernumber, username, status;
    public ItemClickListner listner;

    public RollCallViewHolder(@NonNull View itemView) {
        super(itemView);

        usernumber = (TextView) itemView.findViewById(R.id.usernumber);
        username = (TextView) itemView.findViewById(R.id.username);
        status = (TextView) itemView.findViewById(R.id.status);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }
}
