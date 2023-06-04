package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class AddNewBusActivity extends AppCompatActivity {

    private TextInputEditText busNoEditText;
    private TextInputEditText driverNoEditText;
    private TextInputEditText routeListEditText;
    private CheckBox approveCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bus);

        busNoEditText = findViewById(R.id.busNoEditText);
        driverNoEditText = findViewById(R.id.driverNoEditText);
        routeListEditText = findViewById(R.id.routeListEditText);
        approveCheckBox = findViewById(R.id.approveCheckBox);

        MaterialButton addNewBusButton = findViewById(R.id.addNewBusButton);
       addNewBusButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addNewBusFun();
           }
       });
    }

    private void addNewBusFun() {
        String busNo = busNoEditText.getText().toString();
        String driverNo = driverNoEditText.getText().toString();
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
                    if (task.getResult().isEmpty()) {
                        BusClass newBus = new BusClass(busNo, driverNo, routeListList, isApproved);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Buses")
                                .add(newBus)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(), "Bus Added successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to add bus. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(getApplicationContext(), "Bus with the same bus number already exists.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Error checking bus number. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}