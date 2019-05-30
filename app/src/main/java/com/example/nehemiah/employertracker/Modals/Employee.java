package com.example.nehemiah.employertracker.Modals;

public class Employee {

    private String first_name;
    private String last_name;
    private  String _emial;
    private String phone_number;
    private String pass_word;
    private String role;

    public Employee()
    {}

    public Employee(String first_name, String last_name, String _emial, String phone_number, String pass_word,String role) {
        this.first_name = first_name;
        this.last_name = last_name;
        this._emial = _emial;
        this.phone_number = phone_number;
        this.pass_word = pass_word;
        this.role =role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String get_emial() {
        return _emial;
    }

    public void set_emial(String _emial) {
        this._emial = _emial;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

}
