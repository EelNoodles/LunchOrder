package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Model.OrderList;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.AdminStatsOrderListViewHolder;
import com.example.lunchorder.ViewHolder.OrderListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class AdminStatsFoodOrderListActivity extends AppCompatActivity {

    private TextView AdminStatsFoodOrderListTitle, AdminStatsFoodOrderListPrice;
    private ImageView AdminStatsFoodOrderListBackImage;
    private RecyclerView AdminStatsFoodOrderListRecycle;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private int totalprice = 0;
    private int totalamount = 0;

    private String AdminClass = Paper.book().read("AdminClass").toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats_food_order_list);

        AdminStatsFoodOrderListTitle = (TextView) findViewById(R.id.AdminStatsFoodOrderListTitle);
        AdminStatsFoodOrderListRecycle = (RecyclerView) findViewById(R.id.AdminStatsFoodOrderListRecycle);
        AdminStatsFoodOrderListPrice = (TextView) findViewById(R.id.AdminStatsFoodOrderListPrice);
        AdminStatsFoodOrderListBackImage = (ImageView) findViewById(R.id.AdminStatsFoodOrderListBackImage);

        recyclerview = findViewById(R.id.AdminStatsFoodOrderListRecycle);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        AdminStatsFoodOrderListTitle.setText(AdminClass + " 午餐訂餐總表" + " ( " + Prevalent.DateFoodStore.getDate() + " )");

        AdminStatsFoodOrderListBackImage.setOnClickListener(new View.OnClickListener() {
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


        FirebaseRecyclerOptions<OrderList> options = new FirebaseRecyclerOptions.Builder<OrderList>().setQuery(orderlistRef.child("Admin View").child(AdminClass)
                .child(Prevalent.DateFoodStore.getDate()).child("Food"), OrderList.class).build();

        FirebaseRecyclerAdapter<OrderList, AdminStatsOrderListViewHolder> adapter = new FirebaseRecyclerAdapter<OrderList, AdminStatsOrderListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminStatsOrderListViewHolder holder, int i, @NonNull final OrderList orderList) {

                holder.adminstatsfoodorderlistdesignname.setText(orderList.getFood());
                holder.adminstatsfoodorderlistdesigntotalprice.setText(orderList.getTotalAmount() + " 個");
                holder.adminstatsfoodorderlistdesignprice.setText(orderList.getPrice() + " 元");

                int count = getItemCount();
                totalprice = 0;
                totalamount = 0;

                for (int a = 0; a < count ; a++){

                    int OneFoodPrice = ((Integer.valueOf(getItem(a).getPrice()))) * ((Integer.valueOf(getItem(a).getTotalAmount())));
                    int OneFoodAmount = ((Integer.valueOf(getItem(a).getTotalAmount())));

                    totalprice = totalprice + OneFoodPrice ;
                    totalamount = totalamount + OneFoodAmount;

                }

                AdminStatsFoodOrderListPrice.setText("總金額： " + totalprice + "  " + "總數量： " + totalamount);

            }

            @NonNull
            @Override
            public AdminStatsOrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminstatsfoodorderlistdesign, parent , false);

                AdminStatsOrderListViewHolder holder = new AdminStatsOrderListViewHolder(view);

                return holder;

            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

    public void BackToSetting(){

        Intent intent = new Intent(AdminStatsFoodOrderListActivity.this, AdminStatsLobby.class);
        startActivity(intent);

        Toast.makeText(AdminStatsFoodOrderListActivity.this, "回到訂餐選單。", Toast.LENGTH_SHORT).show();

    }

}
