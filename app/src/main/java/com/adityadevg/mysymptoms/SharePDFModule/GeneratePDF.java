package com.adityadevg.mysymptoms.SharePDFModule;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import com.adityadevg.mysymptoms.DatabaseModule.DBHelper;
import com.adityadevg.mysymptoms.DatabaseModule.SymptomDetailsDTO;
import com.adityadevg.mysymptoms.ProfileActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneratePDF {

    public static Uri createPDF(DBHelper dbHelper, SharedPreferences sharedPreferences) {
        Document doc = new Document(PageSize.A4);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MySymptoms";
        File directoryPath = new File(path);
        File file = new File(directoryPath, "sample.pdf");

        List<SymptomDetailsDTO> listOfSymptomDetails = dbHelper.getListOfAllSymptomDetails();

        PdfWriter writer;

        try {
            if (!directoryPath.exists())
                directoryPath.mkdirs();

            writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
            writer.setPageEvent(new MyFooter(sharedPreferences));

            doc.open();

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
        return Uri.fromFile(file);
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

            Phrase headerPatientName = new Phrase(str_patientName, headerFont);
            Phrase headerPatientEmail = new Phrase(str_patientEmail, headerFont);
            Phrase headerPatientPhone = new Phrase(str_patientPhone, headerFont);

            Phrase headerEmergencyName = new Phrase(str_emergencyName, footerFont);
            Phrase headerEmergencyPhone = new Phrase(str_emergencyPhone, footerFont);

            Phrase footerInsuranceName = new Phrase(str_insuranceName, footerFont);
            Phrase footerMemberID = new Phrase(str_memberID, footerFont);

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


