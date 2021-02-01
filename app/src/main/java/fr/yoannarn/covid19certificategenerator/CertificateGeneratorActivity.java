package fr.yoannarn.covid19certificategenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;

public class CertificateGeneratorActivity extends AppCompatActivity {

    private Button generate;
    private UserIdentity identity;

    private EditText nameInput;
    private EditText birthdayInput;
    private EditText addressInput;
    private EditText birthdayLocationInput;
    private RadioGroup radioGroup;
    private EditText currentLocation;
    private SharedPreferences sharedPref;
    private String certificateDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_generator);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        certificateDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Certificates/";
        File attestationDir = new File(certificateDir);
        if(!attestationDir.exists()){
            attestationDir.mkdirs();
        }

        nameInput = (EditText) findViewById(R.id.name);
        birthdayInput = (EditText) findViewById(R.id.birthday);
        addressInput = (EditText) findViewById(R.id.address);
        birthdayLocationInput = (EditText)findViewById(R.id.birthdayLocation);

        PDFBoxResourceLoader.init(getApplicationContext());
        generate = (Button) findViewById(R.id.attestation_generate_btn);
        generate.setOnClickListener(listener);

        radioGroup = findViewById(R.id.attestation_radio_group);

        currentLocation = findViewById(R.id.location);

        sharedPref = getSharedPreferences("EasyTransport", MODE_PRIVATE);

        String lastIdentity = sharedPref.getString("last_identity", null);
        if (lastIdentity != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                UserIdentity user = mapper.readValue(lastIdentity, UserIdentity.class);
                nameInput.setText(user.getName());
                birthdayInput.setText(user.getBirthday());
                addressInput.setText(user.getAddress());
                birthdayLocationInput.setText(user.getBirthPlace());
                currentLocation.setText(user.getCurrentCountry());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            try{

                UserIdentity identity = new UserIdentity(nameInput.getText().toString(), birthdayInput.getText().toString(), birthdayLocationInput.getText().toString(), addressInput.getText().toString(),currentLocation.getText().toString());

                //Save last identity
                SharedPreferences.Editor editor = sharedPref.edit();
                ObjectMapper mapper = new ObjectMapper();
                editor.putString("last_identity", mapper.writeValueAsString(identity));
                editor.apply();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButtonSelected = (RadioButton) findViewById(selectedId);
                int attestationType = radioGroup.indexOfChild(radioButtonSelected);

                CertificateBuilder.buildPDF(v.getContext(), identity, attestationType, certificateDir );

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_certificate_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_display_all_certificate:
                Intent intent = new Intent(getApplicationContext(), DisplayCertificatesActivity.class);
                intent.putExtra("DIRECTORY", certificateDir);
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;

    }

}