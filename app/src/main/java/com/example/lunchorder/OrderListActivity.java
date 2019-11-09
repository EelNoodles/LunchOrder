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
import com.example.lunchorder.ViewHolder.FoodViewHolder;
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

public class OrderListActivity extends AppCompatActivity {

    private TextView OrderListTitle, OrderListDetails;
    private ImageView orderlistbacksetting;
    private RecyclerView orderlistrecyclerview;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private int totalprice = 0;
    private int totalamount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        OrderListTitle = (TextView) findViewById(R.id.OrderListTitle);
        orderlistbacksetting = (ImageView) findViewById(R.id.orderlistbacksetting);
        orderlistrecyclerview = (RecyclerView) findViewById(R.id.orderlistrecyclerview);
        OrderListDetails = (TextView) findViewById(R.id.OrderListDetails);

        recyclerview = findViewById(R.id.orderlistrecyclerview);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        OrderListTitle.setText("訂餐總表" + " ( " + Prevalent.DateFoodStore.getDate() + " )");

        orderlistbacksetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BackToSetting();

            }
        });
    }

    private void GetTotalPrice(){

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference orderlistRef = FirebaseDatabase.getInstance().getReference().child("OrderList");

        final DatabaseReference totalpriceRef = FirebaseDatabase.getInstance().getReference().child("Users");

        totalpriceRef.child(Prevalent.DateFoodStore.getDate()).child(Prevalent.RightOnlineUser.getNumber()).removeValue();

        FirebaseRecyclerOptions<OrderList> options = new FirebaseRecyclerOptions.Builder<OrderList>().setQuery(orderlistRef.child("User View")
                .child(Prevalent.RightOnlineUser.getNumber()).child(Prevalent.DateFoodStore.getDate()), OrderList.class).build();

        FirebaseRecyclerAdapter<OrderList, OrderListViewHolder> adapter = new FirebaseRecyclerAdapter<OrderList, OrderListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderListViewHolder holder, int i, @NonNull final OrderList orderList) {

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

                OrderListDetails.setText("總金額： " + totalprice + "  " + "總數量： " + totalamount);

                String StringTotalPrice = String.valueOf(totalprice);

                final HashMap<String, Object> TotalPrice = new HashMap<>();

                TotalPrice.put("Name", Prevalent.RightOnlineUser.getName());
                TotalPrice.put("Number", Prevalent.RightOnlineUser.getNumber());
                TotalPrice.put("TotalPrice", StringTotalPrice);

                totalpriceRef.child(Prevalent.DateFoodStore.getDate()).child(Prevalent.RightOnlineUser.getNumber()).updateChildren(TotalPrice);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        orderlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                CharSequence options[] = new CharSequence[]{

                                        "刪除。"

                                };


                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
                                builder.setTitle("餐點動作");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){

                                            Food foodtype = dataSnapshot.child("User View").child(Prevalent.RightOnlineUser.getNumber()).child(Prevalent.DateFoodStore.getDate()).child(orderList.getFood()).getValue(Food.class);

                                            if(foodtype == null){

                                                final Food drinkdateTotalAmount = dataSnapshot.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Drink").child(orderList.getIce() + orderList.getSweet() + orderList.getFood()).getValue(Food.class);
                                                final String stringdrinkdateTotalAmount = drinkdateTotalAmount.getTotalAmount();

                                                orderlistRef.child("User View").child(Prevalent.RightOnlineUser.getNumber()).child(Prevalent.DateFoodStore.getDate()).child(orderList.getIce() + orderList.getSweet() + orderList.getFood()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){

                                                            int aftertotalamount = ((Integer.valueOf(stringdrinkdateTotalAmount))) - ((Integer.valueOf(orderList.getAmount())));
                                                            String stringaftertotalamount = String.valueOf(aftertotalamount);

                                                            final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                                                            adminorderlistMap.put("TotalAmount", stringaftertotalamount);

                                                            orderlistRef.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Drink").child(orderList.getIce() + orderList.getSweet() + orderList.getFood()).updateChildren(adminorderlistMap);

                                                            if(stringaftertotalamount.equals("0")){

                                                                orderlistRef.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Drink").child(orderList.getIce() + orderList.getSweet() + orderList.getFood()).removeValue();

                                                            }

                                                            Toast.makeText(OrderListActivity.this, "此餐點已經刪除。", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(OrderListActivity.this, OrderListActivity.class);
                                                            startActivity(intent);

                                                        }

                                                    }
                                                });

                                            }else{

                                                final Food fooddateTotalAmount = dataSnapshot.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Food").child(orderList.getFood()).getValue(Food.class);
                                                final String stringfooddateTotalAmount = fooddateTotalAmount.getTotalAmount();

                                                orderlistRef.child("User View").child(Prevalent.RightOnlineUser.getNumber()).child(Prevalent.DateFoodStore.getDate()).child(orderList.getFood()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){

                                                            int aftertotalamount = ((Integer.valueOf(stringfooddateTotalAmount))) - ((Integer.valueOf(orderList.getAmount())));
                                                            String stringaftertotalamount = String.valueOf(aftertotalamount);

                                                            final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                                                            adminorderlistMap.put("TotalAmount", stringaftertotalamount);

                                                            orderlistRef.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Food").child(orderList.getFood()).updateChildren(adminorderlistMap);

                                                            if(stringaftertotalamount.equals("0")){

                                                                orderlistRef.child("Admin View").child(Prevalent.DateFoodStore.getDate()).child("Food").child(orderList.getFood()).removeValue();

                                                            }

                                                            Toast.makeText(OrderListActivity.this, "此餐點已經刪除。", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(OrderListActivity.this, OrderListActivity.class);
                                                            startActivity(intent);

                                                        }

                                                    }
                                                });

                                            }

                                        }

                                    }
                                });

                                builder.show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

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

    public void BackToSetting(){

        Intent intent = new Intent(OrderListActivity.this, SettingActivity.class);
        startActivity(intent);

        Toast.makeText(OrderListActivity.this, "回到訂餐選單。", Toast.LENGTH_SHORT).show();

    }
}
