package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PassUpdateActivity extends AppCompatActivity {

    private TextInputEditText passNoEditText;
    private TextInputEditText parentNoEditText;
    private TextInputEditText reasonEditText;
    private CheckBox approveCheckBox,declineBox;


    FirebaseFirestore fStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_update);

        Intent intent = getIntent();
        String a = intent.getStringExtra("RollNum");
        String b = intent.getStringExtra("ParentNo");
        String c = intent.getStringExtra("Reason");
        Boolean d = intent.getBooleanExtra("status", false);

        passNoEditText = findViewById(R.id.passRollText);
        parentNoEditText = findViewById(R.id.passParentText);
        reasonEditText = findViewById(R.id.passReasonText);
        approveCheckBox = findViewById(R.id.approveBox);
        declineBox = findViewById(R.id.declineBox);

        passNoEditText.setText(a);
        parentNoEditText.setText(b);
        reasonEditText.setText(c);
        approveCheckBox.setChecked(d);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialButton updateDetails = findViewById(R.id.passUpdateBtn);
        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassDetails();
            }
        });

    }

    private void updatePassDetails() {
        String rollNum = passNoEditText.getText().toString();
        boolean isApproved = approveCheckBox.isChecked();
        boolean isDeclined = declineBox.isChecked();

        if (isApproved && isDeclined) {
            Toast.makeText(this, "Cannot approve and decline simultaneously.", Toast.LENGTH_SHORT).show();
        } else if (isApproved) {
            FirebaseFirestore.getInstance().collection("Pass")
                    .whereEqualTo("RollNum", rollNum)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("passStatus", true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(PassUpdateActivity.this, "Pass approved.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PassUpdateActivity.this, "Failed to approve pass.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(PassUpdateActivity.this, "Error retrieving pass details.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else if (isDeclined) {
            FirebaseFirestore.getInstance().collection("Pass")
                    .whereEqualTo("RollNum", rollNum)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().update("declineStatus", true, "passStatus", false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(PassUpdateActivity.this, "Pass declined.", Toast.LENGTH_SHORT).show();
                                                finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PassUpdateActivity.this, "Failed to decline pass.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(PassUpdateActivity.this, "Error retrieving pass details.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}