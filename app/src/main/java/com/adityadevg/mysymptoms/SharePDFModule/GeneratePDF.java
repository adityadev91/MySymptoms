package com.adityadevg.mysymptoms.SharePDFModule;

import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneratePDF {

    public static Uri createPDF() {
        Document doc = new Document();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MySymptoms";
        File dir = new File(path);
        File file = new File(dir, "sample.pdf");
        FileOutputStream fOut;

        try {

            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

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

            //set footer
            Phrase footerText = new Phrase("This is an example of a footer");
            /*HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
            doc.setFooter(pdfFooter);
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
}


