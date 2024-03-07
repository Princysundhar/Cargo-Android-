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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_view_route extends BaseAdapter {
    String[]rid,latitude,longitude,from,to,userinfo,date,no_of_requests,time;
    private Context context;
    SharedPreferences sh;
    String url;


    public custom_view_route(Context applicationContext, String[] rid, String[] latitude, String[] longitude, String[] from, String[] to, String[] userinfo,String []date,String[]no_of_requests,String[]time) {
        this.context = applicationContext;
        this.rid = rid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.from = from;
        this.to = to;
        this.userinfo = userinfo;
        this.date = date;
        this.no_of_requests = no_of_requests;
        this.time = time;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_route, null);

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView12);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView14);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView16);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView18);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView20);
        TextView tv6 = (TextView) gridView.findViewById(R.id.textView66);
        Button b1 = (Button) gridView.findViewById(R.id.button3);
        Button b2 = (Button) gridView.findViewById(R.id.button4);
        Button b3 = (Button) gridView.findViewById(R.id.button6);
        Button b4 = (Button) gridView.findViewById(R.id.button12);
        b4.setTag(i);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("req_id",rid[pos]);
                ed.commit();
                Intent i = new Intent(context,view_user_request.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
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
        b2.setOnClickListener(new View.OnClickListener() {              // UPDATE
            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("rid",rid[pos]);
//                Toast.makeText(context, ""+rid, Toast.LENGTH_SHORT).show();
                ed.commit();
                Intent i = new Intent(context,update_route.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        b3.setTag(i);
        b3.setOnClickListener(new View.OnClickListener() {              //DELETE
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();

                sh = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                sh.getString("ipaddress","");
                url = sh.getString("url","") + "android_delete_route";

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                // response
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(context, "Route Deleted", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context,view_route.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                    }


                                    // }
                                    else {
                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                                    }

                                }    catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();

//                        String id=sh.getString("uid","");
                        params.put("rid",rid[pos]);
//                params.put("mac",maclis);

                        return params;
                    }
                };

                int MY_SOCKET_TIMEOUT_MS=100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);

            }
        });
//

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);


        tv1.setText(from[i]);
        tv2.setText(to[i]);
        tv3.setText(userinfo[i]);
        tv4.setText(date[i]);
        tv5.setText(no_of_requests[i]);
        tv6.setText(time[i]);


        return gridView;
    }
}