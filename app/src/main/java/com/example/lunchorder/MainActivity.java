package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Announcement;
import com.example.lunchorder.Model.Users;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button LoginButton;
    private TextView Version;
    private EditText InputName, InputNumber;
    private ProgressDialog loadingBar;
    private String User = "Users";
    private CheckBox CheckBoxRemember;
    private ImageView comlogo;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginButton = (Button) findViewById(R.id.loginbutton);
        InputName = (EditText) findViewById(R.id.loginname);
        InputNumber = (EditText) findViewById(R.id.loginnumber);
        Version = (TextView) findViewById(R.id.Version);
        loadingBar = new ProgressDialog(this);
        comlogo = (ImageView) findViewById(R.id.comlogo);

        CheckBoxRemember = (CheckBox) findViewById(R.id.loginCheckBox);
        Paper.init(this);

        Announcement();

        final String UserName = Paper.book().read(Prevalent.UserName);
        String UserNumber = Paper.book().read(Prevalent.UserNumber);


        if (UserName !=null && UserNumber !=null){

            if (!TextUtils.isEmpty(UserName) && !TextUtils.isEmpty(UserNumber)){


                        Toast.makeText(MainActivity.this, "登入記住帳號。", Toast.LENGTH_SHORT).show();

                        InputName.setText(UserName);
                        InputNumber.setText(UserNumber);

                    }


            }

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginUser();
            }
        });

        comlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Announcement();

            }
        });

        Version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Paper.book().read("Admin") != null ){

                    Intent intent = new Intent(MainActivity.this, AdminSelectActivity.class);
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "管理員成功登入。", Toast.LENGTH_SHORT).show();

                }else if(Paper.book().read("RollCall") != null){

                    Intent intent = new Intent(MainActivity.this, AdminRollCallDateSelect.class);
                    startActivity(intent);

                }else{

                    final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.adminlogindialogdesign, null);

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("管理員登入")
                            .setView(item)
                            .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText editText = (EditText) item.findViewById(R.id.adminloginpassword);
                                    String name = editText.getText().toString();
                                    if(TextUtils.isEmpty(name)){

                                        Toast.makeText(MainActivity.this, "請輸入認證碼。", Toast.LENGTH_SHORT).show();

                                    } else {

                                        if(name.equals("06180618")){

                                            Intent intent = new Intent(MainActivity.this, AdminSelectActivity.class);
                                            startActivity(intent);

                                            Toast.makeText(MainActivity.this, "管理員成功登入。", Toast.LENGTH_SHORT).show();

                                            Paper.book().write("Admin", "06180618");


                                        }else {

                                            if(name.equals("31022") || name.equals("31042")){

                                                Intent intent = new Intent(MainActivity.this, AdminRollCallDateSelect.class);
                                                startActivity(intent);

                                                Paper.book().write("RollCall", "3102232");

                                            }else{

                                                if(name.equals("310")) {

                                                    Intent intent = new Intent(MainActivity.this, LogInHomeWork.class);
                                                    startActivity(intent);

                                                }else {

                                                    Toast.makeText(MainActivity.this, "認證碼錯誤。", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }
                                    }
                                }
                            })

                            .show();

                }

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = "default_notification_channel_id";
            String channelName = "default_notification_channel_name";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

    }

    private void LoginUser(){

        String Name = InputName.getText().toString();
        String Number = InputNumber.getText().toString();

        if (TextUtils.isEmpty(Name)){

            Toast.makeText(this, "請輸入姓名。", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(Number)){

            Toast.makeText(this, "請輸入座號。", Toast.LENGTH_SHORT).show();

        }else{
            loadingBar.setTitle("正在登入您的帳號...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowLogin(Name, Number);
        }

    }

    private void AllowLogin(final String Name, final String Number){


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users userdata = dataSnapshot.child(User).child(Number).getValue(Users.class);

                if (userdata.getNumber() !=null && userdata.getName() !=null){

                    if (userdata.getNumber().equals(Number) && userdata.getName().equals(Name)){

                            if (CheckBoxRemember.isChecked()) {

                                Paper.book().write(Prevalent.UserName, Name);
                                Paper.book().write(Prevalent.UserNumber, Number);

                            }

                            Toast.makeText(MainActivity.this, "成功登入。", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Prevalent.RightOnlineUser = userdata;

                            choise();

                        }else{

                        Toast.makeText(MainActivity.this, "您的帳號或是密碼可能有誤，請重新輸入。", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void Announcement(){

        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.announcementdesign, null);
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                TextView AnTitle = (TextView) item.findViewById(R.id.announcementtitle);
                TextView AnMessage = (TextView) item.findViewById(R.id.announcementmessage);

                final Announcement announcement = dataSnapshot.child("Announcement").getValue(Announcement.class);

                AnTitle.setText(announcement.getTitle());
                AnMessage.setText(announcement.getMessage());

                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setView(item)
                        .setPositiveButton("關閉公告", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("前往更新網站", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(announcement.getLink() == null){

                                    Toast.makeText(MainActivity.this, "目前無新的更新。", Toast.LENGTH_SHORT).show();

                                }else{

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(announcement.getLink()));
                                    startActivity(intent);

                                }

                            }
                        })

                        .show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void choise(){

        CharSequence options[] = new CharSequence[]{

                "前往訂餐頁面。",
                "前往自習請假頁面。",
                "前往功課列表。"


        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("請選擇欲前往選單");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(i == 0){

                    Intent intent = new Intent(MainActivity.this, OrderDateSelectActivity.class);
                    startActivity(intent);

                }

                if(i == 1){

                    Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
                    startActivity(intent);

                }
                if(i == 2){

                    Intent intent = new Intent(MainActivity.this, HomeWork.class);
                    startActivity(intent);

                }
            }
        });

        builder.show();

    }

}