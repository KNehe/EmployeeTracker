package com.example.nehemiah.employertracker.Admin;

import android.content.Context;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nehemiah.employertracker.Modals.ViewEmployeeLocationM;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Locale;

public class ViewEmployeeLocationAdapter extends FirestoreRecyclerAdapter<ViewEmployeeLocationM, ViewEmployeeLocationAdapter.ViewEmployeeLocationHolder> {
    public ViewEmployeeLocationAdapter(@NonNull FirestoreRecyclerOptions<ViewEmployeeLocationM> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewEmployeeLocationHolder holder, int position, @NonNull ViewEmployeeLocationM model) {

        holder.firstName.setText(model.getFirstName());
        holder.lastName.setText(model.getLastName());
        holder.task.setText(model.getTaskDescription());
        holder.sentTo.setText(model.getLocation());
        holder.currentLocation.setText(model.getCurrentLocation());
    }

    @NonNull
    @Override
    public ViewEmployeeLocationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_employee_location_single_item,viewGroup,false);
        return new ViewEmployeeLocationHolder(view);
    }

    class ViewEmployeeLocationHolder extends RecyclerView.ViewHolder
    {
         TextView firstName,lastName,task,sentTo,currentLocation;
        public ViewEmployeeLocationHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.view_first_name);
            lastName = itemView.findViewById(R.id.view_last_name);
            task = itemView.findViewById(R.id.view_task);
            sentTo = itemView.findViewById(R.id.view_sentTo_location);
            currentLocation = itemView.findViewById(R.id.view_current_location);

        }
    }
}
