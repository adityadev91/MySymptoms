package com.adityadevg.mysymptoms.DatabaseModule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String SYMPTOMS_DATABASE_NAME = "MySymptomsDB.db";
    public static final String SYMPTOMS_TABLE_NAME = "SymptomEntries";
    private static int DATABASE_VERSION = 1;

    public static final String SYMPTOMS_COLUMN_ID = "id";
    public static final String SYMPTOMS_COLUMN_DATE_TIME = "date_time";
    public static final String SYMPTOMS_COLUMN_BODY_PART = "body_part";
    public static final String SYMPTOMS_COLUMN_SYMPTOM_DESC = "symptom_desc";
    public static final String SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY = "level_of_severity";
    public static final String SYMPTOMS_COLUMN_IMAGE_ID = "image_id";

    public static final String FORMAT_SYMPTOMS_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_SYMPTOMS_DATE = "yyyy-MM-dd";
    public static final String FORMAT_SYMPTOMS_TIME = "HH:mm:ss";

    private static SQLiteDatabase db;
    private String statement;

    public DBHelper(Context context) {
        super(context, SYMPTOMS_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        statement = "CREATE TABLE IF NOT EXISTS " + SYMPTOMS_TABLE_NAME +
                " (" + SYMPTOMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SYMPTOMS_COLUMN_DATE_TIME + " TEXT UNIQUE NOT NULL, " +
                SYMPTOMS_COLUMN_BODY_PART + " TEXT NOT NULL, " +
                SYMPTOMS_COLUMN_SYMPTOM_DESC + " TEXT NOT NULL, " +
                SYMPTOMS_COLUMN_IMAGE_ID + " TEXT, " +
                SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY + " TEXT NOT NULL)";

        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        statement = "DROP TABLE IF EXISTS " + SYMPTOMS_TABLE_NAME;

        db.execSQL(statement);
        onCreate(db);
    }

    public boolean insertSymptom(String date_time, String str_bodyPart, String str_symptomDesc, String str_levelOfSeverity, String str_imageID) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SYMPTOMS_COLUMN_DATE_TIME, date_time);
        contentValues.put(SYMPTOMS_COLUMN_BODY_PART, str_bodyPart);
        contentValues.put(SYMPTOMS_COLUMN_SYMPTOM_DESC, str_symptomDesc);
        contentValues.put(SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY, str_levelOfSeverity);
        contentValues.put(SYMPTOMS_COLUMN_IMAGE_ID, str_imageID);

        if (db.insertOrThrow(SYMPTOMS_TABLE_NAME, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public int getIdFromDateTime(String date_time) {
        db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + SYMPTOMS_TABLE_NAME +
                " WHERE " + SYMPTOMS_COLUMN_DATE_TIME + "= ?", new String[]{date_time});
        res.moveToFirst();

        return Integer.valueOf(res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_ID)));

    }

    public Bundle getSymptomFromDateTime(String date_time) {
        Bundle entryDetailsBundle = new Bundle();
        db = this.getReadableDatabase();
        String dateValue = "";
        String timeValue = "";

        try {
            DateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_DATE_TIME);
            Date date = dateTimeFormat.parse(date_time);

            DateFormat dateFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_DATE);
            DateFormat timeFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_TIME);

            dateValue = dateFormat.format(date);
            timeValue = timeFormat.format(date);

        } catch (ParseException pe) {
            pe.getStackTrace();
        }

        Cursor res = db.rawQuery("SELECT * FROM " + SYMPTOMS_TABLE_NAME +
                " WHERE " + SYMPTOMS_COLUMN_DATE_TIME + "= ?", new String[]{date_time});
        res.moveToFirst();


        entryDetailsBundle.putString(FORMAT_SYMPTOMS_DATE, dateValue);
        entryDetailsBundle.putString(FORMAT_SYMPTOMS_TIME, timeValue);
        entryDetailsBundle.putString(SYMPTOMS_COLUMN_BODY_PART, res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_BODY_PART)));
        entryDetailsBundle.putString(SYMPTOMS_COLUMN_SYMPTOM_DESC, res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_SYMPTOM_DESC)));
        entryDetailsBundle.putString(SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY, res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY)));
        entryDetailsBundle.putString(SYMPTOMS_COLUMN_IMAGE_ID, res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_IMAGE_ID)));

        return entryDetailsBundle;
    }

    public int getNumberOfSymptoms() {
        db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SYMPTOMS_TABLE_NAME);
        return numRows;
    }

    public boolean updateSymptom(int symptomID, String date_time, String str_bodyPart, String str_symptomDesc, String str_levelOfSeverity, String str_imageID) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String dateValue = "";
        String timeValue = "";

        try {
            DateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_DATE_TIME);
            Date date = dateTimeFormat.parse(date_time);

            DateFormat dateFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_DATE);
            DateFormat timeFormat = new SimpleDateFormat(FORMAT_SYMPTOMS_TIME);

            dateValue = dateFormat.format(date);
            timeValue = timeFormat.format(date);

        } catch (ParseException pe) {
            pe.getStackTrace();
        }

        contentValues.put(SYMPTOMS_COLUMN_DATE_TIME, dateValue + " " + timeValue);
        contentValues.put(SYMPTOMS_COLUMN_BODY_PART, str_bodyPart);
        contentValues.put(SYMPTOMS_COLUMN_SYMPTOM_DESC, str_symptomDesc);
        contentValues.put(SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY, str_levelOfSeverity);
        contentValues.put(SYMPTOMS_COLUMN_IMAGE_ID, str_imageID);
        if (db.update(SYMPTOMS_TABLE_NAME, contentValues, SYMPTOMS_COLUMN_ID + " = ? ", new String[]{String.valueOf(symptomID)}) == 1) {
            return true;
        }
        return false;
    }

    public Integer deleteSymptom(String date_time) {
        db = this.getWritableDatabase();
        return db.delete(SYMPTOMS_TABLE_NAME,
                SYMPTOMS_COLUMN_DATE_TIME + " = ? ",
                new String[]{date_time});
    }

    public List<String> getListOfColumnValues(String colName) {
        db = this.getWritableDatabase();
        List<String> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + SYMPTOMS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(colName)));
            res.moveToNext();
        }
        return array_list;
    }

    public List<SymptomDetailsDTO> getListOfAllSymptomDetails() {
        db = this.getWritableDatabase();
        List<SymptomDetailsDTO> array_list = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + SYMPTOMS_TABLE_NAME, null);
        res.moveToFirst();

        SymptomDetailsDTO symptomDetailsObj;

        String str_dateTimeStamp;
        String str_bodyPart;
        String str_symptomDesc;
        String str_levelOfSecurity;
        String str_pictureID;

        while (!res.isAfterLast()) {

            str_dateTimeStamp = res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_DATE_TIME));
            str_bodyPart = res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_BODY_PART));
            str_symptomDesc = res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_SYMPTOM_DESC));
            str_levelOfSecurity = res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY));
            str_pictureID = res.getString(res.getColumnIndex(SYMPTOMS_COLUMN_IMAGE_ID));

            symptomDetailsObj = SymptomDetailsDTO.getInstanceOfSymptomDetailsDTO(str_dateTimeStamp, str_bodyPart, str_symptomDesc, str_levelOfSecurity, str_pictureID);
            array_list.add(symptomDetailsObj);

            res.moveToNext();
        }
        return array_list;
    }

    public boolean isExists(int symptomID) {
        Cursor res = null;
        String statement = "SELECT * FROM " + SYMPTOMS_TABLE_NAME + " WHERE " + SYMPTOMS_COLUMN_ID + "= ?";
        res = db.rawQuery(statement, new String[]{String.valueOf(symptomID)});

        if (res.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}