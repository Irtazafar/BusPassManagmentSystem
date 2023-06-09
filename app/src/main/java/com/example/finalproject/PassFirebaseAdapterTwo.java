package com.example.finalproject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PassFirebaseAdapterTwo extends RecyclerView.Adapter<PassFirebaseAdapterTwo.MyViewHolder>{

    Context context;
    ArrayList<PassClass> passList;

    public PassFirebaseAdapterTwo(Context context, ArrayList<PassClass> passList) {
        this.context = context;
        this.passList = passList;
    }

    @NonNull
    @Override
    public PassFirebaseAdapterTwo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_view, parent, false);
        return new PassFirebaseAdapterTwo.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassFirebaseAdapterTwo.MyViewHolder holder, int position) {
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

        holder.imageView.setImageBitmap(obj.getQrCodeImage());

    }

    @Override
    public int getItemCount() {
        return passList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rollNo,parentNo,date,passStatus,reason;
        ImageView imageView;
        public MyViewHolder(@NonNull View view) {
            super(view);
            rollNo = view.findViewById(R.id.passRollNum);
            parentNo = view.findViewById(R.id.passParentNo);
            date = view.findViewById(R.id.passDate);
            passStatus = view.findViewById(R.id.passStatus);
            reason = view.findViewById(R.id.passReason);
            imageView = view.findViewById(R.id.qrCodeImage);
        }
    }
}
