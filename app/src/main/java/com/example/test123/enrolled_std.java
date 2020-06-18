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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class enrolled_std extends AppCompatActivity {

    String classname;
    ListView listView;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> std_uid = new ArrayList<>();
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrolled_std);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        classname = getIntent().getStringExtra("classname");
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        listView = findViewById(R.id.listview_joined);
        final Adapter_enr_std arrayAdapter = new Adapter_enr_std(this, title, img, std_uid);
        listView.setAdapter(arrayAdapter);
        Query query = FirebaseDatabase.getInstance().getReference("Classes/" + classname + "/Joined").orderByChild("uid");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayAdapter.clear();
                if(dataSnapshot.exists()){

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        String pic = ds.child("profilepic").getValue(String.class);
                        String uuid = ds.child("uid").getValue(String.class);

                        title.add(name);
                        img.add(pic);
                        std_uid.add(uuid);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.frame8, new nothing_to_show());
                    ft.commit();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                final String selectedItem = (String) parent.getItemAtPosition(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(enrolled_std.this);
                alert.setTitle("Start chat?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        Intent intent = new Intent(getApplicationContext(), chatwithStd.class);
                        intent.putExtra("uid", selectedItem);
                        startActivity(intent);


                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

            }
        });

    }
}



