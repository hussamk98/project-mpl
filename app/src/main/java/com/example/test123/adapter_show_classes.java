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


public class adapter_show_classes extends ArrayAdapter<String> {


    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> desc;
    private final ArrayList<String> img;

    public adapter_show_classes(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<String> desc, ArrayList<String> img) {
        super(context, R.layout.activity_show_classes_row, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.desc=desc;
        this.img=img;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_show_classes_row, null,true);


        TextView myTitle = rowView.findViewById(R.id.textView1);
        TextView myDescription = rowView.findViewById(R.id.textView2);
        TextView myDesc = rowView.findViewById(R.id.textView3);
        CircleImageView myimg = rowView.findViewById(R.id.img_showclassesrow);

        myTitle.setText(maintitle.get(position));
        myDescription.setText(subtitle.get(position));
        myDesc.setText(desc.get(position));
        Picasso.get().load(img.get(position)).into(myimg);


        return rowView;

    }
}

