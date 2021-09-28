package com.example.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        // General specs
        public TextView Dhash;
        public TextView Did;
        public TextView Daddress;
        public TextView Dname;
        public TextView Dadvertising;

        // For ibeacon
        public LinearLayout llibeacon;
        public TextView Duuid;
        public TextView Dmajor;
        public TextView Dminor;
        public TextView Dpower;

        // For Eddystone UID
        public LinearLayout llibeacon;
        public TextView Duuid;
        public TextView Dmajor;
        public TextView Dminor;
        public TextView Dpower;

        // For Eddystone URL
        public LinearLayout llibeacon;
        public TextView Duuid;
        public TextView Dmajor;
        public TextView Dminor;
        public TextView Dpower;

        // For Eddystone TLM
        public LinearLayout llibeacon;
        public TextView Duuid;
        public TextView Dmajor;
        public TextView Dminor;
        public TextView Dpower;

        // For Eddystone EID
        public LinearLayout llibeacon;
        public TextView Duuid;
        public TextView Dmajor;
        public TextView Dminor;
        public TextView Dpower;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            Dhash = itemView.findViewById(R.id.dhash);
            Did = itemView.findViewById(R.id.did);
            Daddress = itemView.findViewById(R.id.daddress);
            Dname = itemView.findViewById(R.id.dname);
            Dadvertising = itemView.findViewById(R.id.dadvertising);

            // For ibeacon
            llibeacon = itemView.findViewById(R.id.llibeacon);
            Duuid = itemView.findViewById(R.id.duuid);
            Dmajor = itemView.findViewById(R.id.dmajor);
            Dminor = itemView.findViewById(R.id.dminor);
            Dpower = itemView.findViewById(R.id.dpower);

        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.Dhash.setText(currentItem.getMhashcode());
        holder.Dname.setText(currentItem.getMdevicename());
        holder.Did.setText(currentItem.getDevicerssi());
        holder.Daddress.setText(currentItem.getMdeviceaddress());
        holder.Dadvertising.setText(currentItem.getMdeviceadvertising());

        int type = currentItem.getType();

        if (type == 1) {
            holder.llibeacon.setVisibility(View.VISIBLE);
            // For ibeacon
            holder.Duuid.setText(currentItem.getMuuid().toString());
            holder.Dmajor.setText(currentItem.getMmajor());
            holder.Dminor.setText(currentItem.getMminor());
            holder.Dpower.setText(currentItem.getMpower());
        } else holder.llibeacon.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}
