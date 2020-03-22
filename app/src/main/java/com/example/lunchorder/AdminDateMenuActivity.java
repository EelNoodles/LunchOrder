package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lunchorder.Model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminDateMenuActivity extends AppCompatActivity {

    private EditText editDate, AdminFoodStoreSelect, AdminDrinkStoreSelect;
    private Button AdminSendMenu;
    private ProgressDialog loadingBar;

    private String AdminClass = Paper.book().read("AdminClass").toString();

    public AdminDateMenuActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_date_menu);

        editDate = (EditText) findViewById(R.id.AdminDateSelectEditText);
        AdminSendMenu = (Button) findViewById(R.id.AdminSendMenu);
        AdminFoodStoreSelect = (EditText) findViewById(R.id.AdminFoodStoreSelect);
        AdminDrinkStoreSelect = (EditText) findViewById(R.id.AdminDrinkStoreSelect);
        loadingBar = new ProgressDialog(this);


        AdminSendMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setTitle("正在創立日期菜單...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();


                final String ED = editDate.getText().toString();
                final String ADSS = AdminDrinkStoreSelect.getText().toString();
                final String AFSS = AdminFoodStoreSelect.getText().toString();

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("Date", ED);
                        userdataMap.put("DrinkStore", ADSS);
                        userdataMap.put("FoodStore", AFSS);

                        RootRef.child("DateStore").child(AdminClass).child(ED).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(AdminDateMenuActivity.this, "成功創立。", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(AdminDateMenuActivity.this, AdminSelectActivity.class);
                                startActivity(intent);

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

    }

    public void admindatePicker(View v){
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

    public void adminfoodselectactivity(View v){

        Intent intent = new Intent(AdminDateMenuActivity.this, SelectFoodStoreActivity.class);
        startActivity(intent);


    }

    public void admindrinkselectactivity(View v){

        Intent intent = new Intent(AdminDateMenuActivity.this, SelectDrinkStoreActivity.class);
        startActivity(intent);

    }

}
