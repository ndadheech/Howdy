package com.ndadheech.android.howdy;

/**
 * Copyright Nibha Dadheech
 * This class is responsible to contains information of the friend.
 * @author ndadheech
 */
public class Friend {
    private String friendName;
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
