package com.example.lunchorder;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Food;
import com.example.lunchorder.Model.LastUpdateTime;
import com.example.lunchorder.Model.Leave;
import com.example.lunchorder.Model.Users;
import com.example.lunchorder.Prevalent.Prevalent;
import com.example.lunchorder.ViewHolder.FoodViewHolder;
import com.example.lunchorder.ViewHolder.RollCallViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.paperdb.Paper;

public class  AdminRollCallActivity extends AppCompatActivity {

    private DatabaseReference UserRef;
    private RecyclerView recyclerview;
    private TextView AdminRollCallTitle, AdminRollLastUpdate;
    private ImageView AdminRollCallBackMainActivity, AdminRollCallChooseWeek;
    RecyclerView.LayoutManager layoutManager;
    private String Date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_roll_call);

        Date = getIntent().getStringExtra("Date");

        UserRef = FirebaseDatabase.getInstance().getReference().child("Leave").child(Date);

        recyclerview = findViewById(R.id.AdminRollCallRecycleView);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        AdminRollCallTitle = (TextView) findViewById(R.id.AdminRollCallTitle);
        AdminRollCallBackMainActivity = (ImageView) findViewById(R.id.AdminRollCallBackMainActivity);
        AdminRollCallChooseWeek = (ImageView) findViewById(R.id.AdminRollCallChooseWeek);
        AdminRollLastUpdate = (TextView) findViewById(R.id.AdminRollLastUpdate);

        AdminRollCallTitle.setText("點名列表" + "（" + Date + "）");

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LastUpdateTime leave = dataSnapshot.child("Leave").child("LastUpdateTime").child(Date).getValue(LastUpdateTime.class);

                AdminRollLastUpdate.setText("最新更新：" + leave.getLastUpdateTime());

                SaveTime();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AdminRollCallBackMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminRollCallActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        AdminRollCallChooseWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{

                        "星期一。",
                        "星期二。",
                        "星期三。",
                        "星期四。",
                        "星期五。",
                        "星期六上午。",
                        "星期六下午。",
                        "刪除本日點名清單。",
                        "移除管理員認證碼並登出。"

                };

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminRollCallActivity.this);
                builder.setTitle("請選擇點名星期");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(i == 0){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期一未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {3, 4, 8, 25, 26, 28, 29, 41};

                            LeaveList(W);

                        }

                        if(i == 1){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期二未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {3, 8, 23, 26, 29, 30, 31, 36, 37, 39, 41};

                            LeaveList(W);

                        }

                        if(i == 2){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期三未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {1, 2, 3, 4, 5, 6, 8, 9, 10, 20, 21, 26, 28, 29, 31, 41};

                            LeaveList(W);

                        }

                        if(i == 3){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期四未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {2, 3, 8, 10, 18, 19, 21,25, 26, 29, 30, 36, 37, 41};

                            LeaveList(W);

                        }

                        if(i == 4){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期五未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {1, 2, 3, 5, 7, 8, 9, 10, 11, 16, 18, 20, 21, 23, 24, 25, 26, 27, 28, 29, 32, 33, 34, 38, 39, 41};

                            LeaveList(W);

                        }

                        if(i == 5){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期六上午未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {1, 4, 9, 10, 12, 18, 21, 29, 41};

                            LeaveList(W);

                        }

                        if(i == 6){

                            Toast.makeText(AdminRollCallActivity.this, "讀取星期六下午未出席名單。", Toast.LENGTH_SHORT).show();

                            int[] W = {3, 4, 8, 9, 11, 12, 13, 18, 21, 23, 25, 29, 32, 34, 37, 41};

                            LeaveList(W);

                        }

                        if(i == 7){

                            RootRef.child("Leave").child(Date).removeValue();
                            RootRef.child("Leave").child("LastUpdateTime").child(Date).removeValue();

                            Intent intent = new Intent(AdminRollCallActivity.this, MainActivity.class);
                            startActivity(intent);

                        }

                        if(i == 8){

                            Paper.book().delete("RollCall");

                            Intent intent = new Intent(AdminRollCallActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });

                builder.show();

            }
        });


    }

    protected void onStart(){

        super.onStart();

        final FirebaseRecyclerOptions<Leave> options = new FirebaseRecyclerOptions.Builder<Leave>().setQuery(UserRef, Leave.class).build();

        final FirebaseRecyclerAdapter<Leave, RollCallViewHolder> adapter = new FirebaseRecyclerAdapter<Leave, RollCallViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final RollCallViewHolder holder, int position, @NonNull final Leave model) {

                holder.username.setText(model.getName());
                holder.usernumber.setText(model.getNumber());
                holder.status.setText(model.getStatus());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String Number = model.getNumber();
                        String Name = model.getName();
                        String LeaveResult = model.getLeaveResult();

                        if(model.getStatus().equals("✦") || model.getStatus().equals("✘  缺席。") || model.getStatus().equals("✚  晚到。")){

                            NoCome(Number, Name);

                        }else if(model.getStatus().equals("▲  請假。")){

                            Leave(Number, Name, LeaveResult);

                        }else if(model.getStatus().equals("✔  出席。")){

                            Absence(Number, Name);

                        }

                    }
                });

                AdminRollCallTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int count = getItemCount();
                        int NocomeAttendence = 0;
                        int Absence = 0;
                        int Late = 0;
                        int Leave = 0;

                        for (int a = 0; a < count ; a++){

                            if(getItem(a).getStatus().equals("✦")){

                                NocomeAttendence  = NocomeAttendence + 1;

                            }else if(getItem(a).getStatus().equals("✘  缺席。")){

                                Absence = Absence + 1;

                            }else if(getItem(a).getStatus().equals("✚  晚到。")){

                                Late = Late + 1;

                            }else if(getItem(a).getStatus().equals("▲  請假。")){

                                Leave = Leave + 1;

                            }

                        }

                        int ActualAttendence = 41 - NocomeAttendence - Absence - Late - Leave;
                        int ExpectedAttendence = 41 - NocomeAttendence - Leave - Late;

                        String SActualAttendence = String.valueOf(ActualAttendence);
                        String SExpectedAttendence = String.valueOf(ExpectedAttendence);
                        String SLeave = String.valueOf(Leave);
                        String SNocomeAttendence = String.valueOf(NocomeAttendence);
                        String SAbsence = String.valueOf(Absence);
                        String SLate = String.valueOf(Late);

                        final View item = LayoutInflater.from(AdminRollCallActivity.this).inflate(R.layout.statsrollcallnumberdialogdesing, null);

                        TextView SRCNDTitle = (TextView) item.findViewById(R.id.StatsRollCallNumberDialogTitle);
                        TextView SRCNDMainStats = (TextView) item.findViewById(R.id.StatsRollCallNumberDialogMainStats);
                        TextView SRCNDSubStats = (TextView) item.findViewById(R.id.StatsRollCallNumberDialogSubStats);

                        SRCNDTitle.setText(Date + "\n人數統計資料");
                        SRCNDMainStats.setText("應到人數：" + SExpectedAttendence + "\n實到人數：" + SActualAttendence);
                        SRCNDSubStats.setText("調查不來人數：" + SNocomeAttendence + "\n請假人數：" + SLeave + "\n缺曠人數：" + SAbsence + "\n晚到人數：" + SLate);

                        new AlertDialog.Builder(AdminRollCallActivity.this)
                                .setCancelable(false)
                                .setView(item)
                                .setPositiveButton("關閉顯示", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                })

                                .show();

                    }
                });

            }

            @NonNull
            @Override
            public RollCallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rollcalldesign, parent , false);

                RollCallViewHolder holder = new RollCallViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

    private void LeaveList(final int[] W){

        Date = getIntent().getStringExtra("Date");

        for(int a = 0; a < W.length ; a++){

            final int Number = W[a];

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String sNumber = String.valueOf(Number);

                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", Date);
                    LeaveMap.put("LeaveResult", "出席表單顯示不來。");
                    LeaveMap.put("Status", "✦");

                    RootRef.child("Leave").child(Date).child(sNumber).updateChildren(LeaveMap);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void NoCome(final String Number, final String Name){

        CharSequence options[] = new CharSequence[]{

                "移除缺席狀態。"

        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminRollCallActivity.this);
        builder.setTitle("請選擇動作");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                if (i == 0) {
                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", null);
                    LeaveMap.put("LeaveResult", null);
                    LeaveMap.put("Status", null);

                    RootRef.child("Leave").child(Date).child(Number).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AdminRollCallActivity.this, "成功移除 " + Name + " 的缺席狀態。", Toast.LENGTH_SHORT).show();
                            SaveTime();

                        }
                    });
                }
            }
        });

        builder.show();
    }

    private void Leave(final String Number, final String Name, final String LeaveResult){

        CharSequence options[] = new CharSequence[]{

                "查看請假資訊。",
                "移除請假狀態。"

        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminRollCallActivity.this);
        builder.setTitle("請選擇動作");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                if(i == 0){

                    final View item = LayoutInflater.from(AdminRollCallActivity.this).inflate(R.layout.leaveresultdesign, null);

                    TextView LRTitle = (TextView) item.findViewById(R.id.LeaveResulttitle);
                    TextView LRMessage = (TextView) item.findViewById(R.id.LeaveResultmessage);

                    LRTitle.setText(Name);
                    LRMessage.setText("姓名：" + Name + "\n座號：" + Number + "\n請假事由：" + LeaveResult);

                    new AlertDialog.Builder(AdminRollCallActivity.this)
                            .setCancelable(false)
                            .setView(item)
                            .setPositiveButton("關閉顯示", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                }
                            })

                            .show();

                }

                if (i == 1) {
                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", null);
                    LeaveMap.put("LeaveResult", null);
                    LeaveMap.put("Status", null);

                    RootRef.child("Leave").child(Date).child(Number).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AdminRollCallActivity.this, "成功移除 " + Name + " 的請假狀態。", Toast.LENGTH_SHORT).show();
                            SaveTime();

                        }
                    });
                }
            }
        });

        builder.show();
    }

    private void Absence(final String Number, final String Name){

        CharSequence options[] = new CharSequence[]{

                "改為缺席狀態。",
                "改為請假狀態。",
                "改為晚到狀態。"

        };

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminRollCallActivity.this);
        builder.setTitle("請選擇動作");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                if (i == 0) {
                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", Date);
                    LeaveMap.put("LeaveResult", "點名時未出席且無請假。");
                    LeaveMap.put("Status", "✘  缺席。");

                    RootRef.child("Leave").child(Date).child(Number).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AdminRollCallActivity.this, "成功將 " + Name + " 移至缺席狀態。", Toast.LENGTH_SHORT).show();
                            SaveTime();

                        }
                    });
                }

                if (i == 1) {
                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", Date);
                    LeaveMap.put("LeaveResult", "由點名者修改為請假狀態。");
                    LeaveMap.put("Status", "▲  請假。");

                    RootRef.child("Leave").child(Date).child(Number).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AdminRollCallActivity.this, "成功將 " + Name + " 移至請假狀態。", Toast.LENGTH_SHORT).show();
                            SaveTime();

                        }
                    });
                }

                if (i == 2) {
                    final HashMap<String, Object> LeaveMap = new HashMap<>();
                    LeaveMap.put("LeaveDate", Date);
                    LeaveMap.put("LeaveResult", "由點名者修改為晚到狀態。");
                    LeaveMap.put("Status", "✚  晚到。");

                    RootRef.child("Leave").child(Date).child(Number).updateChildren(LeaveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(AdminRollCallActivity.this, "成功將 " + Name + " 移至晚到狀態。", Toast.LENGTH_SHORT).show();
                            SaveTime();

                        }
                    });
                }
            }
        });

        builder.show();

    }

    private void SaveTime(){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");

        java.util.Date dt=new Date();

        String dts=sdf.format(dt);

        AdminRollLastUpdate.setText("最新更新：" + dts);

        final HashMap<String, Object> LastUpdateTime = new HashMap<>();
        LastUpdateTime.put("LastUpdateTime", dts);

        final DatabaseReference LastUpdateTimeRef;
        LastUpdateTimeRef = FirebaseDatabase.getInstance().getReference().child("Leave").child("LastUpdateTime").child(Date);

        LastUpdateTimeRef.updateChildren(LastUpdateTime);

    }

}
