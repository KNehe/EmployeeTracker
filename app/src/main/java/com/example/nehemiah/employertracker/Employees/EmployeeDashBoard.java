package com.example.nehemiah.employertracker.Employees;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nehemiah.employertracker.LoginScreen;
import com.example.nehemiah.employertracker.Modals.ShowTaskAdapterModal;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EmployeeDashBoard extends AppCompatActivity {

    //fireStore stuff
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference taskReference = database.collection("Tasks");
    private ShowTaskAdapter adapter;

    //firebase
    FirebaseAuth firebaseAuth;
    //fireBaseAuth id
    private String currentUserId;

    //to get device location
    //using locationManager
    LocationManager locationManager;
    LocationListener locationListener;
    //using fused location provider client
    private FusedLocationProviderClient client;



   //toolBar and drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);

        //toolbar and drawer
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.navigation_drawer_icon);





        drawerLayout = findViewById(R.id.employee_drawer);
        navigationView = findViewById(R.id.employee_navigation_view);

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

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //getLocation using location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkProvider();//check if user enabled location

        //getLocation using fused location client
        client = new FusedLocationProviderClient(EmployeeDashBoard.this);

        //used to get employee Id from loginScreen Activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            currentUserId = bundle.getString("currentUserId");
        }

        //call to give view data
        setUpRecyclerview();
    }//onCreate


    //check if location provider is on
    public void checkProvider() {
        //i put this because locationManger.requestLocationUpdates couldn't work without permission checks
        //wanted to ignore it because requestPermissions does not turn on permission Button
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmployeeDashBoard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(EmployeeDashBoard.this, "You must turn on Location", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EmployeeDashBoard.this, LoginScreen.class));
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } else {
            //listener that responds to location updates
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //if location manager can't get location (because this turned on location but not always app level permission)
                    //use Fused location provider client
                    if (location == null) {
                        //fused client.getLastLocation() requires this check in order to work
                        //when this 'if' is true the location will have already been turned on
                        //by Settings.ACTION_LOCATION_SOURCE_SETTINGS above when user taps 'OK'
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        //get location using fused location provider
                         client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                             @Override
                             public void onSuccess(Location location) {
                                 updateEmployeeLocation(location); //update using fused location provider
                             }
                         });

                    }else
                    {
                        updateEmployeeLocation(location); //update using location Manager
                    }
                }//onLocationChanged

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }//check provider() method


   //update user location
    public  void updateEmployeeLocation(Location location)
    {
        //know current user
        String currentUserId = firebaseAuth.getUid();
        if (currentUserId != null)
           {
               //update user Location(Current Address) in FireStore document
               //using Geo coder to convert latlang into an address
               //try catch to handle IO exception from geocoder.getFromLocation()
               try
               {
                   Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                   List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                   String address  = addresses.get(0).getAddressLine(0);
                   taskReference.document(currentUserId).update("currentLocation",address);
               }catch (IOException e){
                   System.out.println("Error in geo coder -> EmployeeDashboard: "+e.getMessage());
               }

           }
    }//upDateEmployeeLocation

    //set recycler view adapter
    protected void setUpRecyclerview() {
        //query to fetch data
        Query query = taskReference.whereEqualTo("employeeId", currentUserId);
        //options of type modal
        //takes query and modal class
        FirestoreRecyclerOptions<ShowTaskAdapterModal> options = new FirestoreRecyclerOptions.Builder<ShowTaskAdapterModal>()
                .setQuery(query, ShowTaskAdapterModal.class)
                .build();
        //adapter takes options
        adapter = new ShowTaskAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.show_tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }//setUpRecyclerview()

    //log out user
    public void logOut()
    {
        firebaseAuth.signOut();
        startActivity(new Intent(EmployeeDashBoard.this,LoginScreen.class));
    }

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

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
       //checkProvider();
    }
}
