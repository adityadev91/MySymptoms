package com.adityadevg.mysymptoms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by adityadev
 */
public class SymptomsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    Button btn_newEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        btn_newEntry = (Button) findViewById(R.id.newEntryBtn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    public void openEntryDetails(View newEntryView){
        startActivity(new Intent(this, EntryDetailsActivity.class));
    }


}
