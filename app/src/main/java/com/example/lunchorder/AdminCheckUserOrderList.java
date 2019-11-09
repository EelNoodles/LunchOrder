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

import com.example.lunchorder.Model.OrderList;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.OrderListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCheckUserOrderList extends AppCompatActivity {

    private TextView AdminCheckOrderListTitle, AdminCheckOrderListDetails;
    private ImageView AdminCheckorderlistbacksetting;
    private RecyclerView AdminCheckorderlistrecyclerview;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;

    private String AdminCheckUserName = "";
    private String AdminCheckUserNumber = "";
    private int totalprice = 0;
    private int totalamount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_user_order_list);

        AdminCheckUserName = getIntent().getStringExtra("AdminCheckUserName");
        AdminCheckUserNumber = getIntent().getStringExtra("AdminCheckUserNumber");

        AdminCheckOrderListTitle = (TextView) findViewById(R.id.AdminCheckOrderListTitle);
        AdminCheckorderlistbacksetting = (ImageView) findViewById(R.id.AdminCheckorderlistbacksetting);
        AdminCheckorderlistrecyclerview = (RecyclerView) findViewById(R.id.AdminCheckorderlistrecyclerview);
        AdminCheckOrderListDetails = (TextView) findViewById(R.id.AdminCheckOrderListDetails);

        recyclerview = findViewById(R.id.AdminCheckorderlistrecyclerview);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        AdminCheckorderlistbacksetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCheckUserOrderList.this, AdminStatsLobby.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderlistRef = FirebaseDatabase.getInstance().getReference().child("OrderList");

        FirebaseRecyclerOptions<OrderList> options = new FirebaseRecyclerOptions.Builder<OrderList>().setQuery(orderlistRef.child("User View")
                .child(AdminCheckUserNumber).child(Prevalent.DateFoodStore.getDate()), OrderList.class).build();

        FirebaseRecyclerAdapter<OrderList, OrderListViewHolder> adapter = new FirebaseRecyclerAdapter<OrderList, OrderListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderListViewHolder holder, int i, @NonNull OrderList orderList) {

                holder.orderlisttextviewname.setText(orderList.getFood());
                holder.orderlisttexticeandsweet.setText(orderList.getSweet() + " / " + orderList.getIce());
                holder.orderlisttextviewprice.setText(orderList.getPrice() + " 元");
                holder.orderlisttextviewamount.setText(orderList.getAmount() + " 個");

                int count = getItemCount();
                totalprice = 0;
                totalamount = 0;

                for (int a = 0; a < count ; a++){

                    int OneFoodPrice = ((Integer.valueOf(getItem(a).getPrice()))) * ((Integer.valueOf(getItem(a).getAmount())));
                    int OneFoodAmount = ((Integer.valueOf(getItem(a).getAmount())));

                    totalprice = totalprice + OneFoodPrice ;
                    totalamount = totalamount + OneFoodAmount;

                }

                AdminCheckOrderListTitle.setText(AdminCheckUserName + " " + Prevalent.DateFoodStore.getDate() + " 的餐點" );
                AdminCheckOrderListDetails.setText("總金額： " + totalprice + "  " + "總數量： " + totalamount);

            }

            @NonNull
            @Override
            public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlistdesign, parent , false);

                OrderListViewHolder holder = new OrderListViewHolder(view);

                return holder;

            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }
}
