package com.example.test123;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class login_signup_forgot_fclass extends AppCompatActivity {

    public static FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_forgot_fragment);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseUser != null && firebaseUser.isEmailVerified()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame2, new login_fclass());
        ft.commit();

    }
    public void show_hide(View v){
        EditText e1 = findViewById(R.id.login_password);
        boolean t = ((CheckBox)v).isChecked();
        if(v.getId() == R.id.show_hide_password && t){

            e1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }else if(v.getId() == R.id.show_hide_password  && t!=Boolean.TRUE){

            e1.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    public void show_pass(View v){
        EditText e1, e2;
        e1 = findViewById(R.id.password);
        e2 = findViewById(R.id.confirmPassword);

        boolean t = ((CheckBox)v).isChecked();
        if(v.getId() == R.id.show_hide_password && t){

            e1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            e2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        }else if(v.getId() == R.id.show_hide_password  && t!=Boolean.TRUE){

            e1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            e2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
