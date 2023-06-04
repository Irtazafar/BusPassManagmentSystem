package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentDisapproveActivity extends AppCompatActivity {

    private TextInputEditText rollNumberEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText contactNumberEditText;
    private TextInputEditText emailEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_disapprove);

        Intent intent = getIntent();
        String rollNum = intent.getStringExtra("rollNum");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String contact = intent.getStringExtra("contact");

        rollNumberEditText = findViewById(R.id.rollNumFeild);
        nameEditText = findViewById(R.id.nameField);
        contactNumberEditText = findViewById(R.id.contactField);
        emailEditText = findViewById(R.id.emailField);

        rollNumberEditText.setText(rollNum);
        nameEditText.setText(name);
        emailEditText.setText(email);
        contactNumberEditText.setText(contact);


        MaterialButton registerButton = findViewById(R.id.cancelRegisterBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRegistraionFunction();
            }
        });
    }

    private void cancelRegistraionFunction() {
        String rollNum = rollNumberEditText.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Users").whereEqualTo("RollNum", rollNum);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().update("accountStatus", false)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Updated Successfully !", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Cannot Updated", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
    }
}