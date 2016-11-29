package com.bignerdranch.android.criminalintent;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a single crime
 */

public class Crime {

    private UUID mId;
    @NonNull
    private String mTitle;
    @NonNull
    private Date mDate;
    private boolean mSolved;
    @NonNull
    private String mSuspect;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mTitle = "";
        mDate = new Date();
        mSolved = false;
        mSuspect = "";
    }

    @NonNull
    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(@NonNull String suspect) {
        mSuspect = suspect;
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
                && mSolved == that.mSolved
                && mSuspect.equals(that.mSuspect);
    }

    @NonNull
    public Date getDate() {
        return mDate;
    }

    public void setDate(@NonNull Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }
}
