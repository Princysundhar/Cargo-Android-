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

import java.util.prefs.Preferences;

public class custom_view_uploaded_route extends BaseAdapter {
    String[] route_id, latitude, longitude, From, To, date, no_of_request, uploaded;
    private Context context;


    public custom_view_uploaded_route(Context applicationContext, String[] route_id, String[] latitude,
            String[] longitude, String[] From, String[] To, String[] date, String[] no_of_request, String[] uploaded) {
        this.context = applicationContext;
        this.route_id = route_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.From = From;
        this.To = To;
        this.date = date;
        this.no_of_request = no_of_request;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_uploaded_route, null);

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView41);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView43);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView45);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView47);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView62);
        Button b1 = (Button) gridView.findViewById(R.id.button14);
        Button b2 = (Button) gridView.findViewById(R.id.button15);
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
                int pos = (int)view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("route_id",route_id[pos]);
                ed.commit();
                Intent i = new Intent(context,send_request.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });



        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(From[i]);
        tv2.setText(To[i]);
        tv3.setText(date[i]);
        tv4.setText(no_of_request[i]);
        tv5.setText(uploaded[i]);




        return gridView;
    }
}