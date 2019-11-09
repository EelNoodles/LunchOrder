package com.example.lunchorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lunchorder.Model.Announcement;
import com.example.lunchorder.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AnnouncementSettingActivity extends AppCompatActivity {

    private EditText AnnouncementMessageEdit, AnnouncementTitleEdit, AnnouncementLinkEdit;
    private Button AnnouncementSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_setting);

        AnnouncementMessageEdit = (EditText) findViewById(R.id.AnnouncementMessageEdit);
        AnnouncementTitleEdit = (EditText) findViewById(R.id.AnnouncementTitleEdit);
        AnnouncementSendButton = (Button) findViewById(R.id.AnnouncementSendButton);
        AnnouncementLinkEdit = (EditText) findViewById(R.id.AnnouncementLinkEdit);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final Announcement announcement = dataSnapshot.child("Announcement").getValue(Announcement.class);

                AnnouncementTitleEdit.setText(announcement.getTitle());
                AnnouncementMessageEdit.setText(announcement.getMessage());
                AnnouncementLinkEdit.setText(announcement.getLink());

                AnnouncementSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String AnTitle = AnnouncementTitleEdit.getText().toString();
                        String AnMessage = AnnouncementMessageEdit.getText().toString();
                        String AnLink = AnnouncementLinkEdit.getText().toString();

                        final HashMap<String, Object> Announcement = new HashMap<>();
                        Announcement.put("Title", AnTitle);
                        Announcement.put("Message", AnMessage);
                        Announcement.put("Link", AnLink);

                        RootRef.child("Announcement").updateChildren(Announcement).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(AnnouncementSettingActivity.this, "成功設置公告", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AnnouncementSettingActivity.this, AdminSelectActivity.class);
                                startActivity(intent);

                            }
                        });

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
