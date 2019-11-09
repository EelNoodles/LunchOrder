package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class HomeWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView HomeWorkRecycleSubject, HomeWorkRecycleTitle, HomeWorkRecycleDeadLine;
    public ItemClickListner listner;

    public HomeWorkViewHolder(@NonNull View itemView) {
        super(itemView);

        HomeWorkRecycleSubject = (TextView) itemView.findViewById(R.id.HomeWorkRecycleSubject);
        HomeWorkRecycleTitle = (TextView) itemView.findViewById(R.id.HomeWorkRecycleTitle);
        HomeWorkRecycleDeadLine = (TextView) itemView.findViewById(R.id.HomeWorkRecycleDeadLine);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }
}
