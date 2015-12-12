package edu.cmu.juicymeeting.database.chatDB;

/**
 * Chat group
 * Created by qiuzhexin on 12/6/15.
 */
public class Group {
    public int groupCode;
    public long createTime;

    public Group() {
    }

    public Group(int groupCode, long createTime) {
        this.groupCode = groupCode;
        this.createTime = createTime;
    }
}
