package com.example.icar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_route extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7;
    Button b1;
    SharedPreferences sh;
    String url;
    private TimePicker timePicker1;
    private String format = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        e1 = findViewById(R.id.editTextTextPersonName5);
        e2 = findViewById(R.id.editTextTextPersonName6);
        e3 = findViewById(R.id.editTextTextPersonName7);
        e4 = findViewById(R.id.editTextTextPersonName8);
        e5 = findViewById(R.id.editTextTextPersonName10);
        e6 = findViewById(R.id.editTextTextPersonName11);
        e7 = findViewById(R.id.editTextTextPersonName20);
        b1 = findViewById(R.id.button8);
        e1.setText(gpstracker.lati);
        e2.setText(gpstracker.longi);
        e1.setEnabled(false);
        e2.setEnabled(false);
        e7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                showTime(hour, min);
            }
            public void setTime(View view) {
                int hour = timePicker1.getCurrentHour();
                int min = timePicker1.getCurrentMinute();
                showTime(hour, min);
            }

            public void showTime(int hour, int min) {
                if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                e7.setText(new StringBuilder().append(hour).append(" : ").append(min)
                        .append(" ").append(format));
            }
        });

        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int  mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(add_route.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int a=monthOfYear+1;

                                String mm=String.valueOf(a);
                                String mn="0"+mm;

                                if (mm.length()==1){

                                    e5.setText(year + "-" + (mn) + "-" + dayOfMonth);
                                }
                                else {

                                    e5.setText(year + "-" + (mn) + "-" + dayOfMonth);
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = e3.getText().toString();
                String to = e4.getText().toString();
                String date = e5.getText().toString();
                String no_of_requests = e6.getText().toString();
                String time = e7.getText().toString();
                int flag=0;
                if(from.equalsIgnoreCase("")){
                    e3.setError("null");
                    flag++;
                }
                if(to.equalsIgnoreCase("")){
                    e4.setError("null");
                    flag++;
                }
                if(date.equalsIgnoreCase("")){
                    e5.setError("null");
                    flag++;
                }
                if(no_of_requests.equalsIgnoreCase("")){
                    e6.setError("null");
                    flag++;
                }
                if(flag==0) {


                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ipaddress", "");
                    url = sh.getString("url", "") + "android_add_route";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    // response
                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                            Toast.makeText(add_route.this, "Route added", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), view_route.class);
                                            startActivity(i);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("lid", sh.getString("lid", ""));
                            params.put("latitude", gpstracker.lati);
                            params.put("longitude", gpstracker.longi);
                            params.put("from", from);
                            params.put("to", to);
                            params.put("no_of_requests", no_of_requests);
                            params.put("date", date);
                            params.put("time", time);


                            return params;
                        }
                    };

                    int MY_SOCKET_TIMEOUT_MS = 100000;

                    postRequest.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(postRequest);
                }

            }
        });
    }
}