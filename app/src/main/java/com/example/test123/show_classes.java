package com.example.test123;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_classes extends AppCompatActivity {

    ListView listView;
    TextView t1;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> teacher = new ArrayList<>();
    ArrayList<String> std_count = new ArrayList<>();
    ArrayList<String> img = new ArrayList<String>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_classes);

        t1 = findViewById(R.id.textview_showclasses);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        if(firebaseAuth.getCurrentUser() == null && !firebaseAuth.getCurrentUser().isEmailVerified()){
            finish();
            startActivity(new Intent(this, login_signup_forgot_fclass.class));
        }

        final Query query = FirebaseDatabase.getInstance().getReference("Classroom").orderByChild("ownerid").equalTo(firebaseAuth.getUid().trim());


        listView = findViewById(R.id.listview_showclasses);
        final adapter_show_classes arrayAdapter = new adapter_show_classes(this, title, teacher, std_count , img);
        arrayAdapter.notifyDataSetChanged();
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                switch ((int) dataSnapshot.getChildrenCount()){

                    case 0:

                        t1.setText("NO CLASSES");
                        progressDialog.dismiss();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.frame9, new nothing_to_show());
                        ft.commit();
                        break;
                    default:

                        t1.setText("My Classes");

                        //query for classname and ownername
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds:dataSnapshot.getChildren()){

                                    final String ttitle = ds.child("classroom").getValue(String.class);
                                    final String pic = ds.child("ownerpic").getValue(String.class);

                                    //nested in for loop
                                    //query for counting enrolled students
                                    Query query1 = FirebaseDatabase.getInstance().getReference("Classes/" + ttitle + "/Joined")
                                            .orderByChild("uid");
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            final int enrolled = (int) dataSnapshot.getChildrenCount();

//                                            Query query2 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.)

                                            title.add(ttitle);
                                            teacher.add("Instructor: YOU" );
                                            std_count.add("enrolled: " + enrolled);
                                            img.add(pic);
                                            arrayAdapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });

                                }
                                progressDialog.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                 final String selectedItem = (String) parent.getItemAtPosition(position);


                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_myclasses, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.new_announcement:
                                //TODO HERE
                                Intent i = new Intent(getApplicationContext(), setAnnouncement.class);
                                i.putExtra("classroom", selectedItem);
                                startActivity(i);

                                return true;


                            case R.id.view_enr_stds:
                                Intent intent = new Intent(getApplicationContext(), enrolled_std.class);
                                intent.putExtra("classname", selectedItem);
                                startActivity(intent);
                                return true;
                        }

                        Toast.makeText(getApplicationContext(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });




                popup.show();//showing popup menu


                /*
                *Joined.java - who joined myClass - pass the position of clicked class and get intent "classroom" search
                *that class and get names of all those who have joined that class
                */





            }
        });

    }
}
