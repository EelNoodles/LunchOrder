package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lunchorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LeaveActivity extends AppCompatActivity {

    private EditText LeaveDate, LeaveRessult;
    private Button Leave;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        LeaveDate = (EditText) findViewById(R.id.leavedateedit);
        LeaveRessult = (EditText) findViewById(R.id.leaveresultedit);
        Leave = (Button) findViewById(R.id.leave);
        loadingBar = new ProgressDialog(this);

        LeaveRessult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{

                        "晚到。",
                        "補習。",
                        "有私事不方便透露，家人知情。",
                        "其他。"


                };

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LeaveActivity.this);
                builder.setTitle("請選擇欲前往選單");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i == 0){

                            LeaveRessult.setText("晚到。");

                        }

                        if(i == 1){

                            LeaveRessult.setText("補習。");

                        }

                        if(i == 2){

                            LeaveRessult.setText("有私事不方便透露，家人知情。");

                        }

                        if(i == 3){

                            Toast.makeText(LeaveActivity.this, "請填寫請假緣由。", Toast.LENGTH_SHORT).show();

                            final View item = LayoutInflater.from(LeaveActivity.this).inflate(R.layout.leaveresultdialogdesign, null);

                            new AlertDialog.Builder(LeaveActivity.this)
                                    .setTitle("請輸入請假原因")
                                    .setView(item)
                                    .setPositiveButton("填入", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            EditText editText = (EditText) item.findViewById(R.id.leaveresultdialogdesignedit);
                                            String Result = editText.getText().toString();

                                            LeaveRessult.setText(" ★  " + Result);

                                        }
                                    })

                                    .show();

                        }
                    }
                });

                builder.show();

            }
        });

        Leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String SLeaveDate = LeaveDate.getText().toString();
                final String SLeaveResult = LeaveRessult.getText().toString();
                final String UserClass = Prevalent.RightOnlineUser.getUserClass();

                if(TextUtils.isEmpty(SLeaveDate) || TextUtils.isEmpty(SLeaveResult)){

                    Toast.makeText(LeaveActivity.this, "請填寫請假日期與緣由。", Toast.LENGTH_SHORT).show();

                }else{

                    final DatabaseReference UserRef,LeaveRef;
                    UserRef = FirebaseDatabase.getInstance().getReference().child("LeaveList").child(UserClass);
                    LeaveRef = FirebaseDatabase.getInstance().getReference().child("Leave").child(UserClass).child(SLeaveDate);

                    LeaveRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if (!dataSnapshot.exists()){

                                moveGameRoom(UserRef, LeaveRef);

                                loadingBar.setTitle("正在製作點名資料...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();


                            }else {

                                loadingBar.setTitle("正在登入您的請假資訊...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                if (SLeaveResult.equals("晚到。")) {

                                    final DatabaseReference RootRef;
                                    RootRef = FirebaseDatabase.getInstance().getReference();

                                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            final HashMap<String, Object> LeaveMap = new HashMap<>();
                                            LeaveMap.put("Number", Prevalent.RightOnlineUser.getNumber());
                                            LeaveMap.put("Name", Prevalent.RightOnlineUser.getName());
                                            LeaveMap.put("LeaveDate", SLeaveDate);
                                            LeaveMap.put("LeaveResult", SLeaveResult);
                                            LeaveMap.put("Status", "✚  晚到。");

                                            RootRef.child("Leave").child(UserClass).child(SLeaveDate).child(Prevalent.RightOnlineUser.getNumber()).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(LeaveActivity.this, "晚到通知完成。", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();

                                                    Intent intent = new Intent(LeaveActivity.this, MainActivity.class);
                                                    startActivity(intent);

                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {

                                    final DatabaseReference RootRef;
                                    RootRef = FirebaseDatabase.getInstance().getReference();

                                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            final HashMap<String, Object> LeaveMap = new HashMap<>();
                                            LeaveMap.put("Number", Prevalent.RightOnlineUser.getNumber());
                                            LeaveMap.put("Name", Prevalent.RightOnlineUser.getName());
                                            LeaveMap.put("LeaveDate", SLeaveDate);
                                            LeaveMap.put("LeaveResult", SLeaveResult);
                                            LeaveMap.put("Status", "▲  請假。");

                                            RootRef.child("Leave").child(UserClass).child(SLeaveDate).child(Prevalent.RightOnlineUser.getNumber()).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(LeaveActivity.this, "請假完成。", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();

                                                    Intent intent = new Intent(LeaveActivity.this, MainActivity.class);
                                                    startActivity(intent);

                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

            }
        });

    }

    public void leavedatePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String dateTime = String.valueOf(year)+String.valueOf(month + 1)+String.valueOf(day);

                LeaveDate.setText(dateTime);

            }

        }, year, month, day).show();
    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {

                        if (firebaseError != null) {

                            Toast.makeText(LeaveActivity.this, "發生錯誤。", Toast.LENGTH_SHORT).show();

                        } else {

                            final String SLeaveDate = LeaveDate.getText().toString();
                            final String UserClass = Prevalent.RightOnlineUser.getUserClass();

                            Toast.makeText(LeaveActivity.this, "成功製作點名列表，請重新提交。", Toast.LENGTH_SHORT).show();

                            final HashMap<String, Object> LastUpdateTime = new HashMap<>();
                            LastUpdateTime.put("LastUpdateTime", "null");

                            final DatabaseReference LastUpdateTimeRef;
                            LastUpdateTimeRef = FirebaseDatabase.getInstance().getReference().child("Leave").child(UserClass).child("LastUpdateTime").child(SLeaveDate);

                            LastUpdateTimeRef.updateChildren(LastUpdateTime);

                            loadingBar.dismiss();

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
