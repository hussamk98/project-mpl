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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login_fclass extends Fragment implements View.OnClickListener{
    TextView t1, t2;
    Button b1;
    EditText e1, e2;
    String email, pass;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.login_layout, container, false);
        t1 = v.findViewById(R.id.createAccount);
        t2 = v.findViewById(R.id.forgot_password);

        e1 = v.findViewById(R.id.login_emailid);
        e2 = v.findViewById(R.id.login_password);
        b1 = v.findViewById(R.id.loginBtn);
        b1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t1.setOnClickListener(this);


        progressDialog = new ProgressDialog(getContext());

        return v;
    }

    @Override
    public void onClick(View v) {
        email = e1.getText().toString().trim();
        pass = e2.getText().toString().trim();

        if(v.equals(t1)){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame2, new signup_fclass());
            ft.commit();
        }
        else if(v.equals(t2)){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame2, new forgot_fclass()).addToBackStack(null);
            ft.commit();
        }

        else if(v.equals(b1)){
//            getActivity().finish();
//            startActivity(new Intent(getActivity(), MainActivity.class));

            if(!login_signup_forgot_fclass.isEmailValid(email)){
                Toast.makeText(getContext(), "incorrect email format", Toast.LENGTH_SHORT).show();
            }
            if(email.equals("") || pass.equals("")){
                Toast.makeText(getContext(), "Some fields are missing", Toast.LENGTH_SHORT).show();
            }

            else{
                progressDialog.setMessage("Logging in\nPlease Wait...");
                progressDialog.show();

                login_signup_forgot_fclass.firebaseAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    if(login_signup_forgot_fclass.firebaseAuth.getCurrentUser().isEmailVerified()){
                                        Toast.makeText(getContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    }else{

                                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                        alert.setTitle("Resend verification email?");
                                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    Database_TT db = new Database_TT(DeleteTT.this);

                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                login_signup_forgot_fclass.firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FragmentManager fm= getFragmentManager();
                                                            FragmentTransaction ft = fm.beginTransaction();
                                                            ft.add(R.id.frame2, new verification_message());
                                                            ft.commit();
                                                        }else {
                                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
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
