package edu.cmu.juicymeeting.database.chatDB;

/**
 * message when user chat
 * Created by qiuzhexin on 12/6/15.
 */
public class Message {
    public Group group;
    public String message;
    public String owner;
    public long createTime;
    public int isSelf;

    public Message() {
    }

    public Message(Group group, String message, String owner, long createTime, int isSelf) {
        this.group = group;
        this.message = message;
        this.owner = owner;
        this.createTime = createTime;
        this.isSelf = isSelf;
    }
}
