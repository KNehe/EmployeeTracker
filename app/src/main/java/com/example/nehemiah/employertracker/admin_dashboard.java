package com.example.nehemiah.employertracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nehemiah.employertracker.Admin.AsignTask;
import com.example.nehemiah.employertracker.Admin.ViewEmployeeLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firestore.v1.TargetOrBuilder;

public class admin_dashboard extends AppCompatActivity {
  private static CardView addEmployee,assignTask,employeesLocation,profile;

  private Toolbar toolbar;
  private DrawerLayout drawerLayout;
  private NavigationView navigationView;

  private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation_drawer_icon);

        auth = FirebaseAuth.getInstance();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.logout:
                       // menuItem.setChecked(true);
                        logOut();
                        drawerLayout.closeDrawers();
                        return  true;
                }
                return false;
            }
        });

        addEmployee = findViewById(R.id.add_employer_section);
        employeesLocation = findViewById(R.id.view_employee_location_section);
        profile = findViewById(R.id.employer_profile_section);
        assignTask = findViewById(R.id.add_task_section);



        //listener on addEmployee card
        //fires intent to Admin_Add_employer section to add employees
        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this,Admin_add_employee.class));
            }
        });


        //listener on assignTask card
        //fires intent to assign task
        assignTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this,AsignTask.class));
            }
        });

        //listener on employeesLocation card
        employeesLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_dashboard.this, ViewEmployeeLocation.class));
            }
        });

    }//onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //log out user
    public void logOut()
    {
        auth.signOut();
        startActivity(new Intent(admin_dashboard.this,LoginScreen.class));
    }


 


}
