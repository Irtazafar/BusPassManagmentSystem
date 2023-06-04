package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAllStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAllStudentFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<UsersClass> arrayList = new ArrayList<>();
    RecyclerViewAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewAllStudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewAllStudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAllStudentFragment newInstance(String param1, String param2) {
        ViewAllStudentFragment fragment = new ViewAllStudentFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_student, container, false);
        recyclerView = view.findViewById(R.id.studentsRecView);
        adapter = new RecyclerViewAdapter(arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //InsertData();
        return view;
    }

    private void InsertData() {
        arrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("userLevel","2")
                .whereEqualTo("accountStatus",true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String roll = document.getString("RollNum");
                                String name = document.getString("Name");
                                String email = document.getString("Email");
                                String num = String.valueOf(document.get("Contact"));

                                UsersClass user = new UsersClass(roll, name, num, email);

                                arrayList.add(user);

                            }

                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getContext(), "Error getting documents: ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        InsertData();
    }
}