package com.adityadevg.mysymptoms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by adityadev
 */
public class SymptomsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab_newEntry;
    private static final int REQUEST_NEW_ENTRY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_newEntry = (FloatingActionButton) findViewById(R.id.newEntryBtn);
    }

    public void openEntryDetails(View newEntryView) {
        startActivityForResult(new Intent(this, EntryDetailsActivity.class), REQUEST_NEW_ENTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_ENTRY && resultCode == Activity.RESULT_OK){



        }
    }
}
