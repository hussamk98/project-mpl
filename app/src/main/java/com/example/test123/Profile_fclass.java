package com.example.test123;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_fclass extends Fragment implements View.OnClickListener {


FirebaseUser firebaseUser;
FirebaseAuth firebaseAuth;
DatabaseReference ref;
TextView t1, t2, t3, t4;
CircleImageView c1;
Button b1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_layout, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid());

        t1 = v.findViewById(R.id.p_name);
        t2 = v.findViewById(R.id.p_email);
        t3 = v.findViewById(R.id.p_phone);
        t4 = v.findViewById(R.id.p_section);
        c1 = v.findViewById(R.id.p_imageview);
        b1 = v.findViewById(R.id.edit_profilebtn);



        b1.setOnClickListener(this);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String name = dataSnapshot.child("name").getValue(String.class);
                Users users = dataSnapshot.getValue(Users.class);

                t1.setText(users.getName());
                t2.setText(users.getEmail());
                t3.setText(users.getPhone());
                t4.setText("Section: " + users.getSec());
                Picasso.get().load(users.getProfilepic())
                        .into(c1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


    @Override
    public void onClick(View v) {
        if(v.equals(b1)){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame3, new editProfile_fclass()).addToBackStack(null);
            ft.commit();
        }

    }
}
