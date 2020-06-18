package com.example.test123;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_enr_std extends ArrayAdapter<String> {


    private final Activity context;
    private final ArrayList<String> title;
    private final ArrayList<String> img;
    private final ArrayList<String> userid;

    public Adapter_enr_std(Activity context, ArrayList<String> title, ArrayList<String> img, ArrayList<String> userid) {
        super(context, R.layout.enrolled_std_row, userid);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.title = title;
        this.img = img;
        this.userid = userid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.enrolled_std_row, null, true);


        TextView myTitle = rowView.findViewById(R.id.enr_std_title);
        CircleImageView myimg = rowView.findViewById(R.id.enr_std_img);


        myTitle.setText(title.get(position));
        Picasso.get().load(img.get(position)).into(myimg);


        return rowView;

    }
}