package com.example.test123;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class getAnnouncements extends AppCompatActivity {

    ListView listView;
    ArrayList<String> teacher = new ArrayList<>();
    ArrayList<String> classroom = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    int count = 0;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_announcements);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait...");


        listView = findViewById(R.id.list_getAnnouncements);
        final Adapter_getAnnouncements arrayAdapter = new Adapter_getAnnouncements(this, teacher, classroom, title , desc, img);
        listView.setAdapter(arrayAdapter);

        progressDialog.show();
        query(arrayAdapter);

    }

    private void query(final Adapter_getAnnouncements arrayAdapter) {
        Query query = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid()).orderByChild(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final int classcount = (int) dataSnapshot.getChildrenCount();
                    Log.d("classname: ", String.valueOf(classcount));
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        final String classname = ds.child("classname").getValue(String.class);


                        query1(classname, arrayAdapter, classcount);

                    }
                }else{
                    progressDialog.dismiss();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.frame6, new nothing_to_show());
                    ft.commit();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void query1(final String classname, final Adapter_getAnnouncements arrayAdapter, final int classcount) {
        FirebaseDatabase.getInstance().getReference().child("Classroom").child(classname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String ownername = dataSnapshot.child("ownername").getValue(String.class);
                final String ownerpic = dataSnapshot.child("ownerpic").getValue(String.class);

                query2(classname,ownername, ownerpic, arrayAdapter, classcount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void query2(final String classname, final String ownername, final String ownerpic, final Adapter_getAnnouncements arrayAdapter, final int classcount){



        FirebaseDatabase.getInstance().getReference().child("Announcements").child(classname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    progressDialog.dismiss();

                    if(dataSnapshot.exists()){

                        for(DataSnapshot ds:dataSnapshot.getChildren()){

                            String ttitle = ds.child("title").getValue(String.class);
                            String ddesc = ds.child("desc").getValue(String.class);
                            teacher.add(ownername);
                            classroom.add(classname);
                            img.add(ownerpic);
                            title.add(ttitle);
                            desc.add(ddesc);
                            arrayAdapter.notifyDataSetChanged();

                        }
                    }

                    else {
                        //count if none announcements present
                        count++;
                    }

                    //classcount->total number of classes i've joined
                    if(count==classcount){
                        progressDialog.dismiss();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.frame6, new nothing_to_show());
                        ft.commit();

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
