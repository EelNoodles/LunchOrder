package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

import java.util.ArrayList;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView DisplayFoodName, DisplayFoodPrice, DisplaySelectFoodStoreName, DisplaySelectDrinkStoreName;
    public ImageView DisplayFoodView;
    public ItemClickListner listner;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        DisplayFoodView = (ImageView) itemView.findViewById(R.id.DisplayImageFoodView);
        DisplayFoodName = (TextView) itemView.findViewById(R.id.DisplayFoodName);
        DisplayFoodPrice = (TextView) itemView.findViewById(R.id.DisplayFoodPrice);
        DisplaySelectFoodStoreName = (TextView) itemView.findViewById(R.id.DisplaySelectFoodStoreName);
        DisplaySelectDrinkStoreName = (TextView) itemView.findViewById(R.id.DisplaySelectDrinkStoreName);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }
}