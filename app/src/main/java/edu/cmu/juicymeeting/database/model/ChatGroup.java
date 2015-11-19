package edu.cmu.juicymeeting.database.model;

/**
 * Created by chenlinquan on 11/18/15.
 */
public class ChatGroup {
    private String groupName;
    private String groupDescription;
    private int groupImage;

    public ChatGroup(String groupName, String groupDescription, int groupImage) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupImage = groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public int getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(int groupImage) {
        this.groupImage = groupImage;
    }

}
