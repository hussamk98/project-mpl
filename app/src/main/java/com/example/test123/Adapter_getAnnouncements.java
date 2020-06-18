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

public class Adapter_getAnnouncements extends ArrayAdapter<String> {


    private final Activity context;
    private final ArrayList<String> teachername;
    private final ArrayList<String> classname;
    private final ArrayList<String> title;
    private final ArrayList<String> desc;
    private final ArrayList<String> img;

    public Adapter_getAnnouncements(Activity context, ArrayList<String> teachername, ArrayList<String> classname, ArrayList<String> title, ArrayList<String> desc, ArrayList<String> img) {
        super(context, R.layout.get_accouncements_row, classname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.teachername=teachername;
        this.classname=classname;
        this.desc=desc;
        this.title=title;
        this.img=img;

    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.get_accouncements_row, null,true);


        TextView mTeacher = rowView.findViewById(R.id.t_getannounce_tname);
        TextView mClass = rowView.findViewById(R.id.t_getannounce_cname);
        TextView mTitle = rowView.findViewById(R.id.t_getannounce_title);
        TextView mDesc = rowView.findViewById(R.id.t_getannounce_desc);
        CircleImageView myimg = rowView.findViewById(R.id.getannounce_pic);

        mTeacher.setText(teachername.get(position));
        mClass.setText(classname.get(position));
        mTitle.setText(title.get(position));
        mDesc.setText(desc.get(position));
        Picasso.get().load(img.get(position)).into(myimg);


        return rowView;

    }
}

