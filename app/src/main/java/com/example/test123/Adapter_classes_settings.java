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

public class Adapter_classes_settings extends ArrayAdapter<String> {


    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> uid;
    private final ArrayList<String> img;
    private final ArrayList<String> sub;

    public Adapter_classes_settings (Activity context, ArrayList<String> maintitle, ArrayList<String> uid , ArrayList<String> img, ArrayList<String> sub) {
        super(context, R.layout.manageclasses_row, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.uid=uid;
        this.sub=sub;
        this.img=img;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.manageclasses_row, null,true);


        TextView myTitle = rowView.findViewById(R.id.t_manageclasses_row);
        TextView mySub = rowView.findViewById(R.id.t_manageclasses_row_sub);

        CircleImageView myimg = rowView.findViewById(R.id.img_manageclasses_row);

        myTitle.setText(maintitle.get(position));
        mySub.setText(sub.get(position));
        Picasso.get().load(img.get(position)).into(myimg);


        return rowView;

    }
}
