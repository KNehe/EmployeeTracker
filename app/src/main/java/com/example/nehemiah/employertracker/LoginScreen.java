package com.example.nehemiah.employertracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.nehemiah.employertracker.Employees.EmployeeDashBoard;
import com.example.nehemiah.employertracker.Modals.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;


public class LoginScreen extends AppCompatActivity {

    private TextInputEditText editTextEmail,editTextPassowrd;
    private ContentLoadingProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button signInButton = findViewById(R.id.sign_in_button);
        editTextEmail =  findViewById(R.id.login_email);
        editTextPassowrd = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);

        //to be Goat from snapshop

        firebaseAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassowrd.getText().toString();

                if (email.isEmpty())
                {
                    editTextEmail.setError("Email Required");
                    editTextEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    editTextEmail.setError("Invalid Email");
                    editTextEmail.requestFocus();
                    return;
                }
                if (password.isEmpty())
                {
                    editTextPassowrd.setError("Password Required");
                    editTextPassowrd.requestFocus();
                    return;
                }

                //make call to sign in User
                signInUser(email,password);
            }
        });//onclick
    }//onCreate


    //signIn user
    private void signInUser(final String email, String password)
    {
        progressBar.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //know current user
                        final String currentUserId = firebaseAuth.getUid();
                        if (task.isSuccessful())
                        {
                            if (currentUserId != null)
                            {
                                database.collection("Employers").document(currentUserId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                String role = task.getResult().get("Role").toString();
                                                if (role.equals("Admin"))
                                                {
                                                    startActivity(new Intent(LoginScreen.this,admin_dashboard.class));
                                                    progressBar.hide();
                                                    editTextEmail.setText("");
                                                    editTextPassowrd.setText("");
                                                }else if(role.equals("Employee")){
                                                    Intent intent = new Intent(LoginScreen.this,EmployeeDashBoard.class);
                                                    intent.putExtra("currentUserId",currentUserId);
                                                    startActivity(intent);
                                                    progressBar.hide();
                                                    editTextEmail.setText("");
                                                    editTextPassowrd.setText("");
                                                }else
                                                {
                                                    Toast.makeText(LoginScreen.this,"An error occurred",Toast.LENGTH_LONG).show();
                                                    progressBar.hide();
                                                    editTextEmail.setText("");
                                                    editTextPassowrd.setText("");
                                                }

                                            }
                                        });
                            }//if currentUserId != null

                        }// if task is Successfull

                    }//onComplete
                })//addOnComplete
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.hide();
                    }
                });

    }

}//class
