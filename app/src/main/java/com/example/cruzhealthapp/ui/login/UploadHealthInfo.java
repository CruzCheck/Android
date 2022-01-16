package com.example.cruzhealthapp.ui.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.MasterKey;
import com.example.cruzhealthapp.R;

import android.content.Context;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDate;
import java.time.Period;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


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
                    System.out.println(readStream(in));
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }


}