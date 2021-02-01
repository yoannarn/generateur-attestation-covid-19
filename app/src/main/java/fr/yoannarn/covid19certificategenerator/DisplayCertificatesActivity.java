package fr.yoannarn.covid19certificategenerator;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DisplayCertificatesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_certificate);

        recyclerView = findViewById(R.id.pdfFile_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String directory = getIntent().getStringExtra("DIRECTORY");


        File dir = new File(directory);
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".pdf");
            }
        });

        if(files.length != 0){
            List<File> pdfFiles = Arrays.asList(files);
            Collections.reverse(pdfFiles);
            recyclerView.setAdapter(new CertificateListAdapter(pdfFiles));
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Aucune attestation disponible",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}