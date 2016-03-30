package com.adityadevg.mysymptoms.SharePDFModule;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import com.adityadevg.mysymptoms.DatabaseModule.DBHelper;
import com.adityadevg.mysymptoms.DatabaseModule.SymptomDetailsDTO;
import com.adityadevg.mysymptoms.ProfileActivity;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneratePDF {

    public static final String APP_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MySymptoms";
    public static final String IMAGE_DIR_PATH = GeneratePDF.APP_DIR_PATH + File.separator + "Symptom Images";

    private static final String DATE = "Date";
    private static final String TIME = "Time";
    private static final String BODY_PART = "Body Part";
    private static final String SYMPTOM_DESCRIPTION = "Symptom Description";
    private static final String LEVEL_OF_SEVERITY = "Level of Severity";

    public static Uri createPDF(DBHelper dbHelper, SharedPreferences sharedPreferences) {
        Document doc = new Document(PageSize.A4);
        File directoryPath = new File(APP_DIR_PATH);
        String pdfFileName = sharedPreferences.getString(ProfileActivity.keyPatientName, "").split(" ")[0]
                + "_"
                + System.currentTimeMillis()
                + ".pdf";
        File newPdfFile = new File(directoryPath, pdfFileName);

        List<SymptomDetailsDTO> listOfSymptomDetails = dbHelper.getListOfAllSymptomDetails();

        PdfWriter writer;

        try {
            if (!directoryPath.exists())
                directoryPath.mkdirs();

            writer = PdfWriter.getInstance(doc, new FileOutputStream(newPdfFile));
            writer.setPageEvent(new MyFooter(sharedPreferences));

            doc.open();
            doc.add(new Paragraph(" "));
            doc.add(getSymptomDetailsTable(listOfSymptomDetails));
            doc.newPage();
            getSymptomsImages(listOfSymptomDetails, writer, doc);

/*
            Paragraph p1 = new Paragraph("Hi! I am generating my first PDF");
            Font paraFont = new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            doc.add(p1);


            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            Font paraFont2 = new Font(Font.FontFamily.COURIER, 14.0f, Color.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            doc.add(p2);
*/

/*
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.test);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);


            //add image to document
            doc.add(myImg);
*/


        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            doc.close();
        }
        return Uri.fromFile(newPdfFile);
    }

    private static void getSymptomsImages(List<SymptomDetailsDTO> listOfSymptomDetails, PdfWriter writer, Document doc) {
        Image img;
        for (SymptomDetailsDTO symptomDetailsObj : listOfSymptomDetails) {
            try {

                img = Image.getInstance(IMAGE_DIR_PATH + File.separator + symptomDetailsObj.getPictureID());
                img.scaleToFit(PageSize.A4.getWidth() * 3 / 4, PageSize.A4.getHeight() * 3 / 4);
                doc.newPage();
                doc.add(new Paragraph(" "));
                String tempDateTime = symptomDetailsObj.getdateTimeStamp();
                if (tempDateTime.trim().length() > 0) {
                    doc.add(new Paragraph("Date: " + tempDateTime.split(" ")[0]));
                    doc.add(new Paragraph("Time: " + tempDateTime.split(" ")[1].substring(0, tempDateTime.split(" ")[1].length() - 3)));
                }
                doc.add(new Paragraph("Body Part: " + symptomDetailsObj.getBodyPart()));
                doc.add(new Paragraph("Severity Level: " + symptomDetailsObj.getLevelOfSecurity()));
                img.setAbsolutePosition(0, doc.bottom() + Font.DEFAULTSIZE * 2);
                doc.add(img);

            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    private static PdfPTable getSymptomDetailsTable(List<SymptomDetailsDTO> listOfSymptomDetails) {
        PdfPTable pdfTable = new PdfPTable(5);
        Font colHeadFont = new Font(Font.FontFamily.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD);

        pdfTable.addCell(new PdfPCell(new Phrase(DATE, colHeadFont)));
        pdfTable.addCell(new PdfPCell(new Phrase(TIME, colHeadFont)));
        pdfTable.addCell(new PdfPCell(new Phrase(BODY_PART, colHeadFont)));
        pdfTable.addCell(new PdfPCell(new Phrase(SYMPTOM_DESCRIPTION, colHeadFont)));
        pdfTable.addCell(new PdfPCell(new Phrase(LEVEL_OF_SEVERITY, colHeadFont)));

        for (SymptomDetailsDTO symptomDetailsObj : listOfSymptomDetails) {

            if (symptomDetailsObj.getdateTimeStamp().trim().length() > 0) {
                // Display date value from datetimestamp string
                pdfTable.addCell(symptomDetailsObj.getdateTimeStamp().split(" ")[0]);
                // Display time value from datetimestamp string
                // And eliminate seconds value from time stamp
                pdfTable.addCell(symptomDetailsObj.getdateTimeStamp().split(" ")[1].substring(0, symptomDetailsObj.getdateTimeStamp().split(" ")[1].length() - 3));
            }


            pdfTable.addCell(symptomDetailsObj.getBodyPart());
            pdfTable.addCell(symptomDetailsObj.getSymptomDesc());
            pdfTable.addCell(symptomDetailsObj.getLevelOfSecurity());
        }
        return pdfTable;
    }


    static class MyFooter extends PdfPageEventHelper {
        Font headerFont = new Font(Font.FontFamily.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD);
        Font footerFont = new Font(Font.FontFamily.UNDEFINED, Font.DEFAULTSIZE, Font.ITALIC);

        SharedPreferences profileData;
        String str_patientName;
        String str_patientEmail;
        String str_patientPhone;
        String str_insuranceName;
        String str_memberID;
        String str_emergencyName;
        String str_emergencyPhone;
        private static final String phoneTAG = "Phone: ";
        private static final String emergencyContactTAG = "Emergency Contact: ";
        private static final String insuranceCompanyTAG = "Insurance Company: ";
        private static final String memberIdTAG = "Member ID: ";

        public MyFooter(SharedPreferences profileData) {
            this.profileData = profileData;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();

            str_patientName = profileData.getString(ProfileActivity.keyPatientName, "");
            str_patientEmail = profileData.getString(ProfileActivity.keyPatientEmail, "");
            str_patientPhone = profileData.getString(ProfileActivity.keyPatientPhone, "");
            str_insuranceName = profileData.getString(ProfileActivity.keyInsuranceName, "");
            str_memberID = profileData.getString(ProfileActivity.keyMemberID, "");
            str_emergencyName = profileData.getString(ProfileActivity.keyEmergencyName, "");
            str_emergencyPhone = profileData.getString(ProfileActivity.keyEmergencyPhone, "");

            Phrase headerPatientName = new Phrase(str_patientName.toUpperCase(), headerFont);
            Phrase headerPatientEmail = new Phrase(str_patientEmail, headerFont);
            Phrase headerPatientPhone = new Phrase(phoneTAG + str_patientPhone, headerFont);

            Phrase headerEmergencyName = new Phrase(emergencyContactTAG + str_emergencyName, footerFont);
            Phrase headerEmergencyPhone = new Phrase(phoneTAG + str_emergencyPhone, footerFont);

            Phrase footerInsuranceName = new Phrase(insuranceCompanyTAG + str_insuranceName, footerFont);
            Phrase footerMemberID = new Phrase(memberIdTAG + str_memberID, footerFont);

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    headerPatientName,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + Font.DEFAULTSIZE + Font.DEFAULTSIZE, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    headerPatientEmail,
                    document.leftMargin(),
                    document.top() + Font.DEFAULTSIZE, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    headerPatientPhone,
                    document.leftMargin(),
                    document.top(), 0);

            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    headerEmergencyName,
                    document.right(),
                    document.top() + Font.DEFAULTSIZE, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    headerEmergencyPhone,
                    document.right(),
                    document.top(), 0);


            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footerInsuranceName,
                    document.leftMargin(),
                    document.bottom(), 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footerMemberID,
                    document.right(),
                    document.bottom(), 0);
        }
    }
}


