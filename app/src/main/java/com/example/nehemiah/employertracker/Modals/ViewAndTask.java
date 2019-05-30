package com.example.nehemiah.employertracker.Modals;
//used when retrieving some of all user data from collection("Employers")
//implementation in EmployeeAdapter located in Admin package
public class ViewAndTask {

    private String Email;
    private String First_Name;
    private String Last_Name;



    public ViewAndTask() {
    }

    public ViewAndTask(String email, String first_Name, String last_Name) {
        Email = email;
        First_Name = first_Name;
        Last_Name = last_Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }
}
