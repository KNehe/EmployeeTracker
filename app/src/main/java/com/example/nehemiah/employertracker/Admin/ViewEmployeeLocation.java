package com.example.nehemiah.employertracker.Admin;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.nehemiah.employertracker.Modals.ViewEmployeeLocationM;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewEmployeeLocation extends AppCompatActivity {
      FirebaseFirestore databse = FirebaseFirestore.getInstance();
      CollectionReference taskReference = databse.collection("Tasks");

      ViewEmployeeLocationAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee_location);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        setUpRecyclerView();
    }//onCreate

    //set up recycler view
    void setUpRecyclerView()
    {
        Query query = taskReference.orderBy("location", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ViewEmployeeLocationM> options = new FirestoreRecyclerOptions.Builder<ViewEmployeeLocationM>()
                .setQuery(query,ViewEmployeeLocationM.class)
                .build();

        adapter = new ViewEmployeeLocationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.view_employee_location_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }//setUpRecyclerView

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }
}//class
