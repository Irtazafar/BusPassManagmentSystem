package com.example.finalproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<UsersClass> UsersArrayList;

    public RecyclerViewAdapter(ArrayList<UsersClass> arrayList) {
        this.UsersArrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_row, parent, false);
        ViewHolder obj = new ViewHolder(view);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersClass obj = UsersArrayList.get(position);
        holder.rollnum.setText(obj.getRollNum());
        holder.name.setText(obj.getName());
        holder.email.setText(obj.getEmail());
        holder.contact.setText(obj.getContactNo());

    }

    @Override
    public int getItemCount() {
        return UsersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView rollnum,name,email,contact;
        public ViewHolder(@NonNull View v) {
            super(v);
            rollnum = v.findViewById(R.id.txtroll);
            name = v.findViewById(R.id.txtname);
            email = v.findViewById(R.id.txtemail);
            contact = v.findViewById(R.id.txtphone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                UsersClass clickedUser = UsersArrayList.get(position);
                //String rollNum = clickedUser.getRollNum();
                //Toast.makeText(itemView.getContext(), "Clicked Roll#: " + rollNum, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(itemView.getContext(), StudentDisapproveActivity.class);
                intent.putExtra("rollNum", clickedUser.getRollNum());
                intent.putExtra("name", clickedUser.getName());
                intent.putExtra("email", clickedUser.getEmail());
                intent.putExtra("contact", clickedUser.getContactNo());
                itemView.getContext().startActivity(intent);
            }

        }
    }


}
