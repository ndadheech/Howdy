package com.ndadheech.android.howdy;

/**
 * Copyright Nibha Dadheech
 * This class is responsible to contains information of the friend.
 * @author ndadheech
 */
public class Friend {
    private String mFriendName;
    private String mPhoneNumber;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getFriendName() {
        return mFriendName;
    }

    public void setFriendName(String friendName) {
        this.mFriendName = friendName;
    }
}
