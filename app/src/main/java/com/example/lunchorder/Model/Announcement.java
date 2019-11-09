package com.example.lunchorder.Model;

public class Announcement {

    private String Title, Message, Link;

    public Announcement(){

    }

    public Announcement(String title, String message, String link) {
        Title = title;
        Message = message;
        Link = link;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
