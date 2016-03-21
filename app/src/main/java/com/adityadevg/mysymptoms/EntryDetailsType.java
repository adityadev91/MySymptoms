package com.adityadevg.mysymptoms;

/**
 * Created by adityadev on 3/8/2016.
 */
public class EntryDetailsType {

    String str_timeStamp;
    String str_dateStamp;
    String str_bodyPart;
    String str_symptomDesc;
    String str_levelOfSecurity;
    String str_pictureID;

    private EntryDetailsType(){}

    public EntryDetailsType(String str_dateStamp, String str_timeStamp, String str_bodyPart, String str_symptomDesc, String str_levelOfSecurity, String str_pictureID) {
        setStr_dateStamp(str_dateStamp);
        setStr_timeStamp(str_timeStamp);
        setStr_bodyPart(str_bodyPart);
        setStr_symptomDesc(str_symptomDesc);
        setStr_levelOfSecurity(str_levelOfSecurity);
        setStr_pictureID(str_pictureID);
    }

    public String getStr_bodyPart() {
        return str_bodyPart;
    }

    public void setStr_bodyPart(String str_bodyPart) {
        this.str_bodyPart = str_bodyPart;
    }

    public String getStr_dateStamp() {
        return str_dateStamp;
    }

    public void setStr_dateStamp(String str_dateStamp) {
        this.str_dateStamp = str_dateStamp;
    }

    public String getStr_levelOfSecurity() {
        return str_levelOfSecurity;
    }

    public void setStr_levelOfSecurity(String str_levelOfSecurity) {
        this.str_levelOfSecurity = str_levelOfSecurity;
    }

    public String getStr_pictureID() {
        return str_pictureID;
    }

    public void setStr_pictureID(String str_pictureID) {
        this.str_pictureID = str_pictureID;
    }

    public String getStr_symptomDesc() {
        return str_symptomDesc;
    }

    public void setStr_symptomDesc(String str_symptomDesc) {
        this.str_symptomDesc = str_symptomDesc;
    }

    public String getStr_timeStamp() {
        return str_timeStamp;
    }

    public void setStr_timeStamp(String str_timeStamp) {
        this.str_timeStamp = str_timeStamp;
    }
}
