package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class AdminStatsOrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView adminstatsfoodorderlistdesignname, adminstatsfoodorderlistdesignprice, adminstatsdrinkorderlistdesignname, adminstatsdrinkorderlistdesignsweet, adminstatsdrinkorderlistdesignprice, adminstatsdrinkorderlistdesigntotalprice, adminstatsfoodorderlistdesigntotalprice ;
    public ItemClickListner listner;

    public AdminStatsOrderListViewHolder(@NonNull View itemView) {
        super(itemView);

        adminstatsfoodorderlistdesignname = (TextView) itemView.findViewById(R.id.adminstatsfoodorderlistdesignname);
        adminstatsfoodorderlistdesignprice = (TextView) itemView.findViewById(R.id.adminstatsfoodorderlistdesignprice);
        adminstatsdrinkorderlistdesignname = (TextView) itemView.findViewById(R.id.adminstatsdrinkorderlistdesignname);
        adminstatsdrinkorderlistdesignsweet = (TextView) itemView.findViewById(R.id.adminstatsdrinkorderlistdesignsweet);
        adminstatsdrinkorderlistdesignprice = (TextView) itemView.findViewById(R.id.adminstatsdrinkorderlistdesignprice);
        adminstatsdrinkorderlistdesigntotalprice = (TextView) itemView.findViewById(R.id.adminstatsdrinkorderlistdesigntotalprice);
        adminstatsfoodorderlistdesigntotalprice = (TextView) itemView.findViewById(R.id.adminstatsfoodorderlistdesigntotalprice);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }

}
