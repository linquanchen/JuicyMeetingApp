package edu.cmu.juicymeeting.database.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object for storing event information
 * Created by qiuzhexin on 11/13/15.
 */
public class Event implements Parcelable {

    private int id;
    private String eventImage;
    private String eventName;
    private String description;
    private String creatorImage;
    private String creatorName;
    private int followers;
    private String location;
    private String date;
    private long titleContextColor;
    private long imageContextColor;


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
        id = in.readInt();
        eventImage = in.readString();
        eventName = in.readString();
        description = in.readString();
        creatorImage = in.readString();
        creatorName = in.readString();
        followers = in.readInt();
        location = in.readString();
        date = in.readString();
        titleContextColor = in.readLong();
        imageContextColor = in.readLong();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getImageContextColor() {
        return imageContextColor;
    }

    public void setImageContextColor(long imageContextColor) {
        this.imageContextColor = imageContextColor;
    }

    public long getTitleContextColor() {
        return titleContextColor;
    }

    public void setTitleContextColor(long titleContextColor) {
        this.titleContextColor = titleContextColor;
    }

    public String getCreatorImage() {
        return creatorImage;
    }

    public void setCreatorImage(String creatorImage) {
        this.creatorImage = creatorImage;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
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
        dest.writeInt(id);
        dest.writeString(eventImage);
        dest.writeString(eventName);
        dest.writeString(description);
        dest.writeString(creatorImage);
        dest.writeString(creatorName);
        dest.writeInt(followers);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeLong(titleContextColor);
        dest.writeLong(imageContextColor);
    }
}
