package com.adityadevg.mysymptoms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adityadev
 */
public class SymptomsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab_newEntry;
    private static final int REQUEST_NEW_ENTRY = 1;
    private DBHelper dbHelper;

    private ListView listView_symptomsList;
    private List<String> listOfSymptoms;
    SymptomsArrayAdapter symptomsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        fab_newEntry = (FloatingActionButton) findViewById(R.id.newEntryBtn);

        listOfSymptoms = dbHelper.getAllSymptoms();
        listView_symptomsList = (ListView) findViewById(R.id.lv_symptomsList);
        populateSymptoms();

    }

    public void openEntryDetails(View newEntryView) {
        startActivityForResult(new Intent(this, EntryDetailsActivity.class), REQUEST_NEW_ENTRY);
    }

    public void populateSymptoms() {
        symptomsArrayAdapter = new SymptomsArrayAdapter(this, android.R.layout.simple_list_item_1, listOfSymptoms);
        listView_symptomsList.setAdapter(symptomsArrayAdapter);
        listView_symptomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showModifyEntryOptions(parent, view, position);
            }
        });
    }

    /*
    * Called by setOnItemClickListener to enable modification of entries
    */
    public void showModifyEntryOptions(final AdapterView<?> parent, final View view, final int position) {
        // Set dialog options to display when user clicks list view item
        CharSequence[] listOfModifyEntryOptions = {getString(R.string.edit), getString(R.string.delete)};

        final AlertDialog.Builder modifyEntryAlertDialog = new AlertDialog.Builder(this);
        modifyEntryAlertDialog.setTitle(getString(R.string.modify_entry));
        modifyEntryAlertDialog.setItems(listOfModifyEntryOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        updateEntry(parent, position);
                        break;
                    case 1:
                        deleteEntry(parent, view, position);
                        break;
                    default:
                        break;
                }
            }
        });
        modifyEntryAlertDialog.create();
        modifyEntryAlertDialog.show();
    }

    private void updateEntry(final AdapterView<?> parent, final int position) {
        final String date_time = (String) parent.getItemAtPosition(position);
        startActivity((new Intent(getApplicationContext(), EntryDetailsActivity.class))
                .putExtras(dbHelper.getSymptomEntry(date_time)));
    }

    /*
    *  Deletes selected entry from UI and db
    */
    private void deleteEntry(final AdapterView<?> parent, final View view, final int position) {
        final String date_time = (String) parent.getItemAtPosition(position);
        if (dbHelper.deleteSymptom(date_time) == 1){
            view.animate().setDuration(2000).alpha(0)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            listOfSymptoms.remove(date_time);
                            symptomsArrayAdapter.notifyDataSetChanged();
                            listView_symptomsList.setAlpha(1);
                        }
                    });
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_deleted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_could_not_be_deleted), Toast.LENGTH_SHORT).show();
        }
    }

    private class SymptomsArrayAdapter extends ArrayAdapter<String> {

        private final Map<String, Integer> mapOfSymptomEntries;

        public SymptomsArrayAdapter(Context context, int resource, List<String> listOfSymptomEntries) {
            super(context, resource, listOfSymptomEntries);

            mapOfSymptomEntries = new HashMap<>();

            for (int i = 0; i < listOfSymptomEntries.size(); ++i) {
                mapOfSymptomEntries.put(listOfSymptomEntries.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            return mapOfSymptomEntries.get(getItem(position));
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
