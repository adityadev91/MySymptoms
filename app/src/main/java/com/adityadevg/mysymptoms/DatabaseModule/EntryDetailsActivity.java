package com.adityadevg.mysymptoms.DatabaseModule;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adityadevg.mysymptoms.ListOfSymptomsActivity;
import com.adityadevg.mysymptoms.PickerFragments.DatePickerFragment;
import com.adityadevg.mysymptoms.R;
import com.adityadevg.mysymptoms.SharePDFModule.GeneratePDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adityadev on 3/4/2016.
 * Activity displays a form to edit existing entry or create a new entry
 */
public class EntryDetailsActivity extends AppCompatActivity
        implements OnItemSelectedListener {
    private Toolbar toolbar;
    private DBHelper dbHelper;
    private File imgDirPath;
    private File imgFilePath;

    /**
     * Random, unique value for receive image from intent
     */
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Random, unique value for receive image from intent
     */
    static final int REQUEST_IMAGE_SELECT = 2;

    /**
     * Root of the layout of this Activity
     */
    private View linearLayoutView;

    private Bitmap bitmap;

    private TextView tv_dateStamp;
    private TextView tv_timeStamp;

    private Spinner sp_bodyPart;
    private String bodyPart;

    private EditText et_sympDesc;

    private Spinner sp_levelOfSeverity;
    private String levelOfSeverity;

    private ImageView iv_symptomImg;
    private String str_imgID;

    private Button btn_save;
    private Button btn_back;

    Bundle bundle;
    boolean isUpdate = false;

    String str_date = "", str_time = "";
    private int symptomID;

    private OutputStream fOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);
        bundle = getIntent().getExtras();

        dbHelper = new DBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayoutView = findViewById(R.id.linearLayoutView);
        iv_symptomImg = (ImageView) findViewById(R.id.img_symptom);

        tv_dateStamp = (TextView) findViewById(R.id.tv_dateOfOccurrence);
        tv_timeStamp = (TextView) findViewById(R.id.tv_timeOfOccurrence);

        sp_bodyPart = (Spinner) findViewById(R.id.spinner_bodyPart);
        sp_bodyPart.setOnItemSelectedListener(this);
        sp_bodyPart.setPrompt(getString(R.string.choose_a_body_part));

        et_sympDesc = (EditText) findViewById(R.id.editText_symptomDesc);

        sp_levelOfSeverity = (Spinner) findViewById(R.id.spinner_severityLevel);
        sp_levelOfSeverity.setOnItemSelectedListener(this);
        sp_levelOfSeverity.setPrompt(getString(R.string.choose_a_level_of_severity));

        imgDirPath = new File(GeneratePDF.IMAGE_DIR_PATH);
        if (!imgDirPath.exists()) {
            imgDirPath.mkdirs();
        }

        btn_save = (Button) findViewById(R.id.saveBtn);
        btn_back = (Button) findViewById(R.id.backBtn);

        if (null != bundle && bundle.containsKey(DBHelper.FORMAT_SYMPTOMS_DATE)) {
            str_date = bundle.getString(dbHelper.FORMAT_SYMPTOMS_DATE);
            str_time = bundle.getString(dbHelper.FORMAT_SYMPTOMS_TIME);

            tv_dateStamp.setText(str_date);
            tv_timeStamp.setText(str_time);
            sp_bodyPart.setSelection(getIndex(sp_bodyPart, bundle.getString(dbHelper.SYMPTOMS_COLUMN_BODY_PART).toString()));
            et_sympDesc.setText(bundle.getString(dbHelper.SYMPTOMS_COLUMN_SYMPTOM_DESC));
            sp_levelOfSeverity.setSelection(getIndex(sp_levelOfSeverity, bundle.getString(dbHelper.SYMPTOMS_COLUMN_LEVEL_OF_SEVERITY).toString()));
            str_imgID = (bundle.getString(dbHelper.SYMPTOMS_COLUMN_IMAGE_ID));
            iv_symptomImg.setImageURI(Uri.parse(GeneratePDF.IMAGE_DIR_PATH + File.separator + str_imgID));
            symptomID = dbHelper.getIdFromDateTime(str_date + " " + str_time);

            isUpdate = true;
        } else {
            isUpdate = false;
        }
    }

    /*
    * Returns index of selected spinner option
    * */
    private int getIndex(Spinner spinner, String selectedVal) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selectedVal)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Called when the Camera button is clicked
     */
    public void capturePicture(View view) {
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
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
     * Requests the Camera and External Storage R/W permission
     * If permission was denied previously, a SnackBar will prompt user to grant
     * permission, else permission is requested directly
     */
    private void requestCameraAndExtStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this,
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
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_IMAGE_CAPTURE);
                        }
                    })
                    .show();
        } else {
            // Camera permission has not been granted yet. Request it directly
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
        }
    }

    /*
     * Called by Camera button after required permissions are granted
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
                str_imgID = getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg";
                imgFilePath = new File(imgDirPath, str_imgID);
                switch (which) {
                    case 0:
                        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFilePath));
                        startActivityForResult(captureImageIntent, REQUEST_IMAGE_CAPTURE);
                        break;
                    case 1:
                        captureImageIntent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        captureImageIntent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(captureImageIntent, getString(R.string.select_from_existing_images)),
                                REQUEST_IMAGE_SELECT);
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
    /** To receive image from camera intent */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            try {
                switch (requestCode) {
                    case REQUEST_IMAGE_CAPTURE:
                        imageFromCamera(data);
                        break;
                    case REQUEST_IMAGE_SELECT:
                        imageFromGallery(data);
                        break;
                    default:
                        Toast.makeText(this, getString(R.string.you_may_only_click_a_new_image_or_select_an_existing_image), Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Image result from camera
     * @param data
     */
    private void imageFromCamera(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        updateImageView(bitmap);
    }

    /**
     * Image result from gallery
     * @param data
     */
    private void imageFromGallery(Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        bitmap = BitmapFactory.decodeFile(filePath);
        updateImageView(bitmap);
    }

    /**
     * Update the imageView with new bitmap
     * @param newImage
     */
    private void updateImageView(Bitmap newImage) {
        iv_symptomImg.setImageBitmap(newImage);
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
    public void modifyEntry(View entryView) {
        final AlertDialog.Builder saveEntryDialog = new AlertDialog.Builder(this);
        String date_time = tv_dateStamp.getText().toString() + " " + tv_timeStamp.getText().toString();

        if (null == str_imgID || str_imgID.trim().length() <= 0)
            str_imgID = " ";
        saveEntryDialog.setMessage(R.string.your_changes_can_be_modified_or_deleted_later)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date_time = tv_dateStamp.getText().toString() + " " + tv_timeStamp.getText().toString();
                                writeBitmapToFile();
                                if (isUpdate) {
                                    updateEntry(date_time, bodyPart, et_sympDesc.getText().toString().trim().length() == 0 ? " " : et_sympDesc.getText().toString(), levelOfSeverity, str_imgID);
                                } else {
                                    saveEntry(date_time, bodyPart, et_sympDesc.getText().toString().trim().length() == 0 ? " " : et_sympDesc.getText().toString(), levelOfSeverity, str_imgID);
                                }
                            }
                        }
                )
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }

                );

        if (!dbHelper.isExists(symptomID)|| isUpdate) {
            if (null == date_time || date_time.trim().length() <= 10) {
                Toast.makeText(getBaseContext(), getString(R.string.you_must_select_a_valid_time_of_occurrence), Toast.LENGTH_LONG).show();
            } else {
                saveEntryDialog.create();
                saveEntryDialog.show();
            }
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.a_symptom_already_exists_with_the_same_time), Toast.LENGTH_LONG).show();
        }
    }

    private void writeBitmapToFile() {
        if (null != bitmap) {
            try {
                fOut = new FileOutputStream(imgFilePath);
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        }
        if (null != fOut) {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEntry(String date_time, String bodyPart, String sympDesc, String levelOfSeverity, String str_imgID) {
        if (dbHelper.insertSymptom(date_time, bodyPart, sympDesc, levelOfSeverity, str_imgID)) {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_logged), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ListOfSymptomsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_cannot_be_logged_due_to_an_internal_database_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEntry(String date_time, String bodyPart, String sympDesc, String levelOfSeverity, String str_imgID) {
        if (dbHelper.updateSymptom(symptomID, date_time, bodyPart, sympDesc, levelOfSeverity, str_imgID)) {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_has_been_logged), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ListOfSymptomsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.your_symptom_cannot_be_logged_due_to_an_internal_database_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        dontSaveEntry(new View(this));
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
                        startActivity(new Intent(getApplicationContext(), ListOfSymptomsActivity.class)
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
    /** Stores selected value from Body Part and Level of Severity Spinners in a string */
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinner_bodyPart:
                bodyPart = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner_severityLevel:
                levelOfSeverity = parent.getItemAtPosition(position).toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
