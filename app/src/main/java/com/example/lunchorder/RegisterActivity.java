package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.sax.RootElement;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button RegisterButton;
    private EditText InputName, InputNumber;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button) findViewById(R.id.registerbutton);
        InputName = (EditText) findViewById(R.id.registername);
        InputNumber = (EditText) findViewById(R.id.registernumber);
        loadingBar = new ProgressDialog(this);


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount(){
        String Name = InputName.getText().toString();
        String Number = InputNumber.getText().toString();

        if (TextUtils.isEmpty(Name)){

            Toast.makeText(this, "請輸入姓名。", Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(Number)){

            Toast.makeText(this, "請輸入座號。", Toast.LENGTH_LONG).show();

        }else{
            loadingBar.setTitle("正在創立帳號...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidNameNumber(Name, Number);
        }
    }

    private void ValidNameNumber(final String Name, final String Number){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(Number).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Number", Number);
                    userdataMap.put("Name", Name);

                    RootRef.child("Users").child(Number).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(RegisterActivity.this, "您的帳號已經成功註冊。", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, AdminSelectActivity.class);
                                startActivity(intent);

                            }else{

                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "目前出錯，請稍後在嘗試。", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }else{

                    Toast.makeText(RegisterActivity.this, "這個座號註冊過了。", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

