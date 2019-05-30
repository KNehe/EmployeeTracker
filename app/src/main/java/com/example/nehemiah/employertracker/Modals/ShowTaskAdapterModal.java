package com.example.nehemiah.employertracker.Modals;

public class ShowTaskAdapterModal {

    private String location;
    private String taskDescription;

    public ShowTaskAdapterModal() {
    }

    public ShowTaskAdapterModal(String location,String taskDescription) {
        this.location = location;
        this.taskDescription = taskDescription;

    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getLocation() {
        return location;
    }
}
