package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Model.FoodStore;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DrinkDetailActivity extends AppCompatActivity {

    private Button AddToOrderList;
    private TextView FoodDetailsPrice, FoodDetailsName, DrinkDetailsSweet, DrinkDetailsIce;
    private ElegantNumberButton amountbutton;
    private String DrinkID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        DrinkID = getIntent().getStringExtra("DrinkID");

        AddToOrderList = (Button) findViewById(R.id.DrinkAddToOrderList);
        FoodDetailsName = (TextView) findViewById(R.id.DrinkDetailsName);
        FoodDetailsPrice = (TextView) findViewById(R.id.DrinkDetailsPrice);
        amountbutton = (ElegantNumberButton) findViewById(R.id.DrinkDetailsElegantNumberButton);
        DrinkDetailsSweet = (TextView) findViewById(R.id.DrinkDetailsSweet);
        DrinkDetailsIce = (TextView) findViewById(R.id.DrinkDetailsIce);

        GetFoodDetails(DrinkID);

        DrinkDetailsSweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{

                        "全糖。",
                        "少糖。",
                        "半糖。",
                        "微糖。",
                        "一分糖。",
                        "無糖。",
                        "不須選擇"


                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.this);
                builder.setTitle("選擇甜度");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i == 0){

                            DrinkDetailsSweet.setText("全糖");

                        }

                        if(i == 1){

                            DrinkDetailsSweet.setText("少糖");

                        }

                        if(i == 2){

                            DrinkDetailsSweet.setText("半糖");

                        }

                        if(i == 3){

                            DrinkDetailsSweet.setText("微糖");

                        }

                        if(i == 4){

                            DrinkDetailsSweet.setText("一分糖");

                        }

                        if(i == 5){

                            DrinkDetailsSweet.setText("無糖");

                        }

                        if(i == 6){

                            DrinkDetailsSweet.setText("不須選擇");

                        }

                    }
                });

                builder.show();

            }
            });

        DrinkDetailsIce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{

                        "正常。",
                        "少冰。",
                        "微冰。",
                        "去冰。"


                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DrinkDetailActivity.this);
                builder.setTitle("選擇冰塊量");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i == 0){

                            DrinkDetailsIce.setText("正常");

                        }

                        if(i == 1){

                            DrinkDetailsIce.setText("少冰");

                        }

                        if(i == 2){

                            DrinkDetailsIce.setText("微冰");

                        }

                        if(i == 3){

                            DrinkDetailsIce.setText("去冰");

                        }

                    }
                });

                builder.show();

            }
        });


        AddToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Sweet = DrinkDetailsSweet.getText().toString();
                String Ice = DrinkDetailsIce.getText().toString();

                if(amountbutton.getNumber().equals("0") || Sweet.equals("選擇甜度") || Ice.equals("選擇冰塊量")){

                    Toast.makeText(DrinkDetailActivity.this, "請將數量、甜度、冰塊量進行選擇。", Toast.LENGTH_SHORT).show();

                }else {

                    addtoorderlist();

                }
            }
        });

    }

    private void addtoorderlist() {

        final String UserNumber = Prevalent.RightOnlineUser.getNumber();
        final String Date = Prevalent.DateFoodStore.getDate();
        final String FoodName = FoodDetailsName.getText().toString();
        final String amount = amountbutton.getNumber();
        final String Sweet = DrinkDetailsSweet.getText().toString();
        final String Ice = DrinkDetailsIce.getText().toString();

        final DatabaseReference orderlistRef = FirebaseDatabase.getInstance().getReference().child("OrderList");

        orderlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("User View").child(UserNumber).child(Date).child(Ice + Sweet + FoodName).exists()) {

                    Food dateTotalAmount = dataSnapshot.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).getValue(Food.class);
                    Food dateAmount = dataSnapshot.child("User View").child(UserNumber).child(Date).child(Ice + Sweet + FoodName).getValue(Food.class);
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
                    orderlistMap.put("Sweet", Sweet);
                    orderlistMap.put("Ice", Ice);
                    orderlistMap.put("Types", "Drink");

                    adminorderlistMap.put("Food", FoodName);
                    adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                    adminorderlistMap.put("Sweet", Sweet);
                    adminorderlistMap.put("Ice", Ice);
                    adminorderlistMap.put("TotalAmount", stringtotalamount);

                    orderlistRef.child("User View").child(UserNumber).child(Date).child(Ice + Sweet + FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            orderlistRef.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(DrinkDetailActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(DrinkDetailActivity.this, OrderListActivity.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    });

                }else{

                    if(dataSnapshot.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).exists()){

                        Food dateTotalAmount = dataSnapshot.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).getValue(Food.class);
                        final String stringdateTotalAmount = dateTotalAmount.getTotalAmount();
                        int totalamount = ((Integer.valueOf(stringdateTotalAmount))) + ((Integer.valueOf(amount)));

                        String stringtotalamount = String.valueOf(totalamount);


                        final HashMap<String, Object> orderlistMap = new HashMap<>();
                        final HashMap<String, Object> adminorderlistMap = new HashMap<>();

                        orderlistMap.put("Food", FoodName);
                        orderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        orderlistMap.put("Amount", amount);
                        orderlistMap.put("Sweet", Sweet);
                        orderlistMap.put("Ice", Ice);
                        orderlistMap.put("Types", "Drink");

                        adminorderlistMap.put("Food", FoodName);
                        adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        adminorderlistMap.put("Sweet", Sweet);
                        adminorderlistMap.put("Ice", Ice);
                        adminorderlistMap.put("TotalAmount", stringtotalamount);

                        orderlistRef.child("User View").child(UserNumber).child(Date).child(Ice + Sweet + FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                orderlistRef.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(DrinkDetailActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(DrinkDetailActivity.this, OrderListActivity.class);
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
                        orderlistMap.put("Sweet", Sweet);
                        orderlistMap.put("Ice", Ice);
                        orderlistMap.put("Types", "Drink");

                        adminorderlistMap.put("Food", FoodName);
                        adminorderlistMap.put("Price", FoodDetailsPrice.getText().toString());
                        adminorderlistMap.put("Sweet", Sweet);
                        adminorderlistMap.put("Ice", Ice);
                        adminorderlistMap.put("TotalAmount", stringtotalamount);

                        orderlistRef.child("User View").child(UserNumber).child(Date).child(Ice + Sweet + FoodName).updateChildren(orderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                orderlistRef.child("Admin View").child(Date).child("Drink").child(Ice + Sweet + FoodName).updateChildren(adminorderlistMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(DrinkDetailActivity.this, "成功訂購餐點", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(DrinkDetailActivity.this, OrderListActivity.class);
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

    private void GetFoodDetails(String DrinkID){

        final String DateDrinkStore = Prevalent.DateFoodStore.getDrinkStore();

        DatabaseReference FoodRef = FirebaseDatabase.getInstance().getReference().child("Food");

        FoodRef.child("Drink").child(DateDrinkStore).child(DrinkID).addValueEventListener(new ValueEventListener() {
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
