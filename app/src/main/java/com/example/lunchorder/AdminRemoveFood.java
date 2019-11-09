package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunchorder.Model.Food;
import com.example.lunchorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminRemoveFood extends AppCompatActivity {

    private DatabaseReference FoodRef;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;

    private String FoodStore = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_remove_food);

        FoodStore = getIntent().getStringExtra("FoodStore");

        FoodRef = FirebaseDatabase.getInstance().getReference().child("Food").child("Food").child(FoodStore);
        recyclerview = findViewById(R.id.RemoveFoodActivityRecycleMenu);
        recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

    }

    protected void onStart(){

        super.onStart();

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(FoodRef, Food.class).build();

        final FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull final Food model) {

                holder.DisplaySelectFoodStoreName.setText(model.getFood());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[] = new CharSequence[]{

                                "刪除此餐點。",

                        };

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AdminRemoveFood.this);
                        builder.setTitle("請選擇動作");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(i == 0){

                                    final DatabaseReference RootRef;
                                    RootRef = FirebaseDatabase.getInstance().getReference();

                                    RootRef.child("Food").child("Food").child(FoodStore).child(model.getFood()).removeValue();

                                }
                            }
                        });

                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_foodselectdesing, parent , false);

                FoodViewHolder holder = new FoodViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

}
