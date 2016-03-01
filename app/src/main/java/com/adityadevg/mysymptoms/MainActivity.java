package com.adityadevg.mysymptoms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_profile = (Button) findViewById(R.id.profileBtn);
        Button btn_viewSymptoms = (Button) findViewById(R.id.viewSymptomsBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    /** Called when the user clicks the Mail button */
    public void sendPDFInMail(View mailView){
        Snackbar.make(mailView, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    /** Called when the user clicks btn_profile */
    public void openProfile(View profileView){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    /** Called when the user clicks the btn_viewSymptoms */
    public void openSymptoms(View symptomsView){
        startActivity(new Intent(this, SymptomsActivity.class));
    }



}
