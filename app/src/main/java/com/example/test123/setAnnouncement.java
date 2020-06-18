package com.example.test123;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class setAnnouncement extends AppCompatActivity {

    EditText e1, e2;
    Button b1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_announcement);

        e1 = findViewById(R.id.et_announce_title);
        e2 = findViewById(R.id.et_announce_desc);
        b1 = findViewById(R.id.bt_post_announce);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final Map<String, String> map  = new HashMap<>();
                 final String title = e1.getText().toString().trim();
                 final String desc = e2.getText().toString().trim();

                 if(title.equals("")){
                     Toast.makeText(getApplicationContext(), "Error. Title can't left empty", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 else if(title.equals("") && desc.equals("")){
                     Toast.makeText(getApplicationContext(), "Error. Can't left empty", Toast.LENGTH_SHORT).show();
                     return;
                 }else{
                     FirebaseDatabase.getInstance().getReference().child("Announcements").child(getIntent().getStringExtra("classroom"))
                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     map.put("title", title);
                                     map.put("desc", desc);
                                     map.put("uid", firebaseUser.getUid());
                                     FirebaseDatabase.getInstance().getReference().child("Announcements").child(getIntent().getStringExtra("classroom"))
                                             .push().setValue(map);
                                     Toast.makeText(getApplicationContext(), "Announcement Successfull", Toast.LENGTH_SHORT).show();

                                     Intent i = new Intent(getApplicationContext(), show_classes.class);
                                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                     finish();

                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });

                 }

            }
        });


    }
}
