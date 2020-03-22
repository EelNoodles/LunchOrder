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
import com.example.lunchorder.Model.Leave;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private TextView ShowName, MailText, LoginText, AdminText, OrderButtom, RollCallButtom;
    private ProgressDialog loadingBar;
    private String User = "Users";
    private CheckBox CheckBoxRemember;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingBar = new ProgressDialog(this);
        ShowName = (TextView) findViewById(R.id.ShowName);
        MailText = (TextView) findViewById(R.id.MailText);
        AdminText = (TextView) findViewById(R.id.AdminText);
        LoginText = (TextView) findViewById(R.id.LoginText);
        RollCallButtom = (TextView) findViewById(R.id.RollCallButtom);
        OrderButtom = (TextView) findViewById(R.id.OrderButtom);

        Paper.init(this);

        final String Name = Paper.book().read(Prevalent.UserName);
        String Number = Paper.book().read(Prevalent.UserNumber);
        String UserClass = Paper.book().read(Prevalent.UserClass);

        if (Name !=null && Number !=null && UserClass !=null){

            if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Number) && !TextUtils.isEmpty(UserClass)){

                Toast.makeText(MainActivity.this, "登入記住帳號。", Toast.LENGTH_SHORT).show();

                RememberLogin(Name, Number, UserClass);

            }

        }

        MailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Announcement();

            }
        });

        OrderButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, OrderDateSelectActivity.class);
                startActivity(intent);

            }
        });

        RollCallButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LeaveActivity.class);
                startActivity(intent);

            }
        });

        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Paper.book().read(Prevalent.UserName) != null){

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提醒")
                            .setMessage("你目前正處在登入狀態中").setPositiveButton("登出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Paper.book().destroy();
                            Toast.makeText(MainActivity.this, "成功登出。", Toast.LENGTH_SHORT).show();

                            ShowName.setText("你好！");

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

                }else{

                    LoginAlertDialog();

                }
            }
        });

        AdminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Paper.book().read("AdminPassword") != null ){

                    Intent intent = new Intent(MainActivity.this, AdminSelectActivity.class);
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "管理員成功登入。", Toast.LENGTH_SHORT).show();


                }else{

                    final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.adminlogindialogdesign, null);

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("管理員登入")
                            .setView(item)
                            .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText AdminPassword = (EditText) item.findViewById(R.id.adminloginpassword);
                                    EditText AdminClass = (EditText) item.findViewById(R.id.adminloginclass);
                                    String adminpassword = AdminPassword.getText().toString();
                                    String adminclass = AdminClass.getText().toString();
                                    if(TextUtils.isEmpty(adminpassword)){

                                        Toast.makeText(MainActivity.this, "請輸入認證碼。", Toast.LENGTH_SHORT).show();

                                    } else {

                                        if(adminpassword.equals("PTHS")){

                                            Intent intent = new Intent(MainActivity.this, AdminSelectActivity.class);
                                            startActivity(intent);

                                            Toast.makeText(MainActivity.this, "管理員成功登入。", Toast.LENGTH_SHORT).show();

                                            Paper.book().write("AdminPassword", "PTHS");
                                            Paper.book().write("AdminClass", adminclass);


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

    private void LoginAlertDialog(){

        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.userlogindesign, null);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("學生登入")
                .setView(item).setPositiveButton("登入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                CheckBoxRemember = (CheckBox) item.findViewById(R.id.loginCheckBox);

                EditText InputName = (EditText) item.findViewById(R.id.loginname);
                EditText InputNumber = (EditText) item.findViewById(R.id.loginnumber);
                EditText InputClass = (EditText) item.findViewById(R.id.loginclass);

                String Name = InputName.getText().toString();
                String Number = InputNumber.getText().toString();
                String UserClass = InputClass.getText().toString();

                LoginUser(Name, Number, UserClass);


            }
        }).show();

    }

    private void LoginUser(String Name, String Number, String UserClass){


        if (TextUtils.isEmpty(UserClass)){

            Toast.makeText(this, "請輸入班級。", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(Number)){

            Toast.makeText(this, "請輸入座號。", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(Name)){

            Toast.makeText(this, "請輸入姓名。", Toast.LENGTH_SHORT).show();

        }else{
            loadingBar.setTitle("正在登入您的帳號...  ☕️");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowLogin(Name, Number, UserClass);
        }

    }

    private void AllowLogin(final String Name, final String Number, final String UserClass){


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users userdata = dataSnapshot.child(User).child(UserClass).child(Number).getValue(Users.class);

                if (userdata.getNumber() !=null && userdata.getName() !=null && userdata.getUserClass() !=null){

                    if (userdata.getNumber().equals(Number) && userdata.getName().equals(Name) && userdata.getUserClass().equals(UserClass)) {

                            if (CheckBoxRemember.isChecked()) {

                                Paper.book().write(Prevalent.UserName, Name);
                                Paper.book().write(Prevalent.UserNumber, Number);
                                Paper.book().write(Prevalent.UserClass, UserClass);

                            }

                            Toast.makeText(MainActivity.this, "成功登入。", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Prevalent.RightOnlineUser = userdata;

                            ShowName.setText("你好， " + Prevalent.RightOnlineUser.getName() + "  \uD83D\uDE0B " + "（ " + Prevalent.RightOnlineUser.getUserClass() + " / " + Prevalent.RightOnlineUser.getNumber() + " ）" );

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

    private void RememberLogin(final String Name, final String Number, final String UserClass){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users userdata = dataSnapshot.child(User).child(UserClass).child(Number).getValue(Users.class);

                        Toast.makeText(MainActivity.this, "成功登入。", Toast.LENGTH_SHORT).show();

                        Prevalent.RightOnlineUser = userdata;

                        ShowName.setText("你好， " + Prevalent.RightOnlineUser.getName() + "  \uD83D\uDE0B " + "（ " + Prevalent.RightOnlineUser.getUserClass() + " / " + Prevalent.RightOnlineUser.getNumber() + " ）" );

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
}