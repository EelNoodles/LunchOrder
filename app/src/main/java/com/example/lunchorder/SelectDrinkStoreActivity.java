package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.lunchorder.Model.Food;
import com.example.lunchorder.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectDrinkStoreActivity extends AppCompatActivity {

    private DatabaseReference FoodRef, DateFoodRef, DateDrinkRef;
    private RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drink_store);

        FoodRef = FirebaseDatabase.getInstance().getReference().child("Store").child("DrinkStore");
        recyclerview = findViewById(R.id.SelectDrinkStoreActivityRecycleMenu);
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

                holder.DisplaySelectDrinkStoreName.setText(model.getStoreName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(SelectDrinkStoreActivity.this, AdminRemoveDrink.class);
                        intent.putExtra("FoodStore", model.getStoreName());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_drinkselectdesign, parent , false);

                FoodViewHolder holder = new FoodViewHolder(view);

                return holder;


            }
        };

        recyclerview.setAdapter(adapter);
        adapter.startListening();

    }

}
