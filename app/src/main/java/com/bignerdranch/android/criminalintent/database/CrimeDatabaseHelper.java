package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "CrimeDatabase.db";

    public CrimeDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "
                + CrimeDbSchema.CrimeTable.NAME
                + " ("
                + "id primary key" + ", "
                + CrimeDbSchema.CrimeTable.Cols.UUID + " text unique" + ", "
                + CrimeDbSchema.CrimeTable.Cols.TITLE + " text" + ", "
                + CrimeDbSchema.CrimeTable.Cols.DATE + " date" + ", "
                + CrimeDbSchema.CrimeTable.Cols.SOLVED + " int"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
