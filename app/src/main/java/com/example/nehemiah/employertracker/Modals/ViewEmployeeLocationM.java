package com.example.nehemiah.employertracker.Modals;
//used to retrieve all data from task document
//purpose is to know the employees' currentLocation
//other data important for identification
public class ViewEmployeeLocationM {
    private String firstName;
    private String lastName;
    private String taskDescription;
    private String location;
    private String currentLocation;

    public ViewEmployeeLocationM() {
    }

    public ViewEmployeeLocationM(String firstName, String lastName, String taskDescription, String location, String currentLocation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.taskDescription = taskDescription;
        this.location = location;
        this.currentLocation = currentLocation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getLocation() {
        return location;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }
}
