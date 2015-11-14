package edu.cmu.juicymeeting.database.model;

/**
 * Message users exchange in chat room
 * Created by qiuzhexin on 11/13/15.
 */
public class Message {
    public ChatRoom chatRoom; // the place where the message sent
    public User sayer; // the person who sends the msg
    public String text;
    public String timeStamp;
}
