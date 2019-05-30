package com.example.nehemiah.employertracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nehemiah.employertracker.Modals.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin_add_employee extends AppCompatActivity {

    public  static   TextInputEditText firstName,lastName,email,phoneNumber,password;

    private Button addButton;

   private FirebaseAuth firebaseAuth;
    private  FirebaseFirestore db ;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_employee);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //getting references
        firstName =(TextInputEditText)findViewById(R.id.first_name);
        lastName =(TextInputEditText)findViewById(R.id.last_name);
        email =(TextInputEditText)findViewById(R.id.email);
        phoneNumber  =(TextInputEditText)findViewById(R.id.phone_number);
        password =(TextInputEditText)findViewById(R.id.pass_Word);
        addButton = (MaterialButton)findViewById(R.id.button_add);

        db =  FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Admin_add_employee.this);

          //listener on addButton
       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //getting data from fields
              String first_name = firstName.getText().toString();
              String last_name = lastName.getText().toString();
              String _email = email.getText().toString();
              String phone_number = phoneNumber.getText().toString();
              String pass_word = password.getText().toString();

              //adding data to employee modal
               Employee employee = new Employee(first_name,last_name,_email,phone_number,pass_word,"Employee");

               addEmployee(employee);
           }
       });//onClick
    }//onCreate


    //add employee
    public  void  addEmployee(final Employee employee)
    {

        //validating fields
        if (employee.getFirst_name().isEmpty())
        {
            firstName.setError("First Name is Required");
            firstName.requestFocus();
            return;
        }
        if (employee.getLast_name().isEmpty())
        {
            lastName.setError("Last Name is Required");
            lastName.requestFocus();
            return;
        }
        if (employee.get_emial().isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(employee.get_emial()).matches())
        {
            email.setError("Invalid Email");
            email.requestFocus();
            return;
        }
        if (employee.getPhone_number().isEmpty())
        {
            phoneNumber.setError("Email is required");
            phoneNumber.requestFocus();
            return;
        }
        if (employee.getPhone_number().length() < 10)
        {
            phoneNumber.setError("Should be 10 numbers");
            phoneNumber.requestFocus();
            return;
        }
        if (employee.getPass_word().isEmpty())
        {
            password.setError("Email is required");
            password.requestFocus();
            return;
        }
        if (employee.getPass_word().length() < 6)
        {
            password.setError("Password should have at least 8 characters");
            password.requestFocus();
            return;
        }

        //save to cloud firestore
        //alert user what is going on
        progressDialog.setTitle("EV");
        progressDialog.setMessage("Adding employee...");
        progressDialog.show();

        //create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(employee.get_emial(),employee.getPass_word())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            progressDialog.hide();
                            Toast.makeText(Admin_add_employee.this, "Email Already In use", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //can be removed and use custom Employee object directly
                        Map<String,Object> employees = new HashMap<>();
                        employees.put("First_Name",employee.getFirst_name());
                        employees.put("Last_Name",employee.getLast_name());
                        employees.put("Email",employee.get_emial());
                        employees.put("Phone_Number",employee.getPhone_number());
                        employees.put("Password",employee.getPass_word());
                        employees.put("Role",employee.getRole());

                        //adding saving user details
                        String userId = firebaseAuth.getUid();//currentUserId
                        db.collection("Employers").document(userId).set(employees)
                                //signing up the created user
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Admin_add_employee.this,"Employee Added!!",Toast.LENGTH_SHORT).show();
                                        firstName.setText("");
                                        lastName.setText("");
                                        email.setText("");
                                        phoneNumber.setText("");
                                        password.setText("");
                                        progressDialog.hide();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin_add_employee.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressDialog.hide();
                                    }
                                });///end of document stuff

                    }//onComplete
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toast.makeText(Admin_add_employee.this,"User not created",Toast.LENGTH_LONG).show();
                    }
                });//end of createuser()




    }//void addEmployee
}
