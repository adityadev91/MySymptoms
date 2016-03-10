package com.adityadevg.mysymptoms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adityadev on 3/4/2016.
 * Activity displays a form to edit existing entry or create a new entry
 */
public class EntryDetailsActivity extends AppCompatActivity implements OnItemSelectedListener {
    Toolbar toolbar;
    Spinner sp_selectBodyPart;
    String bodyPart;

    /**
     * Random, unique value for receive image from intent
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Random, unique value for receive image from intent
     */
    static final int REQUEST_IMAGE_SELECT = 2;

    /**
     * Root of the layout of this Activity
     */
    private View linearLayoutView;

    private Bitmap bitmap;
    private ImageView symptomImg;
    List<String> listOfPermissions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutView = findViewById(R.id.linearLayoutView);
        symptomImg = (ImageView) findViewById(R.id.img_symptom);

        sp_selectBodyPart = (Spinner) findViewById(R.id.spinner_bodyPart);
        sp_selectBodyPart.setOnItemSelectedListener(this);
        sp_selectBodyPart.setPrompt(getString(R.string.choose_a_level_of_severity));

    }

    /**
     * Called when the 'show camera' button is clicked.
     * Callback is defined in resource layout definition.
     */
    public void capturePicture(View view) {
        Log.i(getLocalClassName(), "Show camera button pressed. Checking permission.");
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // Camera and external storage R/W permission has not been granted.
            requestCameraAndExtStoragePermission();
        } else {
            // Permissions are already available, show the camera preview.
            showCamOptions();
        }
    }

    /**
     * Requests the Camera permission
     * If permission was denied previously, a SnackBar will prompt user to grant
     * permission, else permission is requested directly
     */
    private void requestCameraAndExtStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Display additional message to the user, if permission was not granted
            // User would benefit they have previously denied permission
            Snackbar.make(linearLayoutView, R.string.allow_permissions_for_smooth_functioning, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(EntryDetailsActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_IMAGE_CAPTURE);
                            ActivityCompat.requestPermissions(EntryDetailsActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_IMAGE_CAPTURE);
                            ActivityCompat.requestPermissions(EntryDetailsActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_IMAGE_CAPTURE);
                        }
                    })
                    .show();
        } else {
            // Camera permission has not been granted yet. Request it directly
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
        }
    }

    /*
     * Called by Camera button
     */
    public void showCamOptions() {
        // Set dialog options to display when user clicks Camera button
        CharSequence[] listOfPictureOptions = {getString(R.string.click_a_new_picture), getString(R.string.select_from_existing_images)};

        final AlertDialog.Builder capturePictureAlertDialog = new AlertDialog.Builder(this);
        capturePictureAlertDialog.setTitle(getString(R.string.attach_a_picture));
        capturePictureAlertDialog.setItems(listOfPictureOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent captureImageIntent;
                switch (which) {
                    case 0:
                        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
                        break;
                    case 1:
                        captureImageIntent = new Intent(
                                Intent.ACTION_GET_CONTENT,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        captureImageIntent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(captureImageIntent, getString(R.string.select_from_existing_images)),
                                REQUEST_IMAGE_SELECT);

/*
                        captureImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        captureImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
*/
                        break;
                    default:
                        break;
                }
            }
        });
        capturePictureAlertDialog.create();
        capturePictureAlertDialog.show();
    }

    @Override
    /** To receive image from intent */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && ((requestCode == REQUEST_IMAGE_CAPTURE) || (requestCode == REQUEST_IMAGE_SELECT))) {
            try {
                // We need to recyle unused bitmaps
                /*if (null != bitmap) {
                    bitmap.recycle();
                }
                */
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                symptomImg.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calls DatePickerFragment which in turn calls TimePickerFragment
     */
    public void setTimeOfOccurrence(View timeView) {
        DialogFragment setDateTimeFragment = new DatePickerFragment();
        setDateTimeFragment.show(getFragmentManager(), getString(R.string.date_picker));
    }

    /**
     * Called by Save button
     */
    public void saveEntry(View entryView) {
        final AlertDialog.Builder saveEntryDialog = new AlertDialog.Builder(this);
        saveEntryDialog.setMessage(R.string.your_changes_can_be_modified_or_deleted_later)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_logged), Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(getApplicationContext(), SymptomsActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        saveEntryDialog.create();
        saveEntryDialog.show();
    }

    /**
     * Called by Back button
     */
    public void dontSaveEntry(View dontSaveView) {
        final AlertDialog.Builder dontSaveEntryDialog = new AlertDialog.Builder(this);
        dontSaveEntryDialog.setMessage(R.string.your_changes_will_be_discarded)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getApplicationContext(), SymptomsActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dontSaveEntryDialog.create();
        dontSaveEntryDialog.show();

    }

    @Override
    /** Stores selected value from Body Part Spinner in a string */
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        bodyPart = parent.getItemAtPosition(position).toString();
    }

    @Override
    /** If no body part is selected, display toast message */
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), getString(R.string.please_select_a_body_part_from_given_options), Toast.LENGTH_SHORT).show();
        Toast.makeText(parent.getContext(), getString(R.string.select_other_if_none_of_them_seem_valid), Toast.LENGTH_SHORT).show();
    }
}
