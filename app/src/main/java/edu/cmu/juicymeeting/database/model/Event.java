package edu.cmu.juicymeeting.database.model;

/**
 * Object for storing event information
 * Created by qiuzhexin on 11/13/15.
 */
public class Event {
    public String eventName;
    public String location;
    public String agenda;
    public String date;
    public String time;
    public String founder;
    public User creator;
    public ChatRoom chatroom;
}
