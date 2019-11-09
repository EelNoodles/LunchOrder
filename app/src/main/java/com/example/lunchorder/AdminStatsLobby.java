package com.example.lunchorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Model.Users;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.AdminStatsViewHolder;
import com.example.lunchorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminStatsLobby extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference AdminStatsRef;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;

    private int admintotalprice = 0;
    private int admintotaluser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats_lobby);

        AdminStatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.DateFoodStore.getDate());

        recyclerview = findViewById(R.id.recycle_adminstatislobby);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("管理員統計");
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_stats_lobby, menu);
        return true;
    }

    protected void onStart(){

        super.onStart();

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(AdminStatsRef, Users.class).build();

        final FirebaseRecyclerAdapter<Users, AdminStatsViewHolder> adapter = new FirebaseRecyclerAdapter<Users, AdminStatsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull AdminStatsViewHolder holder, int position, @NonNull final Users model) {

                holder.adminstatslobbymame.setText(model.getName());
                holder.adminstatslobbynumber.setText(model.getNumber());
                holder.adminstatslobbyprice.setText(model.getTotalPrice());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(AdminStatsLobby.this, AdminCheckUserOrderList.class);
                        intent.putExtra("AdminCheckUserName", model.getName());
                        intent.putExtra("AdminCheckUserNumber", model.getNumber());
                        startActivity(intent);

                    }
                });

                int count = getItemCount();
                admintotalprice = 0;
                admintotaluser = count;

                for (int a = 0; a < count ; a++){

                    int OneFoodPrice = ((Integer.valueOf(getItem(a).getTotalPrice())));

                    admintotalprice = admintotalprice + OneFoodPrice ;

                }

                toolbar.setTitle("管理員統計 ( " + admintotalprice + " / " + admintotaluser + " )");

            }

            @NonNull
            @Override
            public AdminStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminstatslobbydesign, parent , false);

                AdminStatsViewHolder holder = new AdminStatsViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.adminaction_settings) {

            Toast.makeText(AdminStatsLobby.this, "成功登出。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminStatsLobby.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_adminlogout) {

            Toast.makeText(AdminStatsLobby.this, "已經回到登入畫面。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminStatsLobby.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        if (id == R.id.nav_admindateselect) {

            Toast.makeText(AdminStatsLobby.this, "已經回到日期選擇畫面。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminStatsLobby.this, AdminStatsDateSelectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        if (id == R.id.nav_adminorderfoodlist) {

            Toast.makeText(AdminStatsLobby.this, "前往午餐總表。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminStatsLobby.this, AdminStatsFoodOrderListActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_drinkorderdrinklist) {

            Toast.makeText(AdminStatsLobby.this, "前往飲料總表。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminStatsLobby.this, AdminStatsDrinkOrderListActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
