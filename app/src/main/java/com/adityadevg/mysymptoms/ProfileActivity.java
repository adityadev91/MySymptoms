package com.adityadevg.mysymptoms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by adityadev
 */
public class ProfileActivity extends AppCompatActivity {


    public final static String keyPreferenceName = "MySymptomsProfile";
    public final static String keyPatientName = "PatientName";
    public final static String keyPatientEmail = "PatientEmail";
    public final static String keyPatientPhone = "PatientPhone";
    public final static String keyInsuranceName = "InsuranceName";
    public final static String keyMemberID = "MemberID";
    public final static String keyEmergencyName = "EmergencyName";
    public final static String keyEmergencyPhone = "EmergencyPhone";


    SharedPreferences profileData;
    SharedPreferences.Editor profileDataEditor;
    EditText et_patientName, et_patientEmail, et_patientPhone,
            et_insuranceName, et_memberID, et_emergencyName,
            et_emergencyPhone;
    Button btn_back, btn_save;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Init Components
        et_patientName = (EditText) findViewById(R.id.editText_patientName);
        et_patientEmail = (EditText) findViewById(R.id.editText_patientEmail);
        et_patientPhone = (EditText) findViewById(R.id.editText_patientPhone);
        et_insuranceName = (EditText) findViewById(R.id.editText_insuranceName);
        et_memberID = (EditText) findViewById(R.id.editText_memberID);
        et_emergencyName = (EditText) findViewById(R.id.editText_emergencyName);
        et_emergencyPhone = (EditText) findViewById(R.id.editText_emergencyPhone);
        btn_back = (Button) findViewById(R.id.saveBtn);
        btn_save = (Button) findViewById(R.id.backBtn);

        loadSavedProfile();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void loadSavedProfile() {
        // Init Shared Preferences
        profileData = getSharedPreferences(keyPreferenceName, MODE_PRIVATE);
        profileDataEditor = profileData.edit();

        et_patientName.setText(profileData.getString(keyPatientName, ""));
        et_patientEmail.setText(profileData.getString(keyPatientEmail, ""));
        et_patientPhone.setText(profileData.getString(keyPatientPhone, ""));
        et_insuranceName.setText(profileData.getString(keyInsuranceName, ""));
        et_memberID.setText(profileData.getString(keyMemberID, ""));
        et_emergencyName.setText(profileData.getString(keyEmergencyName, ""));
        et_emergencyPhone.setText(profileData.getString(keyEmergencyPhone, ""));

    }

    public void saveProfile(View saveProfileView) {
        final AlertDialog.Builder saveDataDialog = new AlertDialog.Builder(this);
        saveDataDialog.setMessage(R.string.your_profile_will_be_visible_in_all_your_logs)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setProfileData();
                        Toast.makeText(getBaseContext(), getString(R.string.your_profile_data_has_been_updated), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        saveDataDialog.create();
        saveDataDialog.show();
    }

    private void setProfileData() {
        profileDataEditor.putString(keyPatientName, et_patientName.getText().toString());
        profileDataEditor.putString(keyPatientEmail, et_patientEmail.getText().toString());
        profileDataEditor.putString(keyPatientPhone, et_patientPhone.getText().toString());
        profileDataEditor.putString(keyInsuranceName, et_insuranceName.getText().toString());
        profileDataEditor.putString(keyMemberID, et_memberID.getText().toString());
        profileDataEditor.putString(keyEmergencyName, et_emergencyName.getText().toString());
        profileDataEditor.putString(keyEmergencyPhone, et_emergencyPhone.getText().toString());
        profileDataEditor.commit();
    }

    public void dontSaveProfile(View dontSaveView) {
        final AlertDialog.Builder dontSaveDataDialog = new AlertDialog.Builder(this);
        dontSaveDataDialog.setMessage(R.string.your_changes_will_be_discarded)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dontSaveDataDialog.create();
        dontSaveDataDialog.show();

    }
}
