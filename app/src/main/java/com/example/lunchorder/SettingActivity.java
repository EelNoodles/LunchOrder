package com.example.lunchorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.lunchorder.Interface.ItemClickListner;
import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Model.FoodStore;
import com.example.lunchorder.Model.OrderList;
import com.example.lunchorder.Model.Users;
import com.example.lunchorder.Prevalent.Prevalent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference FoodRef;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    private EditText editDate;

    final String DateFoodStore = Prevalent.DateFoodStore.getFoodStore();
    final String DateDrinkStore = Prevalent.DateFoodStore.getDrinkStore();
    final String Date = Prevalent.DateFoodStore.getDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FoodRef = FirebaseDatabase.getInstance().getReference().child("Food").child("Food").child(DateFoodStore);
        recyclerview = findViewById(R.id.recycle_menu);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        editDate = (EditText) findViewById(R.id.DateSelectEditText);

        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("午餐點餐選單" + " ( " + Prevalent.DateFoodStore.getDate() + " )");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingActivity.this, SettingDrinkActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        TextView UserName = headView.findViewById(R.id.SettingUserName);

        UserName.setText(Prevalent.RightOnlineUser.getNumber() + "   " + Prevalent.RightOnlineUser.getName());

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

    protected void onStart(){

        super.onStart();

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(FoodRef, Food.class).build();

        final FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull final Food model) {

                holder.DisplayFoodPrice.setText(model.getPrice().toString() + " 元");
                holder.DisplayFoodName.setText(model.getFood());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(SettingActivity.this, FoodDetailsActivity.class);
                        intent.putExtra("FoodID", model.getFood());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lobby, parent , false);

                FoodViewHolder holder = new FoodViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Paper.book().destroy();

            Toast.makeText(SettingActivity.this, "成功登出。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }

        if (id == R.id.menudatials) {

            new AlertDialog.Builder(SettingActivity.this).setTitle("本日菜單").setIcon(R.drawable.menu).setMessage("\n日期：" + Date + "\n\n午餐：" + DateFoodStore + "\n\n飲料：" + DateDrinkStore)
                    .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    }).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            Toast.makeText(SettingActivity.this, "已經回到登入畫面。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        if (id == R.id.nav_dateselect) {

            Toast.makeText(SettingActivity.this, "已經回到日期選擇畫面。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SettingActivity.this, OrderDateSelectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        if (id == R.id.nav_orderlist) {

            Toast.makeText(SettingActivity.this, "前往訂單列表。", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SettingActivity.this, OrderListActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
