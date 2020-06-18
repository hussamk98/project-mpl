package com.example.test123;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwithStd extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageButton btn_sendmessage;
    EditText et_writemessage;
    CircleImageView img;

    adapter_chatbubbles madapter_chatbubbles;
    List<ref_chats> mref_chats;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        Toolbar toolbar =  findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setToolbarimg();

        btn_sendmessage = findViewById(R.id.btn_sendmessage);
        et_writemessage = findViewById(R.id.et_writemessage);
        recyclerView = findViewById(R.id.messages_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setTile();

        readMessages(firebaseUser.getUid(), getIntent().getStringExtra("uid"));
        btn_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_writemessage.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), getIntent().getStringExtra("uid"), msg);
                    et_writemessage.setText("");
                }else{
//                            Toast.makeText(chats.this, "You can't send empty message", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setToolbarimg() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child("profilepic").getValue(String.class);
                img = findViewById(R.id.toolbar_chat_image);
                Picasso.get().load(url).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTile(){
        String uuid = getIntent().getStringExtra("uid");
        FirebaseDatabase.getInstance().getReference().child("Users").child(uuid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        getSupportActionBar().setTitle(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage(String sender, final String reciever, String message){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);
        hashMap.put("message", message);
        ref.child("Chats").push().setValue(hashMap);


        //chatlist - mychats with users
        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference().child("Chatlist")
                .child(firebaseUser.getUid())
                .child(reciever);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref.child("id").setValue(reciever);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //chatlist - their chats with me
        final DatabaseReference chatref2 = FirebaseDatabase.getInstance().getReference().child("Chatlist")
                .child(reciever)
                .child(firebaseUser.getUid());
        chatref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref2.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readMessages(final String myid, final String userid ){
        DatabaseReference ref;
        mref_chats = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mref_chats.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    ref_chats chat = ds.getValue(ref_chats.class);
                    if(chat.getReciever().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(myid)){

                        mref_chats.add(chat);
                    }
                    madapter_chatbubbles = new adapter_chatbubbles(chatwithStd.this, mref_chats);
                    recyclerView.setAdapter(madapter_chatbubbles);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("EXIT", true);
        startActivity(i);
        finish();

    }

}
