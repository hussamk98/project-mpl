package com.example.test123;

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
import android.widget.Switch;
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


public class Manageclass extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    /** MyClasses**/
    ListView myClasses_listView;
    ArrayList<String> myClasses_title = new ArrayList<>();
    ArrayList<String> myClasses_sub = new ArrayList<>();
    ArrayList<String> myClasses_uid = new ArrayList<>();
    ArrayList<String> myClasses_img = new ArrayList<>();

    /** MyJoinedClasses**/
    ListView joinedClasses_listview;
    ArrayList<String> joinedClasses_title = new ArrayList<>();
    ArrayList<String> joinedClasses_sub = new ArrayList<>();
    ArrayList<String> joinedClasses_uid = new ArrayList<>();
    ArrayList<String> joinedClasses_img = new ArrayList<>();

    ProgressDialog progressDialog;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        View v = inflater.inflate(R.layout.manageclasses, container, false);

        myClasses_listView = v.findViewById(R.id.list_myclasses);
        joinedClasses_listview = v.findViewById(R.id.list_joinedclasses);
         progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        showMyClasses();
        showJoinedClasses();




        return v;
    }

    private void showJoinedClasses() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid());


        final Adapter_classes_settings adapterMyJoinedClasses = new Adapter_classes_settings(getActivity(), joinedClasses_title, joinedClasses_uid, joinedClasses_img, joinedClasses_sub);
        joinedClasses_listview.setAdapter(adapterMyJoinedClasses);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch ((int) dataSnapshot.getChildrenCount()){
                    case 0:

                        //if none classes are joined
                        //TODO HERE
                        progressDialog.dismiss();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.frame4, new nothing_to_show());
                        ft.commit();

                        break;
                    default:

                        //if joined a class or classes

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    final String ttitle = ds.child("classname").getValue(String.class);
                                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Classroom").child(ttitle);
                                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                            progressDialog.dismiss();

                                            final String uuid = dataSnapshot.child("ownerid").getValue(String.class);
                                            final String photo = dataSnapshot.child("ownerpic").getValue(String.class);
                                            final String sub = dataSnapshot.child("ownername").getValue(String.class);


                                                    joinedClasses_title.add(ttitle);
                                                    joinedClasses_img.add(photo);
                                                    joinedClasses_uid.add(uuid);
                                                    joinedClasses_sub.add("Instructor: "+sub);
                                                    adapterMyJoinedClasses.notifyDataSetChanged();


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        joinedClasses_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = (String) parent.getItemAtPosition(position);
//                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                leaveClass(selectedItem);
            }
        });

    }

    private void leaveClass(final String nameclass) {


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Leave Class?");
        alert.setMessage("Are you sure you want to leave?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    Database_TT db = new Database_TT(DeleteTT.this);

            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Classes").child(nameclass).child("Joined").child(firebaseUser.getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            ref.removeValue();
                        }
                        else{
                            Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Query query = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid()).orderByChild("classname").equalTo(nameclass);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds:dataSnapshot.getChildren()){

                            if(ds.exists()){
                                String push = ds.child("push").getValue(String.class);

                                FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid()).child(push).removeValue();
                                Toast.makeText(getActivity(), "Class left", Toast.LENGTH_SHORT).show();




                            }
                            else{
                                Toast.makeText(getActivity(), "unknown error occured", Toast.LENGTH_SHORT).show();

                            }

                        }
                        FragmentManager fm= getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        fm.popBackStack("abc", fm.POP_BACK_STACK_INCLUSIVE);
                        ft.replace(R.id.frame3, new Manageclass());
                        ft.commit();





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();


    }

    private void showMyClasses(){
        final Query query = FirebaseDatabase.getInstance().getReference("Classroom").orderByChild("ownerid").equalTo(firebaseAuth.getUid().trim());

        final Adapter_classes_settings adapterMyclass = new Adapter_classes_settings(getActivity(), myClasses_title, myClasses_uid, myClasses_img, myClasses_sub);
        myClasses_listView.setAdapter(adapterMyclass);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                switch ((int) dataSnapshot.getChildrenCount()){

                    case 0:

                        //when there is no class
                        //TODO HERE

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.frame5, new nothing_to_show());
                        ft.commit();

                        break;
                    default:

                        //when there are class/classes present

                        //query for classname and ownername
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds:dataSnapshot.getChildren()){

                                    final String ttitle = ds.child("classroom").getValue(String.class);
                                    final String uuid = ds.child("ownerid").getValue(String.class);
                                    final String pic = ds.child("ownerpic").getValue(String.class);
//                                    final String name = ds.child("ownername").getValue(String.class);

                                            myClasses_title.add(ttitle);
                                            myClasses_img.add(pic);
                                            myClasses_uid.add(uuid);
                                            myClasses_sub.add("Instructor: You");
                                            adapterMyclass.notifyDataSetChanged();

                                }
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


        myClasses_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = (String) parent.getItemAtPosition(position);

                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.activity_class_setting, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptsView);
                final Switch sw =  promptsView .findViewById(R.id.switch_allowjoin);
                getJoinvalue(selectedItem, sw);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, final int id) {

                                setJoinValue(sw, selectedItem);


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();





            }
        });





    }
    private void getJoinvalue(String classname, final Switch sw){
        FirebaseDatabase.getInstance().getReference("Classroom/" + classname +"/")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String state =dataSnapshot.child("allowjoin").getValue(String.class);

                        if(state.equals("true")){
                            sw.setChecked(true);
                        }
                        if(state.equals("false")){
                            sw.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void setJoinValue(Switch sw, String classname){
        Boolean switchState = sw.isChecked();

        if(switchState){
            FirebaseDatabase.getInstance().getReference("Classroom/" + classname +"/")
                    .child("allowjoin").setValue("true");

            Toast.makeText(getActivity(), "Allowed", Toast.LENGTH_SHORT).show();

        }
        else{

            FirebaseDatabase.getInstance().getReference("Classroom/" + classname +"/")
                    .child("allowjoin").setValue("false");
            Toast.makeText(getActivity(), "Denied", Toast.LENGTH_SHORT).show();

        }


    }

}
