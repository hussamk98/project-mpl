package com.example.test123;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardListview extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] ttitle;
    private final String[] date;
    private final String[] descrip;
    private final Integer[] imgid;

    public DashboardListview(Activity context, String[] ttitle, String[] date, Integer[] imgid, String[] descrip) {
        super(context, R.layout.dashboard_list, ttitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ttitle=ttitle;
        this.date=date;
        this.imgid=imgid;
        this.descrip=descrip;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.dashboard_list, null,true);

        TextView title = rowView.findViewById(R.id.dashboard_teacher);
        ImageView imageView =  rowView.findViewById(R.id.dashboard_icon);
        TextView ddate =  rowView.findViewById(R.id.dashboard_date);
        TextView ddescrip =  rowView.findViewById(R.id.dashboard_description);

        title.setText(ttitle[position]);
        imageView.setImageResource(imgid[position]);
        ddate.setText(date[position]);
        ddescrip.setText(descrip[position]);


        return rowView;

    }
}
