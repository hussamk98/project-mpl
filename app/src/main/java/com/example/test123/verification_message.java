package com.example.test123;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class verification_message extends Fragment implements View.OnClickListener{
    TextView t1;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.verification_message, container, false);

        t1 = v.findViewById(R.id.backToLoginBtn1);
        t1.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(t1)){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame2, new login_fclass());
            ft.commit();
        }
    }
}
