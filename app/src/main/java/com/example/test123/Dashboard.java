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
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class Dashboard extends Fragment implements View.OnClickListener {


    CardView c1, c2, c3, c4, c5, c6, c7;
    FragmentManager fm;
    FragmentTransaction ft;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertDialogBuildernew;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        MainActivity.toolbar.setTitle("Dashboard");
        View v =   inflater.inflate(R.layout.dashboard, container, false);





         c1 = v.findViewById(R.id.myclasses);
         c2 = v.findViewById(R.id.joinedclasses);
         c3 = v.findViewById(R.id.newclass);
         c4 = v.findViewById(R.id.joinclass);
         c5 = v.findViewById(R.id.card_settings);
         c6 = v.findViewById(R.id.card_logout);
         c7 = v.findViewById(R.id.card_announcements);

         c1.setOnClickListener(this);
         c2.setOnClickListener(this);
         c3.setOnClickListener(this);
         c4.setOnClickListener(this);
         c5.setOnClickListener(this);
         c6.setOnClickListener(this);
         c7.setOnClickListener(this);
         progressDialog = new ProgressDialog(getContext());
         alertDialogBuildernew =  new AlertDialog.Builder(getContext());
         alertDialogBuildernew.setCancelable(false).setNegativeButton("Dismiss",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return v;

    }

    @Override
    public void onClick(View v) {
        if(v.equals(c1)){
            startActivity(new Intent(getContext(), show_classes.class));
        }
        if(v.equals(c2)){
            startActivity(new Intent(getContext(), show_joinedclasses.class));
        }
        if(v.equals(c3)){


            LayoutInflater lii = LayoutInflater.from(getContext());
            View promptsVieww = lii.inflate(R.layout.newclassroom, null);
            final AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(getContext());
            // set newclassroom.xml to alertdialog builder
            alertDialogBuilderr.setView(promptsVieww);
            final EditText userInputt =  promptsVieww .findViewById(R.id.et_classid);
            userInputt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            // set dialog message
            alertDialogBuilderr
                    .setCancelable(false)
                    .setPositiveButton("Create",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    //code for new classroom creation starts here
                                    {
                                        progressDialog.setTitle("Creating new Class");
                                        progressDialog.setMessage("Please wait...");
                                        progressDialog.show();

                                        firebaseAuth = FirebaseAuth.getInstance();
                                        firebaseUser = firebaseAuth.getCurrentUser();

                                        ref = FirebaseDatabase.getInstance().getReference("Classroom/");

                                        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String name = dataSnapshot.child("name").getValue(String.class);
                                                String photo = dataSnapshot.child("profilepic").getValue(String.class);

//                                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
//                                                        name = ds.child("name").getValue(String.class);
//                                                        photo = ds.child("profilepic").getValue(String.class);
//                                                    }

                                                final Map<String, String> user = new HashMap<>();
                                                user.put("classroom", userInputt.getText().toString().trim());
                                                user.put("ownerid", firebaseUser.getUid());
                                                user.put("owneremail", firebaseUser.getEmail());
                                                user.put("ownername", name);
                                                user.put("ownerpic", photo);
                                                user.put("allowjoin", "true");


                                                Query query = FirebaseDatabase.getInstance().getReference("Classroom/")
                                                        .orderByChild("classroom").equalTo(userInputt.getText().toString().trim());



                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
                                                        switch (((int) dataSnapshot.getChildrenCount())) {
                                                            case 1:
                                                                progressDialog.dismiss();
                                                                alertDialogBuildernew.setTitle("error, name already taken!");
                                                                AlertDialog alertDialog = alertDialogBuildernew.create();
                                                                alertDialog.show();


//                                                                Toast.makeText(getActivity(), "error, name already taken!", Toast.LENGTH_SHORT).show();
                                                                break;
                                                            case 0:
                                                                progressDialog.dismiss();
                                                                alertDialogBuildernew.setTitle("class "+userInputt.getText().toString().trim()+" created successfully");
                                                                AlertDialog alertDialog1 = alertDialogBuildernew.create();
                                                                alertDialog1.show();

//                                                                Toast.makeText(getActivity(), "class: '" + userInputt.getText().toString().trim() + "' created successfully :)", Toast.LENGTH_LONG).show();
                                                                ref.child(userInputt.getText().toString().trim()).setValue(user);
                                                                break;

                                                            default:
                                                                progressDialog.dismiss();
                                                                alertDialogBuildernew.setTitle("error");
                                                                AlertDialog alertDialog2 = alertDialogBuildernew.create();
                                                                alertDialog2.show();

//                                                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                                                break;
                                                        }
                                                        return;
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
//                                            query1.addListenerForSingleValueEvent(new ValueEventListener() );
//                                        ref.addListenerForSingleValueEvent(valueEventListener);




                                    }
                                    //code for new classroom ends here


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialogg = alertDialogBuilderr.create();

            // show it
            alertDialogg.show();


        }
        if(v.equals(c4)){
            LayoutInflater li = LayoutInflater.from(getContext());
            View promptsView = li.inflate(R.layout.newclassroom, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setView(promptsView);
            final EditText userInput =  promptsView .findViewById(R.id.et_classid);
            userInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Join",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, final int id) {



                                    //code for Joining classroom starts here
                                    progressDialog.setTitle("Joining Class");
                                    progressDialog.setMessage("Please wait...");
                                    progressDialog.show();

                                    firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    Query query = FirebaseDatabase.getInstance().getReference("Classroom/")
                                            .orderByChild("classroom").equalTo(userInput.getText().toString().trim());


                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
                                            switch (((int) dataSnapshot.getChildrenCount())) {


                                                case 1:
                                                    //if class exists
                                                    ref = FirebaseDatabase.getInstance().getReference("Classroom/" + userInput.getText().toString().trim() + "/");
                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Classroom classroom = dataSnapshot.getValue(Classroom.class);

                                                            //if already joined(admin)
                                                            if(classroom.getOwnerid().equals(firebaseUser.getUid())){
                                                                progressDialog.dismiss();
                                                                alertDialogBuildernew.setTitle("Your'e the admin");
                                                                AlertDialog alertDialog = alertDialogBuildernew.create();
                                                                alertDialog.show();
//                                                                Toast.makeText(getActivity(), "Your'e the admin", Toast.LENGTH_LONG).show();
                                                                return;
                                                            }
                                                            //if not joined
                                                            else if(!classroom.getOwnerid().equals(firebaseUser.getUid())){

                                                                if(classroom.getAllowjoin().equals("true")){
                                                                    Query query1 = FirebaseDatabase.getInstance().getReference("Users/").orderByChild("uid").equalTo(firebaseUser.getUid());
                                                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                            String name = null;
                                                                            String picprofile =null;
                                                                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                                                name = ds.child("name").getValue(String.class);
                                                                                picprofile = ds.child("profilepic").getValue(String.class);
                                                                            }


                                                                            Query query2 = FirebaseDatabase.getInstance().getReference("Classes/" + userInput.getText().toString().trim() + "/Joined/").orderByChild("uid").equalTo(firebaseUser.getUid());
                                                                            final String finalName = name;
                                                                            final String finalPicprofile = picprofile;
                                                                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String joined = "false";
                                                                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                                                        joined = ds.child("join").getValue(String.class);
                                                                                    }

                                                                                    if(joined.equals("true")){
                                                                                        progressDialog.dismiss();
                                                                                        alertDialogBuildernew.setTitle("Already joined :)");
                                                                                        AlertDialog alertDialog = alertDialogBuildernew.create();
                                                                                        alertDialog.show();

//                                                                                        Toast.makeText(getActivity(), "Already joined :)", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    else if(joined.equals("false")) {
                                                                                        /*  insert into  Classes/"classname"/Joined/"userID"   */
                                                                                        Map<String, String> user = new HashMap<>();
                                                                                        user.put("uid", firebaseUser.getUid());
                                                                                        user.put("name", finalName);
                                                                                        user.put("profilepic", finalPicprofile);
                                                                                        user.put("email", firebaseUser.getEmail());
                                                                                        user.put("join", "true");
                                                                                        FirebaseDatabase.getInstance().getReference("Classes/"
                                                                                                + userInput.getText().toString().trim() + "/Joined/"
                                                                                                + firebaseUser.getUid()).setValue(user);


                                                                                        /*  insert into joinedclasses/"userID"/push()     */
                                                                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("joinedclasses").child(firebaseUser.getUid());
                                                                                        String push = ref1.push().getKey().trim();
                                                                                        Map<String, String> map = new HashMap<>();
                                                                                        map.put("classname", userInput.getText().toString().trim());
                                                                                        map.put("uid", firebaseUser.getUid());
                                                                                        map.put("push", push);
                                                                                        ref1.child(push).setValue(map);

                                                                                        progressDialog.dismiss();
                                                                                        alertDialogBuildernew.setTitle("Class join Success :)");
                                                                                        AlertDialog alertDialog1 = alertDialogBuildernew.create();
                                                                                        alertDialog1.show();
//                                                                                        Toast.makeText(getActivity(), "Class join Success :)", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });


                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                                else if(classroom.getAllowjoin().equals("false")){
                                                                    progressDialog.dismiss();
                                                                    alertDialogBuildernew.setTitle("Join permission denied");
                                                                    AlertDialog alertDialog2 = alertDialogBuildernew.create();
                                                                    alertDialog2.show();
//                                                                    Toast.makeText(getActivity(), "Join permission denied", Toast.LENGTH_SHORT).show();
                                                                }

                                                                return;
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    break;


                                                case 0:
                                                    progressDialog.dismiss();
                                                    alertDialogBuildernew.setTitle("No Classes Found :(");
                                                    AlertDialog alertDialog = alertDialogBuildernew.create();
                                                    alertDialog.show();
//                                                    Toast.makeText(getActivity(), "No Classes Found :(", Toast.LENGTH_LONG).show();
                                                    break;

                                                default:
                                                    progressDialog.dismiss();
                                                    alertDialogBuildernew.setTitle("unknown error");
                                                    AlertDialog alertDialog1 = alertDialogBuildernew.create();
                                                    alertDialog1.show();
//                                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                            return;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//
                                    //code for new classroom ends here


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
        if(v.equals(c5)){
            startActivity(new Intent(getActivity(), Settings.class));

        }
        if(v.equals(c6)){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            /*
            *code copied from
            *https://wajahatkarim.com/2018/04/closing-all-activities-and-launching-any-specific-activity/
            * clears stack of activities
            ----------------------------------------------------------------------------------------*/
            Intent i = new Intent(getContext(), login_signup_forgot_fclass.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("EXIT", true);
            startActivity(i);
            getActivity().finish();
            /*---------------------------------------------------------------------------------------*/
        }
        if(v.equals(c7)){
            startActivity(new Intent(getActivity(), getAnnouncements.class));
        }
    }




    /** Listview Within a Scrollview **/
//    public static void getListViewSize(ListView myListView) {
//        ListAdapter myListAdapter = myListView.getAdapter();
//        if (myListAdapter == null) {
//            //do nothing return null
//            return;
//        }
//        //set listAdapter in loop for getting final size
//        int totalHeight = 0;
//        for (int size = 0; size <= myListAdapter.getCount()-4; size++) {
//            View listItem = myListAdapter.getView(size, null, myListView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        //setting listview item in adapter
//        ViewGroup.LayoutParams params = myListView.getLayoutParams();
//        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
//        myListView.setLayoutParams(params);
//        // print height of adapter on log
//    }

}
