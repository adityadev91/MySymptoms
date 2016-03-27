package com.adityadevg.mysymptoms.DatabaseModule;

/**
 * Created by adityadev on 3/25/2016.
 */
public class SymptomDetailsDTO {
    private String str_dateTimeStamp;
    private String str_bodyPart;
    private String str_symptomDesc;
    private String str_levelOfSecurity;
    private String str_pictureID;

    private SymptomDetailsDTO(){}

    private SymptomDetailsDTO(String str_dateTimeStamp, String str_bodyPart, String str_symptomDesc, String str_levelOfSecurity, String str_pictureID) {
        setDateTimeStamp(str_dateTimeStamp);
        setBodyPart(str_bodyPart);
        setSymptomDesc(str_symptomDesc);
        setLevelOfSecurity(str_levelOfSecurity);
        setPictureID(str_pictureID);
    }

    public static SymptomDetailsDTO getInstanceOfSymptomDetailsDTO(String str_dateTimeStamp, String str_bodyPart, String str_symptomDesc, String str_levelOfSecurity, String str_pictureID){
        return new SymptomDetailsDTO(str_dateTimeStamp, str_bodyPart, str_symptomDesc, str_levelOfSecurity, str_pictureID);
    }


    public String getBodyPart() {
        return str_bodyPart;
    }

    private void setBodyPart(String str_bodyPart) {
        this.str_bodyPart = str_bodyPart;
    }

    public String getdateTimeStamp() {
        return str_dateTimeStamp;
    }

    private void setDateTimeStamp(String str_dateTimeStamp) {
        this.str_dateTimeStamp = str_dateTimeStamp;
    }

    public String getLevelOfSecurity() {
        return str_levelOfSecurity;
    }

    private void setLevelOfSecurity(String str_levelOfSecurity) {
        this.str_levelOfSecurity = str_levelOfSecurity;
    }

    public String getPictureID() {
        return str_pictureID;
    }

    private void setPictureID(String str_pictureID) {
        this.str_pictureID = str_pictureID;
    }

    public String getSymptomDesc() {
        return str_symptomDesc;
    }

    private void setSymptomDesc(String str_symptomDesc) {
        this.str_symptomDesc = str_symptomDesc;
    }
}
