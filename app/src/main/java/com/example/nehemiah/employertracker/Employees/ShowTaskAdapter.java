package com.example.nehemiah.employertracker.Employees;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nehemiah.employertracker.Modals.ShowTaskAdapterModal;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ShowTaskAdapter  extends FirestoreRecyclerAdapter<ShowTaskAdapterModal, ShowTaskAdapter.ShowTaskHolder> {


    public ShowTaskAdapter(@NonNull FirestoreRecyclerOptions<ShowTaskAdapterModal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShowTaskHolder holder, int position, @NonNull ShowTaskAdapterModal model) {
         holder.location.setText(model.getLocation());
         holder.description.setText(model.getTaskDescription());
    }

    @NonNull
    @Override
    public ShowTaskHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_task_item,viewGroup,false);
        return new ShowTaskHolder(v);
    }


    class  ShowTaskHolder extends RecyclerView.ViewHolder{

    TextView location,description;

    public ShowTaskHolder(@NonNull View itemView) {
        super(itemView);
        location = itemView.findViewById(R.id.task_location1);
        description = itemView.findViewById(R.id.task_description1);
    }
}
}
