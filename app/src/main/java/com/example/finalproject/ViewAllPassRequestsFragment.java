package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAllPassRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllPassRequestsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ArrayList<PassClass> arrayList;
    PassFirebaseAdapter adapter;
    FirebaseFirestore fStore;
    public ViewAllPassRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAllPassRequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAllPassRequestsFragment newInstance(String param1, String param2) {
        ViewAllPassRequestsFragment fragment = new ViewAllPassRequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_pass_requests, container, false);

        recyclerView = view.findViewById(R.id.passView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fStore = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();

        //showPassRequestFun();

        return view;
    }

    private void showPassRequestFun() {
        fStore.collection("Pass")
                .whereEqualTo("passStatus",false)
                .whereEqualTo("declineStatus",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String a = document.getString("RollNum");
                                String b = document.getString("ParentNo");
                                String c = document.getString("DateTime");
                                String d = document.getString("Reason");
                                Boolean e = document.getBoolean("passStatus");

                                PassClass showPass = new PassClass(a, d, b, c, e);
                                arrayList.add(showPass);
                            }

                            adapter = new PassFirebaseAdapter(getContext(), arrayList, new PassFirebaseAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(PassClass obj) {
                                    updatePassStatus(obj);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext(), "Error fetching Pass data.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        showPassRequestFun();
    }

    private void updatePassStatus(PassClass obj) {
        Intent intent= new Intent(getContext(), PassUpdateActivity.class);
        intent.putExtra("RollNum", obj.getRollNum());
        intent.putExtra("ParentNo", obj.getParentNo());
        intent.putExtra("Reason", obj.getReason());
        intent.putExtra("status", obj.getStatus());
        startActivity(intent);
    }

}