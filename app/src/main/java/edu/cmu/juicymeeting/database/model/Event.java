package edu.cmu.juicymeeting.database.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object for storing event information
 * Created by qiuzhexin on 11/13/15.
 */
public class Event implements Parcelable {

    public String eventName;
    private String description;
    private String creatorName;
    private int followers;
    public String location;
    public String date;

    public String agenda;
    public String time;
    public String founder;
    public User creator;
    public ChatRoom chatroom;

    public Event() {

    }

    public Event(String eventName, String location, String date) {
        this.eventName = eventName;
        this.location = location;
        this.date = date;
    }

    protected Event(Parcel in) {
        eventName = in.readString();
        location = in.readString();
        agenda = in.readString();
        date = in.readString();
        time = in.readString();
        founder = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public ChatRoom getChatroom() {
        return chatroom;
    }

    public void setChatroom(ChatRoom chatroom) {
        this.chatroom = chatroom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(location);
        dest.writeString(agenda);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(founder);
    }
}
