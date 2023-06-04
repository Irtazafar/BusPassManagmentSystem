package com.example.finalproject;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseAdapter extends ArrayAdapter<HashMap<String,String>> {
    private ArrayList<HashMap<String, String>> dataSet;
    Context mContext;

    public FirebaseAdapter(ArrayList<HashMap<String, String>> data, Context context) {
        super(context, R.layout.pending_registration_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pending_registration_layout, parent, false);
        }
        HashMap<String, String> user = getItem(position);

        // Lookup view for data population
        TextView txtRollNum = convertView.findViewById(R.id.txtrollnum);
        TextView txtName = convertView.findViewById(R.id.txtname);
        TextView txtEmail = convertView.findViewById(R.id.txtemail);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        // Populate the data into the template view using the data object
        txtRollNum.setText(user.get("RollNum"));
        txtName.setText(user.get("Name"));
        txtEmail.setText(user.get("Email"));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateFireStore(user);
                    //refreshData();

                }
            }
        });

        return convertView;
    }

    private void refreshData() {
        notifyDataSetChanged();
    }

    private void updateFireStore(HashMap<String, String> user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("RollNum",user.get("RollNum"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Users").document(document.getId())
                                        .update("accountStatus", true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                                                user.put("accountStatus", "true");
                                                refreshData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });


    }
}
