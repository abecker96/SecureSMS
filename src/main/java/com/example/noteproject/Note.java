package com.example.noteproject;

public class Note {
    private long ID;
    private String subject;
    private String body;

    //Constructors
    Note(){}

    Note(String subject, String body){
        this.subject = subject;
        this.body = body;
    }

    Note(long ID, String subject, String body){
        this.ID = ID;
        this.subject = subject;
        this.body = body;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
