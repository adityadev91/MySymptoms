package com.adityadevg.mysymptoms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by adityadev on 3/4/2016.
 */
public class EntryDetailsActivity extends AppCompatActivity implements OnItemSelectedListener {
    Toolbar toolbar;
    FloatingActionButton fab;
    Spinner sp_selectBodyPart;
    String bodyPart;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        sp_selectBodyPart = (Spinner) findViewById(R.id.spinner_bodyPart);
        sp_selectBodyPart.setOnItemSelectedListener(this);
        sp_selectBodyPart.setPrompt(getString(R.string.choose_a_level_of_severity));
    }

    public void capturePicture(View pictureView) {
        final AlertDialog.Builder capturePictureAlertDialog = new AlertDialog.Builder(this);
        CharSequence[] listOfPictureOptions = new CharSequence[2];
        listOfPictureOptions[0] = getString(R.string.click_a_new_picture);
        listOfPictureOptions[1] = getString(R.string.select_from_existing_images);
        capturePictureAlertDialog.setItems(listOfPictureOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent captureImageIntent = new Intent();
                switch (which) {
                    case 0:
                        captureImageIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
                        break;
                    case 1:
                        captureImageIntent.setType("image/*");
                        captureImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        captureImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setTimeOfOccurrence(View timeView){
        DialogFragment setDateTimeFragment = new DatePickerFragment();
        setDateTimeFragment.show(getFragmentManager(),getString(R.string.date_picker));
    }

    public void saveEntry(View entryView) {
        final AlertDialog.Builder saveEntryDialog = new AlertDialog.Builder(this);
        saveEntryDialog.setMessage(R.string.your_changes_can_be_modified_or_deleted_later)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_logged), Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(getApplicationContext(),SymptomsActivity.class)
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bodyPart = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), getString(R.string.please_select_a_body_part_from_given_options), Toast.LENGTH_SHORT).show();
        Toast.makeText(parent.getContext(), getString(R.string.select_other_if_none_of_them_seem_valid), Toast.LENGTH_SHORT).show();
    }
}
