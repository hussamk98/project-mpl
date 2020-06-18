package com.example.test123;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class editProfile_fclass extends Fragment implements View.OnClickListener{
EditText e1, e2, e3;
Button b1, b2;
CircleImageView c1;
String new_name, new_phone, new_sec;
FirebaseUser firebaseUser;
FirebaseAuth firebaseAuth;
Map<String, String> map;
Map<String, String> map1;
Map<String, String> map2;
private ProgressDialog loadingBar;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri mImageuri;
    private CircleImageView profilepic;
private  String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.edit_profile, container, false);

        e1 = v.findViewById(R.id.edit_displayname);
        e2 =v.findViewById(R.id.edit_phonenum);
        e3 = v.findViewById(R.id.edit_section);
        b1 = v.findViewById(R.id.btn_saveinfo);
        b2 = v.findViewById(R.id.btn_uploadprofilepic);
        profilepic = v.findViewById(R.id.e_profilepic);
        c1 = v.findViewById(R.id.e_profilepic);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        loadingBar = new ProgressDialog(getContext());
        map = new HashMap<>();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users/" + firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);

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
        new_name = e1.getText().toString().trim();
        new_phone = e2.getText().toString().trim();
        new_sec = e3.getText().toString().trim();

        if(v.equals(b1)){

            if(new_name.equals("") && new_phone.equals("") && new_sec.equals("")){
                //none
                Toast.makeText(getContext(), "fields cant left empty", Toast.LENGTH_SHORT).show();

            }
            if(!new_name.equals("") && !new_phone.equals("") && !new_sec.equals("")){
                //all



//                map.put("profilepic", getmyProfilepicurl());
                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.child("profilepic").getValue(String.class);
                        map.put("name", new_name);
                        map.put("phone", new_phone);
                        map.put("sec", new_sec);
                        map.put("uid", firebaseUser.getUid());
                        map.put("profilepic", url);
                        map.put("email", firebaseUser.getEmail());
                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).setValue(map);

                        updatename();


                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                        getActivity().finish();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            if(!new_name.equals("") && new_phone.equals("") && new_sec.equals("")){
                //only name

                update();
                updatename();


                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
            if(!new_name.equals("") && !new_phone.equals("") && new_sec.equals("")){
                //name+phone
                update();
                updatename();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
            if(new_name.equals("") && !new_phone.equals("") && new_sec.equals("")){
                //phone only
                update();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
            if(new_name.equals("") && !new_phone.equals("") && !new_sec.equals("")){
                //phone+sec
                update();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
            if(new_name.equals("") && new_phone.equals("") && !new_sec.equals("")){
                //only sec
                update();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
            if(!new_name.equals("") && new_phone.equals("") && !new_sec.equals("")){
                //name + sec
                update();
                updatename();
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }

        }

        if(v.equals(b2)){
            openFilechooser();

        }

    }
    public void openFilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  && data != null && data.getData() != null ){


            loadingBar.setTitle("Set Profile Image");
            loadingBar.setMessage("Please wait, your profile image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mImageuri=data.getData();
            Picasso.get().load(mImageuri).into(profilepic);

            FirebaseStorage.getInstance().getReference().child("Profilepics")
                    .child(firebaseUser.getEmail())
                    .putFile(mImageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    final String image_link = FirebaseStorage.getInstance().getReference().child("Profilepics")
//                            .child(firebaseUser.getEmail()).getDownloadUrl().toString();


                    FirebaseStorage.getInstance().getReference().child("Profilepics")
                            .child(firebaseUser.getEmail())
                            .putFile(mImageuri)
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return  FirebaseStorage.getInstance().getReference().child("Profilepics")
                                    .child(firebaseUser.getEmail()).getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                final String downloadURL = downloadUri.toString();

                                updatenodeUserspic(downloadURL);
                                updatenodeClassroompic(downloadURL);
                                updatenodeClassespic(downloadURL);
                                loadingBar.dismiss();

                            }
                        }
                    });


                }
            });


        }
    }

