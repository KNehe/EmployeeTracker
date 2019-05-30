package com.example.nehemiah.employertracker.Admin;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nehemiah.employertracker.Modals.ViewAndTask;
import com.example.nehemiah.employertracker.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class EmployeeAdapter extends FirestoreRecyclerAdapter<ViewAndTask,EmployeeAdapter.EmployeeHolder>{

    private onButtonClickListener listener;

    public EmployeeAdapter(@NonNull FirestoreRecyclerOptions<ViewAndTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(EmployeeHolder holder, int position, ViewAndTask model) {

        holder.email.setText(String.valueOf(model.getEmail()));
        holder.last_Name.setText(String.valueOf(model.getLast_Name()));
        holder.first_Name.setText(String.valueOf(model.getFirst_Name()));
    }


    @Override
    public EmployeeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_asign_task_card,viewGroup,false);

       return new EmployeeHolder(v);
    }

    class EmployeeHolder extends RecyclerView.ViewHolder{

        TextView  first_Name, last_Name,email;
        Button add_task_button;
        TextInputEditText task_description;

        public EmployeeHolder( View itemView) {
            super(itemView);
            //getting reference to widgets in employee_assign_task_card.xml
            first_Name = itemView.findViewById(R.id.first_name_assign);
            last_Name = itemView.findViewById(R.id.last_name_assign);
            email = itemView.findViewById(R.id.email_assign);
            add_task_button =itemView.findViewById(R.id.button_add_tasks);
            task_description = itemView.findViewById(R.id.task_description);

            //get the id onClick including all userDetails
            add_task_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //array of the user details  expect position
                    String userDetails[] = new String[4];
                    userDetails[0] = first_Name.getText().toString();
                    userDetails[1] = last_Name.getText().toString();
                    userDetails[2] = email.getText().toString();


                    //checking if user entered a description
                    if (task_description.getText().toString().isEmpty())
                    {
                        task_description.setError("Description required");
                        task_description.requestFocus();
                        return;
                    }else
                    {
                        userDetails[3] = task_description.getText().toString(); //description of task to be carried out by employee
                        task_description.setText("");
                    }

                    if (position != RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onClick(getSnapshots().getSnapshot(position),position,userDetails);
                    }

                      }//onClick
            });
        }//view holder constructor
    }//view holder class


    //interface to define blue print for listener on button
    //takes documentSnapshot as parameter,position and an array of user details
    //position it takes is just for testing
    public interface onButtonClickListener
    {
        void onClick(DocumentSnapshot documentSnapshot, int position,String userDetails[]);
    }

    //takes above interface and instantiates it
    public void setOnButtonClickListener(onButtonClickListener listener)
    {
     this.listener = listener;
    }

}//adpater


