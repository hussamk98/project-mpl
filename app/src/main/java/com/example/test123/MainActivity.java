package com.example.test123;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
//    DatabaseReference ref;
//    public static  Toolbar toolbar;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_chats:
                     fm= getSupportFragmentManager();
                     ft = fm.beginTransaction();
                    ft.replace(R.id.frame1, new freq_chats());
                    ft.commit();
                    return true;

                case R.id.navigation_dashboard:
                     fm= getSupportFragmentManager();
                     ft = fm.beginTransaction();
                    ft.replace(R.id.frame1, new Dashboard());
                    ft.commit();
                    return true;
            }
            return false;
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        super.onCreateOptionsMenu(menu);
//        return true;
//    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//
//            case R.id.settings:
//                startActivity(new Intent(this, Settings.class));
//                return true;
//
//
//            case R.id.joinclass:
//
//
//                LayoutInflater li = LayoutInflater.from(this);
//                View promptsView = li.inflate(R.layout.newclassroom, null);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                alertDialogBuilder.setView(promptsView);
//                final EditText userInput =  promptsView .findViewById(R.id.et_classid);
//                userInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("Join",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, final int id) {
//
//
//
//                                        //code for Joining classroom starts here
//
//                                        firebaseAuth = FirebaseAuth.getInstance();
//                                        firebaseUser = firebaseAuth.getCurrentUser();
//                                        Query query = FirebaseDatabase.getInstance().getReference("Classroom/")
//                                                .orderByChild("classroom").equalTo(userInput.getText().toString().trim());
//
//
//                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
//                                                switch (((int) dataSnapshot.getChildrenCount())) {
//
//
//                                                    case 1:
//                                                        //if class exists
//                                                        ref = FirebaseDatabase.getInstance().getReference("Classroom/" + userInput.getText().toString().trim() + "/");
//                                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                Classroom classroom = dataSnapshot.getValue(Classroom.class);
//
//                                                                //if already joined(admin)
//                                                                if(classroom.getOwnerid().equals(firebaseUser.getUid())){
//                                                                   Toast.makeText(getApplicationContext(), "Your'e the admin", Toast.LENGTH_LONG).show();
//                                                                   return;
//                                                                }
//                                                                //if not joined
//                                                                else if(!classroom.getOwnerid().equals(firebaseUser.getUid())){
//
//                                                                    if(classroom.getAllowjoin().equals("true")){
//                                                                        Query query1 = FirebaseDatabase.getInstance().getReference("Users/").orderByChild("uid").equalTo(firebaseUser.getUid());
//                                                                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                                                String name = null;
//                                                                                String picprofile =null;
//                                                                                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                                                                                    name = ds.child("name").getValue(String.class);
//                                                                                    picprofile = ds.child("profilepic").getValue(String.class);
//                                                                                }
//
//
//                                                                                Query query2 = FirebaseDatabase.getInstance().getReference("Classes/" + userInput.getText().toString().trim() + "/Joined/").orderByChild("uid").equalTo(firebaseUser.getUid());
//                                                                                final String finalName = name;
//                                                                                final String finalPicprofile = picprofile;
//                                                                                query2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                                    @Override
//                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                        String joined = "false";
//                                                                                        for(DataSnapshot ds:dataSnapshot.getChildren()){
//                                                                                            joined = ds.child("join").getValue(String.class);
//                                                                                        }
//
//                                                                                        if(joined.equals("true")){
//                                                                                            Toast.makeText(getApplicationContext(), "Already joined :)", Toast.LENGTH_SHORT).show();
//                                                                                        }
//                                                                                        else if(joined.equals("false")) {
//                                                                                            /*  insert into  Classes/"classname"/Joined/"userID"   */
//                                                                                            Map<String, String> user = new HashMap<>();
//                                                                                            user.put("uid", firebaseUser.getUid());
//                                                                                            user.put("name", finalName);
//                                                                                            user.put("profilepic", finalPicprofile);
//                                                                                            user.put("email", firebaseUser.getEmail());
//                                                                                            user.put("join", "true");
//                                                                                            FirebaseDatabase.getInstance().getReference("Classes/"
//                                                                                                    + userInput.getText().toString().trim() + "/Joined/"
//                                                                                                    + firebaseUser.getUid()).setValue(user);
//
//
//                                                                                            /*  insert into joinedclasses/"userID"/push()     */
//                                                                                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid());
//                                                                                            String push = ref1.push().getKey().trim();
//                                                                                            Map<String, String> map = new HashMap<>();
//                                                                                            map.put("classname", userInput.getText().toString().trim());
//                                                                                            map.put("uid", firebaseUser.getUid());
//                                                                                            map.put("push", push);
//                                                                                            ref1.child(push).setValue(map);
//
//                                                                                            Toast.makeText(getApplicationContext(), "Class join Success :)", Toast.LENGTH_SHORT).show();
//                                                                                        }
//                                                                                    }
//
//                                                                                    @Override
//                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                                    }
//                                                                                });
//
//
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    else if(classroom.getAllowjoin().equals("false")){
//                                                                        Toast.makeText(getApplicationContext(), "Join permission denied", Toast.LENGTH_SHORT).show();
//                                                                    }
//
//                                                                    return;
//                                                                }
//                                                            }
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                            }
//                                                        });
//                                                        break;
//
//
//                                                    case 0:
//                                                        Toast.makeText(getApplicationContext(), "No Classes Found :(", Toast.LENGTH_LONG).show();
//                                                        break;
//
//                                                    default:
//                                                        Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
//                                                        break;
//                                                }
//                                                return;
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
////
//                                        //code for new classroom ends here
//
//
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();
//
//
//
//
//
//
//                return true;
//            case R.id.newclass:
//                LayoutInflater lii = LayoutInflater.from(this);
//                View promptsVieww = lii.inflate(R.layout.newclassroom, null);
//                AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(this);
//                // set newclassroom.xml to alertdialog builder
//                alertDialogBuilderr.setView(promptsVieww);
//                final EditText userInputt =  promptsVieww .findViewById(R.id.et_classid);
//                userInputt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//                // set dialog message
//                alertDialogBuilderr
//                        .setCancelable(false)
//                        .setPositiveButton("Create",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//
//                                        //code for new classroom creation starts here
//                                        {
//                                            firebaseAuth = FirebaseAuth.getInstance();
//                                            firebaseUser = firebaseAuth.getCurrentUser();
//
//                                            ref = FirebaseDatabase.getInstance().getReference("Classroom/");
//
//                                            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
//                                            {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                    String name = dataSnapshot.child("name").getValue(String.class);
//                                                    String photo = dataSnapshot.child("profilepic").getValue(String.class);
//
////                                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
////                                                        name = ds.child("name").getValue(String.class);
////                                                        photo = ds.child("profilepic").getValue(String.class);
////                                                    }
//
//                                                    final Map<String, String> user = new HashMap<>();
//                                                    user.put("classroom", userInputt.getText().toString().trim());
//                                                    user.put("ownerid", firebaseUser.getUid());
//                                                    user.put("owneremail", firebaseUser.getEmail());
//                                                    user.put("ownername", name);
//                                                    user.put("ownerpic", photo);
//                                                    user.put("allowjoin", "true");
//
//
//                                                    Query query = FirebaseDatabase.getInstance().getReference("Classroom/")
//                                                            .orderByChild("classroom").equalTo(userInputt.getText().toString().trim());
//
//
//
//                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////
//                                                            switch (((int) dataSnapshot.getChildrenCount())) {
//                                                                case 1:
//                                                                    Toast.makeText(getApplicationContext(), "error, name already taken!", Toast.LENGTH_SHORT).show();
//                                                                    break;
//                                                                case 0:
//                                                                    Toast.makeText(getApplicationContext(), "class: '" + userInputt.getText().toString().trim() + "' created successfully :)", Toast.LENGTH_LONG).show();
//                                                                    ref.child(userInputt.getText().toString().trim()).setValue(user);
//                                                                    break;
//
//                                                                default:
//                                                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
//                                                                    break;
//                                                            }
//                                                            return;
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                        }
//                                                    });
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
////                                            query1.addListenerForSingleValueEvent(new ValueEventListener() );
////                                        ref.addListenerForSingleValueEvent(valueEventListener);
//
//
//
//
//                                        }
//                                        //code for new classroom ends here
//
//
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialogg = alertDialogBuilderr.create();
//
//                // show it
//                alertDialogg.show();
//
//
//
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_chats);



//        BottomNavigationView navigation = findViewById(R.id.navigation);

//        toolbar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("AppName");

//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        firebaseAuth = firebaseAuth.getInstance();
         firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null && !firebaseUser.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), login_signup_forgot_fclass.class));
            finish();
        }

        firebaseUser =firebaseAuth.getCurrentUser();
//        firebaseUser.getEmail();     displaying current logged in user's email.


        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame1, new freq_chats());
        ft.commit();




    }


}
