package com.example.nehemiah.employertracker.Modals;

//modal to be used when adding a task in firestore
//to be used as custom object instead of using Hashmap in AsignTask.java
public class Task {

    private String firstName;
    private String lastName;
    private String email;
    private String taskDescription;
    private String location;
    private String employeeId;
    private String currentLocation;

    public Task() {
    }

    public Task(String firstName, String lastName, String email, String taskDescription, String location, String employeeId,String currentLocation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.taskDescription = taskDescription;
        this.location = location;
        this.employeeId = employeeId;
        this.currentLocation = currentLocation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
