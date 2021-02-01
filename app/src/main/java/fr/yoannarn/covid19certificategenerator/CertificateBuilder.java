package fr.yoannarn.covid19certificategenerator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentCatalog;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDAcroForm;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDCheckbox;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDField;
import com.tom_roush.pdfbox.pdmodel.interactive.form.PDTextField;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;

public class CertificateBuilder {

    public static void buildPDF(Context context, UserIdentity identity, int type, String dirDest) throws IOException {
        PDFBoxResourceLoader.init(context);
        PDDocument document = PDDocument.load(context.getResources().openRawResource(R.raw.certificate));
        PDDocumentCatalog docCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();


        //set Type of attestation
        try {
            ((PDCheckbox) acroForm.getField(String.valueOf(type+1))).check();
        } catch (Exception e) {
            Log.d("AttestationBuilder", "Erreur lors de l'insertion du type d'attestation");
        }


        for(PDField field :acroForm.getFields()){

            switch (field.getPartialName()){
                case "name":
                    ((PDTextField) field).setValue(identity.getName());
                    break;
                case "address":
                    ((PDTextField) field).setValue(identity.getAddress());
                    break;
                case "birthday":
                    ((PDTextField) field).setValue(identity.getBirthday());
                    break;
                case "birthdayLocation":
                    ((PDTextField) field).setValue(identity.getBirthPlace());
                    break;
                case "location":
                    ((PDTextField) field).setValue(identity.getCurrentCountry());
                    break;
                case "time":
                    String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                    ((PDTextField) field).setValue(time);
                    break;
                case "date":
                    String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                    ((PDTextField) field).setValue(date);
                    break;

            }
            //Lock pdf field
            field.setReadOnly(true);
        }


        String timestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        File destFile =  new File(dirDest,"certificate"+ timestamp +".pdf");
        document.save(destFile);
        document.close();

        openPDF(context, destFile);
    }


    // Function which allows to open the given pdf file
    public static void openPDF(Context context, File pdfFile) throws IOException {

        Uri UriPath;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            UriPath = FileProvider.getUriForFile(context,  "fr.yoannarn.covid19certificategenerator.fileprovider", pdfFile);
        } else {
            UriPath = Uri.fromFile(pdfFile);
        }
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(UriPath, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try{
            context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(context, "No PDF viewer available", Toast.LENGTH_SHORT).show();
        }
    }

}
