package com.example.test123;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signup_fclass extends Fragment implements View.OnClickListener{
  TextView t1;
  EditText e1, e2, e3, e4, e5;
  CheckBox c1;
  Button b1;
  String fullname, email, phone, pass, cpass;
  ProgressDialog progressDialog;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference ref;
  FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signup_layout, container, false);
        t1 = v.findViewById(R.id.already_user);
        t1.setOnClickListener(this);


        progressDialog = new ProgressDialog(getContext());

        e1 = v.findViewById(R.id.fullName);
        e2 = v.findViewById(R.id.userEmailId);
        e3 = v.findViewById(R.id.mobileNumber);
        e4 = v.findViewById(R.id.password);
        e5 = v.findViewById(R.id.confirmPassword);
        c1 = v.findViewById(R.id.terms_conditions);
        b1 = v.findViewById(R.id.signUpBtn);

        b1.setOnClickListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users");
        return v;
    }




    @Override
    public void onClick(View v) {
        fullname = e1.getText().toString().trim();
        email = e2.getText().toString().trim();
        phone = e3.getText().toString().trim();
        pass = e4.getText().toString().trim();
        cpass = e5.getText().toString().trim();



        if(v.equals(t1)){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame2, new login_fclass());
            ft.commit();
        }

        if(v.equals(b1)){

            if(!c1.isChecked()){
                Toast.makeText(getContext(), "you must agree to terms and conditions", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!pass.equals(cpass)){
                Toast.makeText(getContext(), "password do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!login_signup_forgot_fclass.isEmailValid(email)){
                Toast.makeText(getContext(), "incorrect Email format", Toast.LENGTH_SHORT).show();
                return;
            }
            if(fullname.equals("") || email.equals("") || phone.equals("") || pass.equals("") || cpass.equals("")){
                Toast.makeText(getContext(),"Some fields are empty", Toast.LENGTH_SHORT).show();
                return;
            }

            else{


                //progress dialog and auth to FireBase
                progressDialog.setMessage("Creating account\nPlease wait...");
                progressDialog.show();


                login_signup_forgot_fclass.firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){

                                   login_signup_forgot_fclass.firebaseAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getContext(), "registered successfully, Please check your email for verification.", Toast.LENGTH_SHORT).show();

                                                        firebaseUser = login_signup_forgot_fclass.firebaseAuth.getCurrentUser();
                                                        Map<String, String> user = new HashMap<>();
                                                        user.put("name", fullname);
                                                        user.put("email", email);
                                                        user.put("phone", phone);
                                                        user.put("uid", firebaseUser.getUid());
                                                        user.put("sec", "Section/Class Name");
                                                        user.put("profilepic", "https://firebasestorage.googleapis.com/v0/b/mpl-chatapp.appspot.com/o/Profilepics%2Fdefault.png?alt=media&token=d46ebb9a-4a63-4538-a44b-70e921b887da");
                                                        ref.child(firebaseUser.getUid()).setValue(user);

                                                        FragmentManager fm= getFragmentManager();
                                                        FragmentTransaction ft = fm.beginTransaction();
                                                        ft.add(R.id.frame2, new verification_message()).addToBackStack(null);
                                                        ft.commit();

                                                    }else {
                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                                else{
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }

        }

    }
}
