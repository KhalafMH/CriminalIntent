package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a single crime
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mTitle = "";
        mDate = new Date();
        mSolved = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Crime)) {
            return false;
        }
        Crime that = (Crime) obj;
        return mId.equals(that.mId)
                && mTitle.equals(that.mTitle)
                && mDate.equals(that.mDate)
                && mSolved == that.mSolved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }
}
