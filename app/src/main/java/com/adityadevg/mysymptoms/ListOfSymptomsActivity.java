package com.adityadevg.mysymptoms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.adityadevg.mysymptoms.DatabaseModule.DBHelper;
import com.adityadevg.mysymptoms.DatabaseModule.EntryDetailsActivity;
import com.adityadevg.mysymptoms.DatabaseModule.ListItemSymptomDetailsDTO;

import java.util.List;

/**
 * Created by adityadev
 */
public class ListOfSymptomsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab_newEntry;
    private static final int REQUEST_NEW_ENTRY = 1;
    private DBHelper dbHelper;

    private ListView listView_symptomsList;
    private List<String> listOfAllDateTimes;
    private List<String> listOfAllSeverityLevels;
    private List<String> listOfAllBodyParts;
    SymptomsArrayAdapter symptomsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        fab_newEntry = (FloatingActionButton) findViewById(R.id.newEntryBtn);

        listOfAllDateTimes = dbHelper.getListOfColumnValues(DBHelper.SYMPTOMS_COLUMN_DATE_TIME);
        listOfAllSeverityLevels = dbHelper.getListOfColumnValues(DBHelper.SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY);
        listOfAllBodyParts = dbHelper.getListOfColumnValues(DBHelper.SYMPTOMS_COLUMN_BODY_PART);

        listView_symptomsList = (ListView) findViewById(R.id.lv_symptomsList);
        populateSymptoms();

    }

    public void openEntryDetails(View newEntryView) {
        startActivityForResult(new Intent(this, EntryDetailsActivity.class), REQUEST_NEW_ENTRY);
    }

    public void populateSymptoms() {
        symptomsArrayAdapter = new SymptomsArrayAdapter(this, ListItemSymptomDetailsDTO.getListOfSymptomDetailsDTO(listOfAllDateTimes, listOfAllBodyParts, listOfAllSeverityLevels));
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
        ListItemSymptomDetailsDTO listItemObj = (ListItemSymptomDetailsDTO) parent.getItemAtPosition(position);
        final String date_time = listItemObj.getDateTimeStamp();
        startActivity((new Intent(getApplicationContext(), EntryDetailsActivity.class))
                .putExtras(dbHelper.getSymptomFromDateTime(date_time)));
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
                            listOfAllDateTimes.remove(date_time);
                            symptomsArrayAdapter.notifyDataSetChanged();
                            listView_symptomsList.setAlpha(1);
                        }
                    });
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_deleted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_could_not_be_deleted), Toast.LENGTH_SHORT).show();
        }
    }
}
