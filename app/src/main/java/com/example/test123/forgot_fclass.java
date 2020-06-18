package com.example.test123;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class forgot_fclass extends Fragment implements View.OnClickListener{
    TextView t1, t2;
    EditText e1;
    String email;

    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.forgotpass_layout, container, false);
        t1 = v.findViewById(R.id.backToLoginBtn);
        e1 = v.findViewById(R.id.forgot_email);
        t2 = v.findViewById(R.id.forgot_button);
        progressDialog = new ProgressDialog(getContext());
        t2.setOnClickListener(this);
        t1.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        email = e1.getText().toString().trim();

        if(v.equals(t1)){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame2, new login_fclass()).addToBackStack(null);
            ft.commit();
        }
        if(v.equals(t2)){
             if(email.equals("")){
                 Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_SHORT).show();
             }
             else if(!login_signup_forgot_fclass.isEmailValid(email)){
                 Toast.makeText(getContext(), "Incorrect email syntax", Toast.LENGTH_SHORT).show();
             }
             else {


                progressDialog.setMessage("Resetting password...");
                progressDialog.show();
                login_signup_forgot_fclass.firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.replace(R.id.frame2, new resetmessage()).addToBackStack(null);
                                    ft.commit();
                                } else {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

    }
}
