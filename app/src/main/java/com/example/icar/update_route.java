package com.example.icar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
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
import org.w3c.dom.DOMImplementation;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class update_route extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7;
    Button b1;
    SharedPreferences sh;
    String url;
    private TimePicker timePicker1;
    private String format = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_route);
        e1 = findViewById(R.id.editTextTextPersonName9);
        e2 = findViewById(R.id.editTextTextPersonName12);
        e3 = findViewById(R.id.editTextTextPersonName13);
        e4 = findViewById(R.id.editTextTextPersonName14);
        e5 = findViewById(R.id.editTextTextPersonName15);
        e6 = findViewById(R.id.editTextTextPersonName16);
        e7 = findViewById(R.id.editTextTextPersonName21);
        b1 = findViewById(R.id.button7);
        e1.setText(gpstracker.lati);
        e2.setText(gpstracker.longi);
        e1.setEnabled(false);
        e2.setEnabled(false);


        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress","");
        url = sh.getString("url","") + "android_update_route";

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
                                e1.setText(jsonObj.getString("latitude"));
                                e2.setText(jsonObj.getString("longitude"));
                                e3.setText(jsonObj.getString("from"));
                                e4.setText(jsonObj.getString("to"));
                                e5.setText(jsonObj.getString("date"));
                                e6.setText(jsonObj.getString("no_of_requests"));
                                e7.setText(jsonObj.getString("time"));

                            }


                            // }
                            else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
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


                params.put("rid",sh.getString("rid",""));


                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

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

                if(flag==0) {
                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ipaddress", "");
                    url = sh.getString("url", "") + "android_update_routes";

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
                                            Toast.makeText(update_route.this, "Route Updated", Toast.LENGTH_SHORT).show();
                                            finish();

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

                            params.put("rid", sh.getString("rid", ""));
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
        e5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int  mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(update_route.this,
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



    }

}