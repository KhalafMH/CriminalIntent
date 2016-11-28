package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDatabaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Holds all the crimes.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //    private List<Crime> mCrimes;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeDatabaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public void addCrime(Crime crime) {
        ContentValues values = getCrimeContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    @NonNull
    private ContentValues getCrimeContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved());
        return values;
    }

    public void updateCrime(Crime crime) {
        mDatabase.update(
                CrimeTable.NAME,
                getCrimeContentValues(crime),
                CrimeTable.Cols.UUID + " = ?",
                new String[]{crime.getId().toString()}
        );
    }

    public List<Crime> getCrimes() {
        CrimeCursorWrapper crimeCursor = queryCrimes(null, null);

        ArrayList<Crime> crimesList = new ArrayList<>(crimeCursor.getCount());
        try {
            crimeCursor.moveToFirst();
            while (!crimeCursor.isAfterLast()) {
                crimesList.add(crimeCursor.getCrime());
                crimeCursor.moveToNext();
            }
            return crimesList;
        } finally {
            crimeCursor.close();
        }
    }

    public Crime getCrime(UUID crimeId) {
        CrimeCursorWrapper crimeCursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{crimeId.toString()}
        );
        try {
            if (crimeCursor.getCount() == 0) {
                return null;
            }
            crimeCursor.moveToFirst();
            return crimeCursor.getCrime();
        } finally {
            crimeCursor.close();
        }
    }

    public void deleteCrime(UUID id) {
        mDatabase.delete(
                CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
    }

    private CrimeCursorWrapper queryCrimes(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
