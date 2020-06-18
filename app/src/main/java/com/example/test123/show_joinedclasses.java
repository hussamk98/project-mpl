package com.example.test123;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class show_joinedclasses extends AppCompatActivity {

    ListView listView;
    TextView t1;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> teacher = new ArrayList<>();
    ArrayList<String> std_count = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_joinedclasses);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching Data");
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        t1 =findViewById(R.id.textview_showjoinedclasses);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, login_signup_forgot_fclass.class));
        }



        listView = findViewById(R.id.listview_showjoinedclasses);
        final adapter_show_classes arrayAdapter = new adapter_show_classes(this, title, teacher, std_count, img );
        listView.setAdapter(arrayAdapter);
        final Query query = FirebaseDatabase.getInstance().getReference("joinedclasses/" + firebaseUser.getUid()).orderByChild("uid").equalTo(firebaseUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch ((int) dataSnapshot.getChildrenCount()){
                    case 0:
                        t1.setText("NO CLASSES JOINED");
                        progressDialog.dismiss();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.frame7, new nothing_to_show());
                        ft.commit();
//                        Toast.makeText(getApplicationContext(), "nothing to show", Toast.LENGTH_SHORT).show();
                        break;
                     default:



                         t1.setText("Joined Classes");

                         query.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                 for(DataSnapshot ds:dataSnapshot.getChildren()){
                                     final String ttitle = ds.child("classname").getValue(String.class);
                                     Query query1 = FirebaseDatabase.getInstance().getReference("Classroom/" + ttitle )
                                             .orderByChild("ownerid");
                                     query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             final String name = dataSnapshot.child("ownername").getValue(String.class);
                                             final String photo = dataSnapshot.child("ownerpic").getValue(String.class);

                                             Query query2 = FirebaseDatabase.getInstance().getReference("Classes/" + ttitle + "/Joined")
                                                     .orderByChild("uid");
                                             query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                     final int enrolled = (int) dataSnapshot.getChildrenCount();

                                                     teacher.add("instructor: " + name);
                                                     std_count.add("enrolled: " +enrolled);
                                                     title.add(ttitle);
                                                     img.add(photo);
                                                     arrayAdapter.notifyDataSetChanged();

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




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                final String selectedItem = (String) parent.getItemAtPosition(position);

//
//                Intent intent = new Intent(getApplicationContext(), studentsinClass.class);
//                intent.putExtra("classname", selectedItem);
//                startActivity(intent);


                AlertDialog.Builder alert = new AlertDialog.Builder(show_joinedclasses.this);
                alert.setTitle("Start chat with the instructor?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(getApplicationContext(), chats.class);
                        intent.putExtra("classroom", selectedItem);
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
