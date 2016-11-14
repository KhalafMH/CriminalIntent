package com.bignerdranch.android.criminalintent;

import java.util.UUID;

/**
 * Represents a single crime
 */

public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime(String title) {
        mTitle = title;
        mId = UUID.randomUUID();
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
