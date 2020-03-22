package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Announcement;
import com.example.lunchorder.Model.FoodStore;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import io.paperdb.Paper;

public class OrderDateSelectActivity extends AppCompatActivity {

    private EditText editDate;
    private Button SelectButton, LogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_date_select);

        editDate = (EditText) findViewById(R.id.DateSelectEditText);
        SelectButton = (Button) findViewById(R.id.UserSelectDateButton);
        LogoutButton = (Button) findViewById(R.id.UserSelectDateLogoutButton);


        SelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserDateSclect = editDate.getText().toString();

                if (TextUtils.isEmpty(UserDateSclect)){

                    Toast.makeText(OrderDateSelectActivity.this, "請選擇日期", Toast.LENGTH_SHORT).show();

                }else{

                    FoodStoreDataSave(UserDateSclect);

                }

            }
        });

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(OrderDateSelectActivity.this, "成功離開。", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(OrderDateSelectActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }

    public void datePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+String.valueOf(month + 1)+String.valueOf(day);
                editDate.setText(dateTime);
            }

        }, year, month, day).show();
    }

    private void FoodStoreDataSave(final String UserDateSclect){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FoodStore dateStore = dataSnapshot.child("DateStore").child(Prevalent.RightOnlineUser.getUserClass()).child(UserDateSclect).getValue(FoodStore.class);

                if (dataSnapshot.child("DateStore").child(Prevalent.RightOnlineUser.getUserClass()).child(UserDateSclect).exists()){

                    Prevalent.DateFoodStore = dateStore;

                    Intent intent = new Intent(OrderDateSelectActivity.this, SettingActivity.class);
                    startActivity(intent);

                }else{

                    Toast.makeText(OrderDateSelectActivity.this, "此日期菜單尚未建立。", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}


