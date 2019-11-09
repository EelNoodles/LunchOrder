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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.HomeWork;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class CancelHomeWork extends AppCompatActivity {

    private EditText CancelTitle;
    private Button CancelButton;
    private ImageView comlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_home_work);

        CancelTitle = (EditText) findViewById(R.id.CancelTitle);
        CancelButton = (Button) findViewById(R.id.CancelButton);
        comlogo = (ImageView) findViewById(R.id.comlogo);

        comlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CancelHomeWork.this, LogInHomeWork.class);
                startActivity(intent);

            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference HWRef;
                HWRef = FirebaseDatabase.getInstance().getReference().child("HomeWork");

                HWRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(int a = 1; a < 42 ; a++){

                            String Number = String.valueOf(a);

                            final String CT = CancelTitle.getText().toString();

                            HWRef.child(Number).child("UnFinish").child(CT).removeValue();
                            HWRef.child(Number).child("Finish").child(CT).removeValue();
                            HWRef.child("HomeWorkList").child(CT).removeValue();

                        }

                        String CT = CancelTitle.getText().toString();

                        Toast.makeText(CancelHomeWork.this, "成功刪除。" + CT, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CancelHomeWork.this, LogInHomeWork.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
