package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class LogInHomeWork extends AppCompatActivity {

    private EditText HomeWorkDeadLineEdit, HomeWorkTitleEdit, HomeWorkRemarksEdit;
    private TextView HomeWorkSubject;
    private Button HomeWorkButton, HomeWorkCancelButton;
    private ImageView comlogo;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_home_work);

        HomeWorkDeadLineEdit = (EditText) findViewById(R.id.HomeWorkDeadLineEdit);
        HomeWorkSubject = (TextView) findViewById(R.id.HomeWorkSubject);
        HomeWorkButton = (Button) findViewById(R.id.HomeWorkButton);
        HomeWorkTitleEdit = (EditText) findViewById(R.id.HomeWorkTitleEdit);
        HomeWorkRemarksEdit = (EditText) findViewById(R.id.HomeWorkRemarksEdit);
        HomeWorkCancelButton = (Button) findViewById(R.id.HomeWorkCancelButton);
        loadingBar = new ProgressDialog(this);
        comlogo = (ImageView) findViewById(R.id.comlogo);

        comlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LogInHomeWork.this, MainActivity.class);
                startActivity(intent);

            }
        });

        HomeWorkSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{

                        "國文",
                        "英文",
                        "數學",
                        "化學",
                        "物理",
                        "生物",
                        "美術",
                        "體育",
                        "生活科技"


                };

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LogInHomeWork.this);
                builder.setTitle("請選擇科目");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i == 0){

                            HomeWorkSubject.setText("【國文】");

                        }

                        if(i == 1){

                            HomeWorkSubject.setText("【英文】");

                        }

                        if(i == 2){

                            HomeWorkSubject.setText("【數學】");

                        }

                        if(i == 3){

                            HomeWorkSubject.setText("【化學】");

                        }

                        if(i == 4){

                            HomeWorkSubject.setText("【物理】");

                        }

                        if(i == 5){

                            HomeWorkSubject.setText("【生物】");

                        }

                        if(i == 6){

                            HomeWorkSubject.setText("【美術】");

                        }

                        if(i == 7){

                            HomeWorkSubject.setText("【體育】");

                        }

                        if(i == 8){

                            HomeWorkSubject.setText("【生活科技】");

                        }

                    }
                });

                builder.show();

            }
        });

        HomeWorkCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LogInHomeWork.this, CancelHomeWork.class);
                startActivity(intent);

            }
        });

        HomeWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String HWTE = HomeWorkTitleEdit.getText().toString();
                String HWDLE = HomeWorkDeadLineEdit.getText().toString();
                String HWRE = HomeWorkRemarksEdit.getText().toString();
                String HWS = HomeWorkSubject.getText().toString();

                if (TextUtils.isEmpty(HWTE) || TextUtils.isEmpty(HWDLE) || HWS.equals("【請選擇科目】")) {

                    Toast.makeText(LogInHomeWork.this, "請填完標題與日期並選擇科目。", Toast.LENGTH_SHORT).show();

                } else {

                    final DatabaseReference HWRef;
                    HWRef = FirebaseDatabase.getInstance().getReference().child("HomeWork");

                    HWRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String HWTE = HomeWorkTitleEdit.getText().toString();
                            String HWDLE = HomeWorkDeadLineEdit.getText().toString();
                            String HWRE = HomeWorkRemarksEdit.getText().toString();
                            String HWS = HomeWorkSubject.getText().toString();

                            for(int a = 1; a < 42 ; a++){

                                String Number = String.valueOf(a);

                                final HashMap<String, Object> HomeWorkMap = new HashMap<>();
                                HomeWorkMap.put("Title", HWTE);
                                HomeWorkMap.put("DeadLine", HWDLE);
                                HomeWorkMap.put("Remarks", HWRE);
                                HomeWorkMap.put("Subject", HWS);

                                HWRef.child(Number).child("UnFinish").child(HWTE).updateChildren(HomeWorkMap);
                                HWRef.child("HomeWorkList").child(HWTE).updateChildren(HomeWorkMap);

                            }

                            Toast.makeText(LogInHomeWork.this, "成功登入" + HWS + "作業。", Toast.LENGTH_SHORT).show();
                            HomeWorkTitleEdit.getText().clear();
                            HomeWorkDeadLineEdit.getText().clear();
                            HomeWorkRemarksEdit.getText().clear();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void DatePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year) + " / " + String.valueOf(month + 1) + " / " + String.valueOf(day);
                HomeWorkDeadLineEdit.setText(dateTime);
            }

        }, year, month, day).show();
    }

}
