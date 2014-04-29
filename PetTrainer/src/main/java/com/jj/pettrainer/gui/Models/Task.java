package com.jj.pettrainer.gui.Models;

import java.text.SimpleDateFormat;

/**
 * Created by JCCP on 4/29/14.
 */
public class Task {

    private User created_by;
    private int id;
    private boolean completed;
    private int pet;
    private String created_on;
    private String due_date;
    private String remind_before;
    private String title;
    private String description;

    @Override
    public String toString() {
        return this.title;
    }

    public Task() {
    }

    public Task(int id, User created_by, boolean completed, int pet, String created_on, String due_date, String remind_before, String title, String description) {

        this.id = id;
        this.created_by = created_by;
        this.completed = completed;
        this.pet = pet;
        this.created_on = created_on;
        this.due_date = due_date;
        this.remind_before = remind_before;
        this.title = title;
        this.description = description;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getPet() {
        return pet;
    }

    public void setPet(int pet) {
        this.pet = pet;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getRemind_before() {
        return remind_before;
    }

    public void setRemind_before(String remind_before) {
        this.remind_before = remind_before;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
