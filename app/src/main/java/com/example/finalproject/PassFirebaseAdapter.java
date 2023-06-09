package com.example.finalproject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PassFirebaseAdapter extends RecyclerView.Adapter<PassFirebaseAdapter.MyViewHolder>{

    private static final int LAYOUT_ONE = 1;
    private static final int LAYOUT_TWO = 2;

    private ItemClickListener itemClickListener;
    Context context;
    ArrayList<PassClass> passList;

    public PassFirebaseAdapter(Context context, ArrayList<PassClass> passList) {
        this.context = context;
        this.passList = passList;
    }

    public PassFirebaseAdapter(Context context, ArrayList<PassClass> passList, ItemClickListener itemClickListener) {
        this.context = context;
        this.passList = passList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PassFirebaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_list, parent, false);
        return new PassFirebaseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassFirebaseAdapter.MyViewHolder holder, int position) {
        PassClass obj = passList.get(position);
        holder.rollNo.setText(obj.getRollNum());
        holder.parentNo.setText(obj.getParentNo());
        holder.date.setText(obj.getDateTime());
        holder.reason.setText(obj.getReason());

        if (obj.getStatus()) {
            holder.passStatus.setText("Approved");
        } else {
            holder.passStatus.setText("Not Approved");
        }

        holder.itemView.setOnClickListener(view ->{
            itemClickListener.onItemClick(obj);
        });
    }

    @Override
    public int getItemCount() {
        return passList.size();
    }

    public interface ItemClickListener {
        void onItemClick(PassClass obj);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rollNo,parentNo,date,passStatus,reason;
        public MyViewHolder(@NonNull View view) {
            super(view);
            rollNo = view.findViewById(R.id.passRollNum);
            parentNo = view.findViewById(R.id.passParentNo);
            date = view.findViewById(R.id.passDate);
            passStatus = view.findViewById(R.id.passStatus);
            reason = view.findViewById(R.id.passReason);
        }
    }
}
