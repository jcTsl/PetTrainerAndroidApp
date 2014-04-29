package com.jj.pettrainer.gui.Models;

import com.google.gson.Gson;

/**
 * Created by JCCP on 4/28/14.
 */
public class User {

    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String token;
    private int id;

    public User() {
    }

    public User(String token) {
        this.token = token;
    }

    public User(String username, String first_name, String last_name, String email, String password) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public int getId() { return id; }

}
