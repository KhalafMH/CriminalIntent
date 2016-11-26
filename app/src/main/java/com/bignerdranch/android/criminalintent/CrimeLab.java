package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Holds all the crimes.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>(100);
        for (int i = 0; i < 5; ++i) {
            Crime crime = new Crime("");
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);

            mCrimes.add(crime);
        }
    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public static void addCrime(Crime crime) {
        sCrimeLab.mCrimes.add(crime);
    }

    public List<Crime> getCrimes() {
        return Collections.unmodifiableList(mCrimes);
    }

    public Crime getCrime(UUID crimeId) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(crimeId)) {
                return crime;
            }
        }
        return null;
    }
}
