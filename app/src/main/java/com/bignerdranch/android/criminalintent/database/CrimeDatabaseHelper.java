package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

public class CrimeDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "CrimeDatabase.db";

    public CrimeDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "
                + CrimeTable.NAME
                + " ("
                + "id primary key" + ", "
                + CrimeTable.Cols.UUID + " text unique" + ", "
                + CrimeTable.Cols.TITLE + " text" + ", "
                + CrimeTable.Cols.DATE + " date" + ", "
                + CrimeTable.Cols.SOLVED + " int" + ", "
                + CrimeTable.Cols.SUSPECT + " text"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
