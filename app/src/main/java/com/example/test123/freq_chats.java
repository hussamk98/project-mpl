package com.example.test123;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class freq_chats extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ListView listView;
    ArrayList<String> Title = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    ArrayList<String> userid = new ArrayList<>();


    DatabaseReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v =   inflater.inflate(R.layout.freq_chats, container, false);
//        MainActivity.toolbar.setTitle("Chats");

        listView = v.findViewById(R.id.list_freq_chats);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        final Adapterfreq_chats arrayAdapter = new Adapterfreq_chats(getActivity(), Title, img, userid);
        listView.setAdapter(arrayAdapter);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait while your chats are fetched from the server.");
        progressDialog.show();



        ref = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(firebaseUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        String chatedwithid = ds.child("id").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(chatedwithid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        progressDialog.dismiss();

                                            String name = dataSnapshot.child("name").getValue(String.class);
                                            String pic = dataSnapshot.child("profilepic").getValue(String.class);
                                            String uid = dataSnapshot.child("uid").getValue(String.class);

                                            Title.add(name);
                                            img.add(pic);
                                            userid.add(uid);
                                            arrayAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
                else{
                    progressDialog.dismiss();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.frame1, new nothing_to_show());
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

                Intent intent = new Intent(getContext(), Chats2.class);
                intent.putExtra("userid", selectedItem);
                startActivity(intent);

//                Toast.makeText(getContext(), selectedItem, Toast.LENGTH_LONG).show();



            }
        });



        return v;

    }




}
