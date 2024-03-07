package com.example.icar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.icar.databinding.ActivityHomePageBinding;
import com.squareup.picasso.Picasso;

public class Home_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView t1,t2;
    ImageView img;
    SharedPreferences sh;
    CardView c_profile, c_route, c_other_route, c_req_status, c_logout;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomePage.toolbar);
//        binding.appBarHomePage.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        t1 = headerView.findViewById(R.id.textView2);
        t2 = headerView.findViewById(R.id.textView);
        img = headerView.findViewById(R.id.imageView);

        c_profile=(CardView)findViewById(R.id.c1);
        c_route=(CardView)findViewById(R.id.c2);
        c_other_route=(CardView)findViewById(R.id.c3);
        c_req_status=(CardView)findViewById(R.id.c4);
        c_logout=(CardView)findViewById(R.id.c5);
        c_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),view_profile.class);
                startActivity(i);
            }
        });

        c_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),view_route.class);
                startActivity(i);
            }
        });

        c_other_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),view_uploaded_route.class);
                startActivity(i);
            }
        });

        c_req_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),view_request_status.class);
                startActivity(i);
            }
        });

        c_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ed = sh.edit();
                ed.commit();
                ed.clear();
                Intent i = new Intent(getApplicationContext(),ip_page.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ipaddress = sh.getString("ipaddress", "");

        String url = "http://" + ipaddress + ":8000" + sh.getString("photo","");    // For Image
//        Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(img);//circle

        t1.setText(sh.getString("name",""));
        t2.setText(sh.getString("email",""));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_home){
            Intent i = new Intent(getApplicationContext(),view_profile.class);
            startActivity(i);
        }
        if(id==R.id.nav_gallery){
            Intent i = new Intent(getApplicationContext(),Send_feedback.class);
            startActivity(i);
        }
        if(id==R.id.nav_slideshow){
            Intent i = new Intent(getApplicationContext(),view_route.class);
            startActivity(i);
        }
//        if(id==R.id.other_route){
//            Intent i = new Intent(getApplicationContext(),view_uploaded_route.class);
//            startActivity(i);
//        }
//        if(id==R.id.Request_status){
//            Intent i = new Intent(getApplicationContext(),view_request_status.class);
//            startActivity(i);
//        }
        if(id==R.id.logout){
            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ed = sh.edit();
            ed.commit();
            ed.clear();
            Intent i = new Intent(getApplicationContext(),ip_page.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}