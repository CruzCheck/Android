package com.example.cruzhealthapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.io.BufferedInputStream;
import java.io.InputStream;


public class UploadHealthInfo extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        final Button uploadButton = findViewById(R.id.button_id);
        EditText mName = findViewById(R.id.editTextTextPersonName);
        EditText mAge = findViewById(R.id.editTextDate);
        EditText mInsuranceName = findViewById(R.id.editTextInsuranceName);
        EditText mInsuranceNum = findViewById(R.id.editTextInsuranceNum);

        uploadButton.setOnClickListener(v -> {

            //TODO: Encrypt data before sending


            final String name = mName.getText().toString();
            final String bday = mAge.getText().toString();
            final LocalDate localDate = LocalDate.parse(bday); //TODO error handling
            final int age = Period.between(localDate, LocalDate.now()).getYears();
            final String insName = mInsuranceName.getText().toString();
            final String insNum = mInsuranceNum.getText().toString();

            //send data to webserver
            try {
                String message = "name=" +
                        name +
                        "&" +
                        "age=" +
                        age +
                        "&" +
                        "insuranceName=" +
                        insName +
                        "&" +
                        "insuranceNumber=" +
                        insNum;

                URL url = new URL("http://34.135.251.234/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

                    DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                    out.writeBytes(message);

                    System.out.println(out.toString());

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if(readStream(in).equals("1")) {
                        runOnUiThread(() -> Toast.makeText(UploadHealthInfo.this,
                                "Data uploaded successfully!",
                                Toast.LENGTH_SHORT).show());
                    }

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(UploadHealthInfo.this,
                        "Error uploading data",
                        Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }

        });

    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }


}