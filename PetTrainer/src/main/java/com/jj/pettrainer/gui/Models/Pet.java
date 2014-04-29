package com.jj.pettrainer.gui.Models;

/**
 * Created by JCCP on 4/28/14.
 */
public class Pet {

    private User owner;
    private int id;
    private String title;
    private String url;
    private String share_code;


    public Pet() {
    }

    public Pet(String title) {
        this.title = title;
    }

    public Pet(User owner, int id, String title, String url, String share_code) {
        this.owner = owner;
        this.id = id;
        this.title = title;
        this.url = url;
        this.share_code = share_code;
    }

    public User getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public String getShare_code() {
        return share_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return title;
    }
}
