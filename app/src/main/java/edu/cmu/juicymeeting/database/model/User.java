package edu.cmu.juicymeeting.database.model;


import android.graphics.Bitmap;

/**
 * Object for storing user information
 * Created by qiuzhexin on 11/13/15.
 */
public class User {
    public String usrName;
    public String account;
    public String password;
    public Bitmap selfie; // user image

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
