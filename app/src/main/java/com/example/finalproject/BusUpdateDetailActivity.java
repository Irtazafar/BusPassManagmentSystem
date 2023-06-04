package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusUpdateDetailActivity extends AppCompatActivity {

    private TextInputEditText busNoEditText;
    private TextInputEditText driverNoEditText;
    private TextInputEditText routeListEditText;
    private CheckBox approveCheckBox;

    String busNo,driverNo;
    ArrayList<String> routeList;
    boolean status;

    FirebaseFirestore fStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_update_detail);

        Intent intent = getIntent();
        busNo = intent.getStringExtra("BusNo");
        driverNo = intent.getStringExtra("driverNo");
        status = intent.getBooleanExtra("status", false);
        routeList = intent.getStringArrayListExtra("routeList");

        busNoEditText = findViewById(R.id.busText);
        driverNoEditText = findViewById(R.id.driverText);
        routeListEditText = findViewById(R.id.routeText);
        approveCheckBox = findViewById(R.id.approveBox);

        busNoEditText.setText(busNo);
        driverNoEditText.setText(driverNo);
        routeListEditText.setText(TextUtils.join(",", routeList));
        approveCheckBox.setChecked(status);

        MaterialButton updateBusButton = findViewById(R.id.updateBtn);
        MaterialButton deleteBusButton = findViewById(R.id.deleteBusBtn);
        
        updateBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });
        
        deleteBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDetails();
            }
        });
    }

    private void deleteDetails() {
        busNo = busNoEditText.getText().toString();
        fStore = FirebaseFirestore.getInstance();
        Query query = fStore.collection("Buses").whereEqualTo("busNo", busNo);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {

                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentId = documentSnapshot.getId();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Buses").document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Bus Deleted successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed to delete bus details. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(), "Bus with the specified bus number does not exist.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error checking bus number. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDetails() {
        driverNo = driverNoEditText.getText().toString();
        String routeList = routeListEditText.getText().toString();
        boolean isApproved = approveCheckBox.isChecked();

        String[] routeListArray = routeList.split(",");
        List<String> routeListList = Arrays.asList(routeListArray);

        // Check if the bus number already exists
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Buses").whereEqualTo("busNo", busNo);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentId = documentSnapshot.getId();

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("driverNo", driverNo);
                        updates.put("approved", isApproved);
                        updates.put("routeList", routeListList);

                        fStore = FirebaseFirestore.getInstance();
                        db.collection("Buses").document(documentId)
                                .update(updates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Bus details updated successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to update bus details. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(getApplicationContext(), "Bus with the specified bus number does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Error checking bus number. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}