package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FoodDetailsActivity extends AppCompatActivity {

    private Button AddToOrderList;
    private TextView FoodDetailsPrice, FoodDetailsName;
    private ElegantNumberButton amountbutton;
    private String FoodID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        FoodID = getIntent().getStringExtra("FoodID");

        AddToOrderList = (Button) findViewById(R.id.AddToOrderList);
        FoodDetailsName = (TextView) findViewById(R.id.FoodDetailsName);
        FoodDetailsPrice = (TextView) findViewById(R.id.FoodDetailsPrice);
        amountbutton = (ElegantNumberButton) findViewById(R.id.FoodDetailsElegantNumberButton);

        GetFoodDetails(FoodID);

        AddToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(amountbutton.getNumber().equals("0")){

                    Toast.makeText(FoodDetailsActivity.this, "請增加商品數量。", Toast.LENGTH_SHORT).show();

                }else{

                    addtoorderlist();

                }
            }
        });

    }

    private void addtoorderlist() {

        final String UserNumber = Prevalent.RightOnlineUser.getNumber();
        final String UserClass = Prevalent.RightOnlineUser.getUserClass();
        final String Date = Prevalent.DateFoodStore.getDate();
        final String FoodName = FoodDetailsName.getText().toString();
        final String amount = amountbutton.getNumber();

        final DatabaseReference orderlistRef = FirebaseDatabase.getInstance().getReference().child("OrderList");

        orderlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("User View").child(UserClass).child(UserNumber).child(Date).child(FoodName).exists()) {

                    Food dateTotalAmount = dataSnapshot.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).getValue(Food.class);
                    Food dateAmount = dataSnapshot.child("User View").child(UserClass).child(UserNumber).child(Date).child(FoodName).getValue(Food.class);
                    final String stringdateTotalAmount = dateTotalAmount.getTotalAmount();
                    final String stringdateAmount = dateAmount.getAmount();

                    int beforeamount = ((Integer.valueOf(stringdateTotalAmount))) - ((Integer.valueOf(stringdateAmount)));
                    int totalamount = ((Integer.valueOf(beforeamount))) + ((Integer.valueOf(amount)));

                    String stringbeforeamount = String.valueOf(beforeamount);
                    String stringtotalamount = String.valueOf(totalamount);

                    final HashMap<String, Object> orderlistMap = new HashMap<>();
                    final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                    orderlistMap.put("Food", FoodName);
                    orderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                    orderlistMap.put("Amount", amount);
                    orderlistMap.put("Types", "Food");

                    adminorderlistMap.put("Food", FoodName);
                    adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                    adminorderlistMap.put("TotalAmount", stringtotalamount);

                    orderlistRef.child("User View").child(UserClass).child(UserNumber).child(Date).child(FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            orderlistRef.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(FoodDetailsActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(FoodDetailsActivity.this, OrderListActivity.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    });

                }else{

                    if(dataSnapshot.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).exists()){

                        Food dateTotalAmount = dataSnapshot.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).getValue(Food.class);
                        final String stringdateTotalAmount = dateTotalAmount.getTotalAmount();
                        int totalamount = ((Integer.valueOf(stringdateTotalAmount))) + ((Integer.valueOf(amount)));

                        String stringtotalamount = String.valueOf(totalamount);


                        final HashMap<String, Object> orderlistMap = new HashMap<>();
                        final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                        orderlistMap.put("Food", FoodName);
                        orderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        orderlistMap.put("Amount", amount);
                        orderlistMap.put("Types", "Food");

                        adminorderlistMap.put("Food", FoodName);
                        adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        adminorderlistMap.put("TotalAmount", stringtotalamount);

                        orderlistRef.child("User View").child(UserClass).child(UserNumber).child(Date).child(FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                orderlistRef.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(FoodDetailsActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(FoodDetailsActivity.this, OrderListActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        });

                    }else{

                        int totalamount = ((Integer.valueOf(amount).intValue()));
                        String stringtotalamount = String.valueOf(totalamount);

                        final HashMap<String, Object> orderlistMap = new HashMap<>();
                        final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                        orderlistMap.put("Food", FoodName);
                        orderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        orderlistMap.put("Amount", amount);
                        orderlistMap.put("Types", "Food");

                        adminorderlistMap.put("Food", FoodName);
                        adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        adminorderlistMap.put("TotalAmount", stringtotalamount);

                        orderlistRef.child("User View").child(UserClass).child(UserNumber).child(Date).child(FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                orderlistRef.child("Admin View").child(UserClass).child(Date).child("Food").child(FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(FoodDetailsActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(FoodDetailsActivity.this, OrderListActivity.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        });

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetFoodDetails(String FoodID){

        final String DateFoodStore = Prevalent.DateFoodStore.getFoodStore();

        DatabaseReference FoodRef = FirebaseDatabase.getInstance().getReference().child("Food");

        FoodRef.child("Food").child(DateFoodStore).child(FoodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Food food = dataSnapshot.getValue(Food.class);

                    FoodDetailsName.setText(food.getFood());
                    FoodDetailsPrice.setText(food.getPrice());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
