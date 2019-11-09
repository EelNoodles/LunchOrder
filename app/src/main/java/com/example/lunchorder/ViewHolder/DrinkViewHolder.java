package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView DisplayDrinkName, DisplayDrinkPrice;
    public ImageView DisplayDrinkView;
    public ItemClickListner listner;

    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);

        DisplayDrinkView = (ImageView) itemView.findViewById(R.id.DisplayImageDrinkView);
        DisplayDrinkName = (TextView) itemView.findViewById(R.id.DisplayDrinkName);
        DisplayDrinkPrice = (TextView) itemView.findViewById(R.id.DisplayDrinkPrice);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }

}
