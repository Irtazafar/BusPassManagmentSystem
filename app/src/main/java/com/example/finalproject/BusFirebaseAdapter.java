package com.example.finalproject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusFirebaseAdapter extends RecyclerView.Adapter<BusFirebaseAdapter.MyViewHolder> {

    private ItemClickListener itemClickListener;
    Context context;
    ArrayList<BusClass> busList;

    public BusFirebaseAdapter(Context context, ArrayList<BusClass> busList) {
        this.context = context;
        this.busList = busList;
    }

    public BusFirebaseAdapter(Context context, ArrayList<BusClass> busList,ItemClickListener itemClickListener) {
        this.context = context;
        this.busList = busList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public BusFirebaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buses_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusFirebaseAdapter.MyViewHolder holder, int position) {
        BusClass obj = busList.get(position);
        holder.busNo.setText(obj.getBusNo());
        holder.driveNo.setText(obj.getDriverNo());
        if (obj.isApproved()) {
            holder.status.setText("Approved");
        } else {
            holder.status.setText("Not Approved");
        }
        String routeListString = TextUtils.join(", ", obj.getRouteList());
        holder.routeList.setText(routeListString);

        holder.itemView.setOnClickListener(view ->{
            itemClickListener.onItemClick(obj);
        });
    }

    @Override
    public int getItemCount() {
       return busList.size();
    }

    public interface ItemClickListener {
        void onItemClick(BusClass obj);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView busNo,driveNo,routeList,status;
        public MyViewHolder(@NonNull View view) {
            super(view);
            busNo = view.findViewById(R.id.busId);
            driveNo = view.findViewById(R.id.driverNo);
            status = view.findViewById(R.id.busStatus);
            routeList = view.findViewById(R.id.busRouteTxt);

        }
    }

}
