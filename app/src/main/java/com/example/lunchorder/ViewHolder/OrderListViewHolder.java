package com.example.lunchorder.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.R;

public class OrderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView orderlisttextviewname, orderlisttextviewprice, orderlisttextviewamount, orderlisttexticeandsweet;
    public ImageView orderlistimageview;
    public ItemClickListner listner;

    public OrderListViewHolder(@NonNull View itemView) {
        super(itemView);

        orderlisttextviewname = (TextView) itemView.findViewById(R.id.orderlisttextviewname);
        orderlisttextviewprice = (TextView) itemView.findViewById(R.id.orderlisttextviewprice);
        orderlisttextviewamount = (TextView) itemView.findViewById(R.id.orderlisttextviewamount);
        orderlistimageview = (ImageView) itemView.findViewById(R.id.orderlistimageview);
        orderlisttexticeandsweet = (TextView) itemView.findViewById(R.id.orderlisttexticeandsweet);

    }

    public void setItemOnClickListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);

    }
}
