package com.example.icar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Payment_mode extends AppCompatActivity {
    RadioGroup r1;
    RadioButton rb1,rb2;
    SharedPreferences sh;
    String url;
    Button b1;
    String mode = "online";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        r1 = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        b1 = findViewById(R.id.button19);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb2.isChecked()){
                    mode = "offline";

                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ipaddress","");
                    url = sh.getString("url","") + "android_offline_payment";

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
                                            Toast.makeText(Payment_mode.this, "payment is offline", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(),Home.class);
                                            startActivity(i);

                                        }
                                        if (jsonObj.getString("status").equalsIgnoreCase("no")) {
                                            Toast.makeText(Payment_mode.this, "Already payed", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(),Home.class);
                                            startActivity(i);

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


                            params.put("lid",sh.getString("lid",""));
                            params.put("req_id",sh.getString("req_id",""));
                            params.put("amount",sh.getString("amount",""));
//                            params.put("mode",mode);


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
                else {
                    Intent i = new Intent(getApplicationContext(),PaymentActivity.class);
                    startActivity(i);


                }
            }
        });


    }
}