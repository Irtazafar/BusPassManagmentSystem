package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UpdateBusActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<BusClass> arrayList;

    BusFirebaseAdapter adapter;

    FirebaseFirestore fStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bus);

        recyclerView = findViewById(R.id.updatebusRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();

        //FetchData();
    }

    private void FetchData() {
        arrayList.clear();
        fStore.collection("Buses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String busNo = document.getString("busNo");
                                String driverNo = document.getString("driverNo");
                                List<String> routeList = (List<String>) document.get("routeList");
                                boolean isApproved = document.getBoolean("approved");

                                BusClass bus = new BusClass(busNo, driverNo, routeList, isApproved);
                                arrayList.add(bus);

                            }
                            adapter = new BusFirebaseAdapter(getApplicationContext(), arrayList, new BusFirebaseAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(BusClass obj) {
                                    openUpdateActivity(obj);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error fetching buses data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openUpdateActivity(BusClass obj) {

        Intent intent= new Intent(this, BusUpdateDetailActivity.class);
        intent.putExtra("BusNo", obj.getBusNo());
        intent.putExtra("driverNo", obj.getDriverNo());
        intent.putExtra("status", obj.isApproved());
        intent.putStringArrayListExtra("routeList", new ArrayList<>(obj.getRouteList()));
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList.clear();
        FetchData();
    }
}