package com.adityadevg.mysymptoms.DatabaseModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityadev on 3/25/2016.
 */
public class ListItemSymptomDetailsDTO {
    private String str_dateTimeStamp;
    private String str_bodyPart;
    private String str_levelOfSecurity;

    private ListItemSymptomDetailsDTO() {
    }

    private ListItemSymptomDetailsDTO(String str_dateTimeStamp, String str_bodyPart, String str_levelOfSecurity) {
        setDateTimeStamp(str_dateTimeStamp);
        setBodyPart(str_bodyPart);
        setLevelOfSecurity(str_levelOfSecurity);
    }

    public static ListItemSymptomDetailsDTO getInstanceOfSymptomDetailsDTO(String str_dateTimeStamp, String str_bodyPart, String str_levelOfSecurity) {
        return new ListItemSymptomDetailsDTO(str_dateTimeStamp, str_bodyPart, str_levelOfSecurity);
    }


    public String getBodyPart() {
        return str_bodyPart;
    }

    private void setBodyPart(String str_bodyPart) {
        this.str_bodyPart = str_bodyPart;
    }

    public String getDateTimeStamp() {
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

    public static List<ListItemSymptomDetailsDTO> getListOfSymptomDetailsDTO(List<String> listOfAllDateTimes, List<String> listOfAllBodyParts, List<String> listOfAllSeverityLevels) {
        List<ListItemSymptomDetailsDTO> array_list = new ArrayList<>();
        for(int i =0;i<listOfAllBodyParts.size();i++){
            array_list.add(getInstanceOfSymptomDetailsDTO(listOfAllDateTimes.get(i), listOfAllBodyParts.get(i), listOfAllSeverityLevels.get(i)));
            i++;
        }
        return array_list;
    }
}
