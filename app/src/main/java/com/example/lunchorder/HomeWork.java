package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Leave;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.HomeWorkViewHolder;
import com.example.lunchorder.ViewHolder.RollCallViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeWork extends AppCompatActivity {

    private DatabaseReference HWRef;
    private TextView HomeWorkListAccount;
    private ImageView HomeWorkListBackMainActivity, HomeWorkListToFinish;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);

        HWRef = FirebaseDatabase.getInstance().getReference().child("HomeWork").child(Prevalent.RightOnlineUser.getNumber()).child("UnFinish");

        HomeWorkListAccount = (TextView) findViewById(R.id.HomeWorkListAccount);
        HomeWorkListToFinish = (ImageView) findViewById(R.id.HomeWorkListToFinish);
        HomeWorkListBackMainActivity = (ImageView) findViewById(R.id.HomeWorkListBackMainActivity);

        recyclerview = findViewById(R.id.HomeWorkListRecycleView);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        HomeWorkListAccount.setText(Prevalent.RightOnlineUser.getName() + " / " + Prevalent.RightOnlineUser.getNumber());

        HomeWorkListBackMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(HomeWork.this, "回到主畫面。", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomeWork.this, MainActivity.class);
                startActivity(intent);

            }
        });

        HomeWorkListToFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(HomeWork.this, "前往完成功課清單。", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomeWork.this, HomeWorkFin.class);
                startActivity(intent);

            }
        });

    }

    protected void onStart(){

        super.onStart();

        final FirebaseRecyclerOptions<com.example.lunchorder.Model.HomeWork> options = new FirebaseRecyclerOptions.Builder<com.example.lunchorder.Model.HomeWork>().setQuery(HWRef, com.example.lunchorder.Model.HomeWork.class).build();

        final FirebaseRecyclerAdapter<com.example.lunchorder.Model.HomeWork, HomeWorkViewHolder> adapter = new FirebaseRecyclerAdapter<com.example.lunchorder.Model.HomeWork, HomeWorkViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final HomeWorkViewHolder holder, int position, @NonNull final com.example.lunchorder.Model.HomeWork model) {

                holder.HomeWorkRecycleTitle.setText(model.getTitle());
                holder.HomeWorkRecycleSubject.setText(model.getSubject());
                holder.HomeWorkRecycleDeadLine.setText(model.getDeadLine());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[] = new CharSequence[]{

                                "顯示作業備註。",
                                "設定作業為完成。"

                        };

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(HomeWork.this);
                        builder.setTitle("請選擇動作");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final DatabaseReference RootRef;
                                RootRef = FirebaseDatabase.getInstance().getReference();

                                if (i == 0){

                                    final View item = LayoutInflater.from(HomeWork.this).inflate(R.layout.homeworkremarksalertdialogdesign, null);

                                    TextView HWRemarks = (TextView) item.findViewById(R.id.homeworkremarksalertdialogdesign);

                                    HWRemarks.setText(model.getRemarks());

                                    new AlertDialog.Builder(HomeWork.this)
                                            .setCancelable(false)
                                            .setView(item)
                                            .setTitle("作業備註")
                                            .setPositiveButton("關閉顯示", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.cancel();

                                                }
                                            })

                                            .show();

                                }

                                if (i == 1) {

                                    final HashMap<String, Object> HomeWorkMap = new HashMap<>();
                                    HomeWorkMap.put("Title", model.getTitle());
                                    HomeWorkMap.put("DeadLine", model.getDeadLine());
                                    HomeWorkMap.put("Remarks", model.getRemarks());
                                    HomeWorkMap.put("Subject", model.getSubject());


                                    RootRef.child("HomeWork").child(Prevalent.RightOnlineUser.getNumber()).child("Finish").child(model.getTitle()).updateChildren(HomeWorkMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            RootRef.child("HomeWork").child(Prevalent.RightOnlineUser.getNumber()).child("UnFinish").child(model.getTitle()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Toast.makeText(HomeWork.this, "成功將 " + model.getTitle() + " 設定為完成。", Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    });
                                }

                            }
                        });

                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public HomeWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeworkdesign, parent , false);

                HomeWorkViewHolder holder = new HomeWorkViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }
}
