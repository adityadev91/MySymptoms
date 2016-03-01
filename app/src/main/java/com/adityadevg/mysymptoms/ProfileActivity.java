package com.adityadevg.mysymptoms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by adityadev
 */
public class ProfileActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    EditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btn_profile = (Button) findViewById(R.id.profileBtn);
        Button btn_viewSymptoms = (Button) findViewById(R.id.viewSymptomsBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
