package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lunchorder.Model.AdminStats;
import com.example.lunchorder.Model.FoodStore;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import io.paperdb.Paper;

public class AdminStatsDateSelectActivity extends AppCompatActivity {

    private EditText AdminEditDate;
    private Button AdminSelectButton, AdminLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats_date_select);

        AdminEditDate = (EditText) findViewById(R.id.AdminDateSelectEditText);
        AdminSelectButton = (Button) findViewById(R.id.AdminUserSelectDateButton);
        AdminLogoutButton = (Button) findViewById(R.id.AdminUserSelectDateLogoutButton);

        AdminSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserDateSclect = AdminEditDate.getText().toString();

                if (TextUtils.isEmpty(UserDateSclect)){

                    Toast.makeText(AdminStatsDateSelectActivity.this, "請選擇日期", Toast.LENGTH_SHORT).show();

                }else{

                    FoodStoreDataSave(UserDateSclect);

                }

            }
        });

        AdminLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Paper.book().destroy();

                Toast.makeText(AdminStatsDateSelectActivity.this, "成功登出。", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminStatsDateSelectActivity.this, MainActivity.class);
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
                AdminEditDate.setText(dateTime);
            }

        }, year, month, day).show();
    }

    private void FoodStoreDataSave(final String UserDateSclect){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FoodStore dateStore = dataSnapshot.child("DateStore").child(UserDateSclect).getValue(FoodStore.class);

                    Prevalent.DateFoodStore = dateStore;

                    Intent intent = new Intent(AdminStatsDateSelectActivity.this, AdminStatsLobby.class);
                    startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}