public void updatenodeUserspic(final String url){

    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String sec = dataSnapshot.child("sec").getValue(String.class);
                    String uid = dataSnapshot.child("uid").getValue(String.class);

                    map.put("name", name);
                    map.put("phone", phone);
                    map.put("email", email);
                    map.put("sec", sec);
                    map.put("uid", uid);
                    map.put("profilepic", url);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    Toast.makeText(getContext(), "Profile pic updated", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
}

    public void updatenodeClassroompic(final String url){

        map1 = new HashMap<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("Classroom").orderByChild("ownerid").equalTo(firebaseUser.getUid().trim());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String allowjoin = ds.child("allowjoin").getValue(String.class);
                    String ownername = ds.child("ownername").getValue(String.class);
                     String classroom = ds.child("classroom").getValue(String.class);
                     String owneremail = ds.child("owneremail").getValue(String.class);

                    map1.put("allowjoin", allowjoin);
                    map1.put("classroom", classroom);
                    map1.put("owneremail", owneremail);
                    map1.put("ownername", ownername);
                    map1.put("ownerid", firebaseUser.getUid());
                    map1.put("ownerpic", url);
                    FirebaseDatabase.getInstance().getReference().child("Classroom").child(classroom).setValue(map1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void updatenodeClassespic(final String url){

        map2 = new HashMap<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid()).orderByChild("uid");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    final String classname = ds.child("classname").getValue(String.class);


                    FirebaseDatabase.getInstance().getReference().child("Classes").child(classname)
                            .child("Joined").child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String email = dataSnapshot.child("email").getValue(String.class);
                                    String join = dataSnapshot.child("join").getValue(String.class);
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String uid = dataSnapshot.child("uid").getValue(String.class);

                                    map2.put("email", email);
                                    map2.put("join", join);
                                    map2.put("name", name);
                                    map2.put("uid", uid);
                                    map2.put("profilepic", url);
                                    FirebaseDatabase.getInstance().getReference().child("Classes").child(classname).child("Joined").child(uid).setValue(map2);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void update(){

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("uid").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.child("name").getValue(String.class);
                    String phone = ds.child("phone").getValue(String.class);
                    String uid = ds.child("uid").getValue(String.class);
                    String sec = ds.child("sec").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String url = ds.child("profilepic").getValue(String.class);

                    if(!new_name.equals("") && new_phone.equals("") && new_sec.equals("")){
                        map.put("name", new_name);
                        map.put("phone", phone);
                        map.put("uid", uid);
                        map.put("sec", sec);
                        map.put("profilepic", url);
                        map.put("email", email);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);

                    }
                    if(!new_name.equals("") && !new_phone.equals("") && new_sec.equals("")){
                        map.put("name", new_name);
                        map.put("phone", new_phone);
                        map.put("uid", uid);
                        map.put("sec", sec);
                        map.put("profilepic", url);
                        map.put("email", email);

                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    }
                    if(new_name.equals("") && !new_phone.equals("") && new_sec.equals("")){
                        map.put("name", name);
                        map.put("phone", new_phone);
                        map.put("uid", uid);
                        map.put("sec", sec);
                        map.put("profilepic", url);
                        map.put("email", email);


                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    }
                    if(new_name.equals("") && !new_phone.equals("") && !new_sec.equals("")){
                        map.put("name", name);
                        map.put("phone", new_phone);
                        map.put("uid", uid);
                        map.put("sec", new_sec);
                        map.put("profilepic", url);
                        map.put("email", email);


                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    }
                    if(new_name.equals("") && new_phone.equals("") && !new_sec.equals("")){
                        map.put("name", name);
                        map.put("phone", phone);
                        map.put("uid", uid);
                        map.put("sec", new_sec);
                        map.put("profilepic", url);
                        map.put("email", email);

                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    }
                    if(!new_name.equals("") && new_phone.equals("") && !new_sec.equals("")){
                        map.put("name", new_name);
                        map.put("phone", phone);
                        map.put("uid", uid);
                        map.put("sec", new_sec);
                        map.put("profilepic", url);
                        map.put("email", email);


                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updatename(){

        //update ownername
        Query query = FirebaseDatabase.getInstance().getReference().child("Classroom").orderByChild("ownerid").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    String allowjoin = ds.child("allowjoin").getValue(String.class);
                    String classroom = ds.child("classroom").getValue(String.class);
                    String owneremail = ds.child("owneremail").getValue(String.class);
                    String ownerid = ds.child("ownerid").getValue(String.class);
                    String ownerpic = ds.child("ownerpic").getValue(String.class);

                    Map<String, String> newmap = new HashMap<>();
                    newmap.put("allowjoin", allowjoin);
                    newmap.put("classroom", classroom);
                    newmap.put("owneremail", owneremail);
                    newmap.put("ownerid", ownerid);
                    newmap.put("ownerpic", ownerpic);
                    newmap.put("ownername", new_name);
                    FirebaseDatabase.getInstance().getReference().child("Classroom").child(classroom).setValue(newmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //update classes/*classname*/joined/*uid*/name
        Query query1 = FirebaseDatabase.getInstance().getReference()
                .child("joinedclasses")
                .child(firebaseUser.getUid())
                .orderByChild("uid")
                .equalTo(firebaseUser.getUid());

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    final String classname = ds.child("classname").getValue(String.class);

                    Query query2 = FirebaseDatabase.getInstance().getReference()
                            .child("Classes")
                            .child(classname)
                            .child("Joined")
                            .orderByChild("uid")
                            .equalTo(firebaseUser.getUid());
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                String email = ds.child("email").getValue(String.class);
                                String join = ds.child("join").getValue(String.class);
                                String uid = ds.child("uid").getValue(String.class);
                                String pic = ds.child("profilepic").getValue(String.class);

                                Map<String, String> mapnew = new HashMap<>();
                                mapnew.put("email", email);
                                mapnew.put("join", join);
                                mapnew.put("uid", uid);
                                mapnew.put("profilepic", pic);
                                mapnew.put("name", new_name);
                                FirebaseDatabase.getInstance().getReference().child("Classes").child(classname).child("Joined").child(uid).setValue(mapnew);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
