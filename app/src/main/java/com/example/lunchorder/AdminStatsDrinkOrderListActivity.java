package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.OrderList;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.AdminStatsOrderListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class AdminStatsDrinkOrderListActivity extends AppCompatActivity {

    private TextView AdminStatsDrinkOrderListTitle, AdminStatsDrinkOrderListPrice;
    private ImageView AdminStatsDrinkOrderListBackImage;
    private RecyclerView AdminStatsDrinkOrderListRecycle;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private int totalprice = 0;
    private int totalamount = 0;

    private String AdminClass = Paper.book().read("AdminClass").toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats_drink_order_list);

        AdminStatsDrinkOrderListTitle = (TextView) findViewById(R.id.AdminStatsDrinkOrderListTitle);
        AdminStatsDrinkOrderListRecycle = (RecyclerView) findViewById(R.id.AdminStatsDrinkOrderListRecycle);
        AdminStatsDrinkOrderListPrice = (TextView) findViewById(R.id.AdminStatsDrinkOrderListPrice);
        AdminStatsDrinkOrderListBackImage = (ImageView) findViewById(R.id.AdminStatsDrinkOrderListBackImage);

        recyclerview = findViewById(R.id.AdminStatsDrinkOrderListRecycle);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        AdminStatsDrinkOrderListTitle.setText(AdminClass + " 午餐訂餐總表" + " ( " + Prevalent.DateFoodStore.getDate() + " )");

        AdminStatsDrinkOrderListBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BackToSetting();

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderlistRef = FirebaseDatabase.getInstance().getReference().child("OrderList");


        final FirebaseRecyclerOptions<OrderList> options = new FirebaseRecyclerOptions.Builder<OrderList>().setQuery(orderlistRef.child("Admin View").child(AdminClass)
                .child(Prevalent.DateFoodStore.getDate()).child("Drink"), OrderList.class).build();

        FirebaseRecyclerAdapter<OrderList, AdminStatsOrderListViewHolder> adapter = new FirebaseRecyclerAdapter<OrderList, AdminStatsOrderListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminStatsOrderListViewHolder holder, int i, @NonNull final OrderList orderList) {

                holder.adminstatsdrinkorderlistdesignname.setText(orderList.getFood());
                holder.adminstatsdrinkorderlistdesigntotalprice.setText(orderList.getTotalAmount() + " 個");
                holder.adminstatsdrinkorderlistdesignprice.setText(orderList.getPrice() + " 元");
                holder.adminstatsdrinkorderlistdesignsweet.setText(orderList.getSweet() + " / " + orderList.getIce());

                int count = getItemCount();
                totalprice = 0;
                totalamount = 0;

                for (int a = 0; a < count ; a++){

                    int OneFoodPrice = ((Integer.valueOf(getItem(a).getPrice()))) * ((Integer.valueOf(getItem(a).getTotalAmount())));
                    int OneFoodAmount = ((Integer.valueOf(getItem(a).getTotalAmount())));

                    totalprice = totalprice + OneFoodPrice ;
                    totalamount = totalamount + OneFoodAmount;

                }

                AdminStatsDrinkOrderListPrice.setText("總金額： " + totalprice + "  " + "總數量： " + totalamount);

            }

            @NonNull
            @Override
            public AdminStatsOrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminstatsdrinkorderlistdesign, parent , false);

                AdminStatsOrderListViewHolder holder = new AdminStatsOrderListViewHolder(view);

                return holder;

            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

    public void BackToSetting(){

        Intent intent = new Intent(AdminStatsDrinkOrderListActivity.this, AdminStatsLobby.class);
        startActivity(intent);

        Toast.makeText(AdminStatsDrinkOrderListActivity.this, "回到訂餐選單。", Toast.LENGTH_SHORT).show();

    }

}
