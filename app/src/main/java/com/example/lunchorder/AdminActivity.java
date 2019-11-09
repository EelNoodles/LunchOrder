package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lunchorder.Model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private EditText InputFoodName, InputFoodPrice, InputStoreName;
    private Button InputFoodButton;
    private String CategoryName, FoodName, FoodPrice, StoreName;
    private ImageView FoodImage;
    private DatabaseReference FoodRef, StoreRef;
    private ProgressDialog loadingBar;
    private RadioButton InputFoodCategory, InputDrinkCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        InputFoodButton = (Button) findViewById(R.id.InputButton);
        InputFoodName = (EditText) findViewById(R.id.InputFoodName);
        InputFoodPrice = (EditText) findViewById(R.id.InputFoodPrice);
        loadingBar = new ProgressDialog(this);
        FoodRef = FirebaseDatabase.getInstance().getReference().child("Food");
        StoreRef = FirebaseDatabase.getInstance().getReference().child("Store");
        InputStoreName = (EditText) findViewById(R.id.InputStoreName);
        InputFoodCategory = (RadioButton) findViewById(R.id.InputFoodCategory);
        InputDrinkCategory = (RadioButton) findViewById(R.id.InputDrinkCategory);

        InputFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    loadingBar.setTitle("正在登入食品....");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidData();
                    SaveFood();

                }
        });

        InputDrinkCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                InputFoodCategory.setChecked(false);

            }
        });

        InputFoodCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                InputDrinkCategory.setChecked(false);

            }
        });
    }

    private void ValidData(){

        FoodName = InputFoodName.getText().toString();
        FoodPrice = InputFoodPrice.getText().toString();
        StoreName = InputStoreName.getText().toString();

        if (TextUtils.isEmpty(StoreName)){

            Toast.makeText(this,"請輸入店家名稱", Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(FoodName)){

            Toast.makeText(this,"請輸入食物名稱", Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(FoodPrice)){

            Toast.makeText(this,"請輸入食物價格", Toast.LENGTH_LONG).show();

        }

    }

    private void SaveFood(){

        HashMap<String, Object> FoodMap = new HashMap<>();
        FoodMap.put("Price", FoodPrice.toString());
        FoodMap.put("Food", FoodName.toString());

        HashMap<String, Object> FoodStoreMap = new HashMap<>();
        FoodStoreMap.put("StoreName", StoreName);

        if (InputFoodCategory.isChecked()){

            StoreRef.child("FoodStore").child(StoreName).updateChildren(FoodStoreMap);
            FoodRef.child("Food").child(StoreName).child(FoodName).updateChildren(FoodMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        loadingBar.dismiss();
                        Toast.makeText(AdminActivity.this,"食品已經成功登入", Toast.LENGTH_LONG).show();

                        InputFoodName.getText().clear();
                        InputFoodPrice.getText().clear();


                    }else{

                        Toast.makeText(AdminActivity.this,"發生錯誤", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                    }

                }
            });

        }else if(InputDrinkCategory.isChecked()){

            StoreRef.child("DrinkStore").child(StoreName).updateChildren(FoodStoreMap);
            FoodRef.child("Drink").child(StoreName).child(FoodName).updateChildren(FoodMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        loadingBar.dismiss();
                        Toast.makeText(AdminActivity.this,"食品已經成功登入", Toast.LENGTH_LONG).show();

                        InputFoodName.getText().clear();
                        InputFoodPrice.getText().clear();

                    }else{

                        Toast.makeText(AdminActivity.this,"發生錯誤", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                    }

                }
            });

        }

    }
}
