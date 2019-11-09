package com.example.lunchorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import io.paperdb.Paper;

public class AdminSelectActivity extends AppCompatActivity {

    private Button AdminSelectDate, AdminSelectAddFood, RegisterButton, AdminStats, Announcement;
    private ImageView Logo, Logouticon, RollCallicon, SetHomeWorkIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select);

        AdminSelectAddFood = (Button) findViewById(R.id.AdminSelcetButton_Add_Food);
        AdminSelectDate = (Button) findViewById(R.id.AdminSelcetButton_Date);
        RegisterButton = (Button) findViewById(R.id.registerbutton);
        AdminStats = (Button) findViewById(R.id.adminstats);
        Announcement = (Button) findViewById(R.id.Announcement);
        Logo = (ImageView) findViewById(R.id.comlogo);
        Logouticon = (ImageView) findViewById(R.id.Logouticon);
        RollCallicon = (ImageView) findViewById(R.id.RollCallicon);
        SetHomeWorkIcon = (ImageView) findViewById(R.id.SetHomeWorkIcon);

        AdminSelectAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, AdminActivity.class);
                startActivity(intent);

            }
        });

        AdminSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, AdminDateMenuActivity.class);
                startActivity(intent);

            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        AdminStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, AdminStatsDateSelectActivity.class);
                startActivity(intent);

            }
        });

        Announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, AnnouncementSettingActivity.class);
                startActivity(intent);

            }
        });

        Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Logouticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, MainActivity.class);
                startActivity(intent);

                Paper.book().delete("Admin");

            }
        });

        RollCallicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, AdminRollCallDateSelect.class);
                startActivity(intent);

            }
        });

        SetHomeWorkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminSelectActivity.this, LogInHomeWork.class);
                startActivity(intent);

            }
        });

    }
}
