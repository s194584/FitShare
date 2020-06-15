package com.example.youfit.domain;

public class UserHelper {

    private String mName;
    private String mEmail;
    private String mPassword;

    public UserHelper() {
    }

    public UserHelper(String mEmail, String mPassword, String mName) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
