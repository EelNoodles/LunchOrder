package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminRollCallDateSelect extends AppCompatActivity {

    private EditText adminrollcallDateSelectEditText;
    private Button adminrollcallUserSelectDateButton, adminrollcallUserSelectDateLogoutButton;

    private String AdminClass = Paper.book().read("AdminClass").toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_roll_call_date_select);

        adminrollcallDateSelectEditText = (EditText) findViewById(R.id.adminrollcallDateSelectEditText);
        adminrollcallUserSelectDateLogoutButton = (Button) findViewById(R.id.adminrollcallUserSelectDateLogoutButton);
        adminrollcallUserSelectDateButton = (Button) findViewById(R.id.adminrollcallUserSelectDateButton);

        adminrollcallUserSelectDateLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminRollCallDateSelect.this, MainActivity.class);
                startActivity(intent);

            }
        });

        adminrollcallUserSelectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Date = adminrollcallDateSelectEditText.getText().toString();

                final DatabaseReference UserRef,LeaveRef;
                UserRef = FirebaseDatabase.getInstance().getReference().child("LeaveList").child(AdminClass);
                LeaveRef = FirebaseDatabase.getInstance().getReference().child("Leave").child(AdminClass).child(Date);

                LeaveRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Date = adminrollcallDateSelectEditText.getText().toString();

                        if (!dataSnapshot.exists()){

                            moveGameRoom(UserRef, LeaveRef);

                        }else{

                            Intent intent = new Intent(AdminRollCallDateSelect.this, AdminRollCallActivity.class);
                            intent.putExtra("Date", Date);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    public void adminrollcalldatePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+String.valueOf(month + 1)+String.valueOf(day);

                adminrollcallDateSelectEditText.setText(dateTime);

            }

        }, year, month, day).show();
    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {

                        if (firebaseError != null) {

                            Toast.makeText(AdminRollCallDateSelect.this, "發生錯誤。", Toast.LENGTH_SHORT).show();

                        } else {

                            String Date = adminrollcallDateSelectEditText.getText().toString();

                            Toast.makeText(AdminRollCallDateSelect.this, "成功製作點名列表。", Toast.LENGTH_SHORT).show();

                            final HashMap<String, Object> LastUpdateTime = new HashMap<>();
                            LastUpdateTime.put("LastUpdateTime", "null");

                            final DatabaseReference LastUpdateTimeRef;
                            LastUpdateTimeRef = FirebaseDatabase.getInstance().getReference().child("Leave").child(AdminClass).child("LastUpdateTime").child(Date);

                            LastUpdateTimeRef.updateChildren(LastUpdateTime);

                            Intent intent = new Intent(AdminRollCallDateSelect.this, AdminRollCallActivity.class);
                            intent.putExtra("Date", Date);
                            startActivity(intent);


                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
