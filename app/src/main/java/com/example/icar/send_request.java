package com.example.icar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class send_request extends AppCompatActivity {
    TextView e1;
    SharedPreferences sh;
    Button b1;
    String url;
    EditText e2,e3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);
        e1 = findViewById(R.id.textView48);
        e2 = findViewById(R.id.editTextTextPersonName3);
        e3 = findViewById(R.id.editTextTextPersonName18);
        b1 = findViewById(R.id.button16);
        e2.setText(gpstracker.lati);
        e3.setText(gpstracker.longi);
        e2.setEnabled(false);
        e3.setEnabled(false);
        e1.setOnClickListener(new View.OnClickListener() {              // Date picking

            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int  mDay = c.get(Calendar.DAY_OF_MONTH);

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(send_request.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int a=monthOfYear+1;

                                String mm=String.valueOf(a);
                                String mn="0"+mm;

                                if (mm.length()==1){

                                    e1.setText(year + "-" + (mn) + "-" + dayOfMonth);
                                }
                                else {

                                    e1.setText(year + "-" + (mm) + "-" + dayOfMonth);
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = e1.getText().toString();

                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ipaddress","");
                url = sh.getString("url","") + "android_send_request";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(getApplicationContext(), "Request Added", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), view_uploaded_route.class);
                                        startActivity(i);


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Request Already added", Toast.LENGTH_LONG).show();
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

                    //                value Passing android to python
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();


                        params.put("lid", sh.getString("lid", ""));//passing to python
                        params.put("route_id", sh.getString("route_id", ""));//passing to python
                        params.put("date", date);
                        params.put("latitude",gpstracker.lati);
                        params.put("longitude",gpstracker.longi);

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

        });
    }


}
