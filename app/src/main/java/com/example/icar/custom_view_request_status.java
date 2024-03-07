package com.example.icar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class custom_view_request_status extends BaseAdapter {
    String[] req_id, date, status, route_info, latitude, longitude,amount, uploaded;
    private Context context;
    SharedPreferences sh;
    String url;

    public custom_view_request_status(Context applicationContext, String[] req_id, String[] date, String[] status,
                                      String[] route_info, String[] latitude, String[] longitude,String[] amount, String[] uploaded) {
        this.context = applicationContext;
        this.req_id = req_id;
        this.date = date;
        this.status = status;
        this.route_info = route_info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.amount = amount;
        this.uploaded=uploaded;
    }


    @Override
    public int getCount() {
        return latitude.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_request_status, null);

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView50);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView52);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView54);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView57);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView64);
        Button b1 = (Button) gridView.findViewById(R.id.button17);
        Button b2 = (Button) gridView.findViewById(R.id.button18);
        Button b3 = (Button) gridView.findViewById(R.id.button21);
        Button b4 = (Button) gridView.findViewById(R.id.button22);
        b3.setEnabled(false);
        b4.setEnabled(false);
        if(status[i].equalsIgnoreCase("paid")){
            b2.setEnabled(false);
            b3.setEnabled(true);
            b4.setEnabled(true);
        }
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ik=(int)view.getTag();
                String url = "http://maps.google.com/?q=" + latitude[ik] + "," + longitude[ik];
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        b2.setTag(i);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("req_id",req_id[pos]);
                ed.putString("amount",amount[pos]);
                ed.commit();
                Intent i = new Intent(context,Payment_mode.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        b3.setTag(i);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("req_id",req_id[pos]);

                ed.commit();
                Intent i = new Intent(context,view_reply.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
        b4.setTag(i);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("req_id",req_id[pos]);
                Toast.makeText(context, ""+req_id[pos], Toast.LENGTH_SHORT).show();
                ed.commit();
                Intent i = new Intent(context,Send_feedback.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });



        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(date[i]);
        tv2.setText(status[i]);
        tv3.setText(route_info[i]);
        tv4.setText(amount[i]);
        tv5.setText(uploaded[i]);


        return gridView;
    }
}