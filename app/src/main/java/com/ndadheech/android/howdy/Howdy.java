package com.ndadheech.android.howdy;

import java.util.Date;
import java.util.UUID;

public class Howdy {
    private UUID mId;
    private Friend mFriend;
    private Date mDate;

    public Howdy(Friend friend, Date date) {
        mId = UUID.randomUUID();
        mFriend = friend;
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public Friend getFriend() {
        return mFriend;
    }

    public void setFriend(Friend friend) {
        mFriend = friend;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
