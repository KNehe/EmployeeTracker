package com.example.nehemiah.employertracker.Admin;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.nehemiah.employertracker.Modals.Task;
import com.example.nehemiah.employertracker.Modals.ViewAndTask;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;



public class AsignTask extends AppCompatActivity {

    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    //to be used in setUpRecyclerView()
    //to retrieve all employers
    private CollectionReference employeeRef = db.collection("Employers");
    private EmployeeAdapter employeeAdapter;
    private String location_to_do_task;

    private String employeeId;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asign_task);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(AsignTask.this);

        //reference to autoComplete fragment
         final PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);
         placeAutocompleteFragment.setHint("Add Work Location");

        //getting name of the selected place
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                location_to_do_task = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AsignTask.this,status.getStatusMessage(),Toast.LENGTH_SHORT).show();
            }
        });

          //call to set up recyclerview
          setUpRecyclerView();

          //implementing onButtonClick interface in EmployeeAdapter
          employeeAdapter.setOnButtonClickListener(new EmployeeAdapter.onButtonClickListener() {
            @Override
            public void onClick(DocumentSnapshot documentSnapshot, int position,String userDetails[]) {
               //Employee employee = documentSnapshot.toObject(Employee.class); //unnecessary for now
                 employeeId = documentSnapshot.getId(); //id of each document in fireStore

                //checking if the location has been set
                if (location_to_do_task == null)
                {
                    Toast.makeText(AsignTask.this,"Please Add a location",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    //showing progress dialog when assigning task
                    progressDialog.setTitle("EV");
                    progressDialog.setMessage("Assigning Task...");
                    progressDialog.show();
                    //using Task.class blueprint to make custom object
                    //making call to saveTaskMethod to add the task in the database
                    Task task = new Task(userDetails[0],userDetails[1],userDetails[2],userDetails[3],location_to_do_task,employeeId,"Not sent");
                    saveTask(task);
                }
            }
        });//listener
    }//onCreate


    //saving task to fireStore database
    private void saveTask(final Task task)
    {
        //creating task location and adding task
        db.collection("Tasks").document(employeeId).set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(AsignTask.this,"Task assigned to "+task.getFirstName(),Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AsignTask.this,"Failed to Assign "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }//save take


    //giving the recyclerview data
    private void setUpRecyclerView()
    {
        Query query = employeeRef.orderBy("Email", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ViewAndTask> options =new FirestoreRecyclerOptions.Builder<ViewAndTask>()
                .setQuery(query,ViewAndTask.class)
                .build();

        employeeAdapter = new EmployeeAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.assign_task_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(employeeAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        employeeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

            employeeAdapter.stopListening();

    }
}
