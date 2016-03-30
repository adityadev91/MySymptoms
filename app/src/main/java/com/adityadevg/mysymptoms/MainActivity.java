package com.adityadevg.mysymptoms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adityadevg.mysymptoms.DatabaseModule.DBHelper;
import com.adityadevg.mysymptoms.DatabaseModule.EntryDetailsActivity;
import com.adityadevg.mysymptoms.SharePDFModule.GeneratePDF;

public class MainActivity extends AppCompatActivity {

    private View linearLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_profile = (Button) findViewById(R.id.profileBtn);
        Button btn_viewSymptoms = (Button) findViewById(R.id.viewSymptomsBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutView = findViewById(R.id.linear_layout_content_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sendMail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsToSendMail();
            }
        });
    }

    /**
     * Called when the user clicks the Mail button
     */
    public void checkPermissionsToSendMail() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestExtStoragePermission();
        } else {
            sendPdfInMail();
        }
    }

    private void sendPdfInMail() {
        Intent mailPDFIntent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        mailPDFIntent.setType("message/rfc822");
        mailPDFIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        mailPDFIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        mailPDFIntent.putExtra(Intent.EXTRA_STREAM, GeneratePDF.createPDF(new DBHelper(this), getSharedPreferences(ProfileActivity.keyPreferenceName, MODE_PRIVATE)));
        mailPDFIntent.setData(Uri.parse("mailto:"));
        mailPDFIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        try {
            // User can choose an email client
            startActivity(Intent.createChooser(mailPDFIntent, getString(R.string.choose_an_email_client)));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, getString(R.string.no_email_client_installed),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void requestExtStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Display additional message to the user, if permission was not granted
            // User would benefit they have previously denied permission
            Snackbar.make(linearLayoutView, R.string.allow_permissions_for_smooth_functioning, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    EntryDetailsActivity.REQUEST_IMAGE_CAPTURE);
                        }
                    })
                    .show();
        } else {
            // External Storage permission has not been granted yet. Request it directly
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EntryDetailsActivity.REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Called when the user clicks btn_profile
     */
    public void openProfile(View profileView) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    /**
     * Called when the user clicks the btn_viewSymptoms
     */
    public void openSymptoms(View symptomsView) {
        startActivity(new Intent(this, SymptomsActivity.class));
    }
}
