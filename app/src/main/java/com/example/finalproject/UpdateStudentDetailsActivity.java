package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateStudentDetailsActivity extends AppCompatActivity {


    private TextInputEditText rollText;
    private TextInputEditText contactText;
    private TextInputEditText nameText;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseFirestore db;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_details);

        rollText = findViewById(R.id.sturollText);
        nameText = findViewById(R.id.stunameText);
        contactText = findViewById(R.id.stuphoneText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        MaterialButton updateButton = findViewById(R.id.stuDetailsBtn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentDetails();
            }
        });


        if (firebaseUser != null) {
            fetchData();
        }


    }

    private void fetchData() {
        String userEmail = firebaseUser.getEmail();

        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");

        Query query = usersRef.whereEqualTo("Email", userEmail);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            String rollNumber = documentSnapshot.getString("RollNum");
                            String fullName = documentSnapshot.getString("Name");
                            String contactNumber = documentSnapshot.getString("Contact");

                            rollText.setText(rollNumber);
                            nameText.setText(fullName);
                            contactText.setText(contactNumber);
                        }else {
                            Toast.makeText(getApplicationContext(), "User document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to fetch user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void updateStudentDetails() {

        String rollNumber = rollText.getText().toString();
        String fullName = nameText.getText().toString();
        String contactNumber = contactText.getText().toString();

        db = FirebaseFirestore.getInstance();
        CollectionReference user = db.collection("Users");

        Query query = user.whereEqualTo("RollNum", rollNumber);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference().update(
                                        "Name", fullName,
                                        "Contact", contactNumber)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "User details updated successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed to update user details. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Error fetching user details. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}