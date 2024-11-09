package com.cybertooths.sectorsecured;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Level5Activity extends AppCompatActivity {

    private EditText uriInput;
    private EditText passcodeInput;
    private Button verifyPasscodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level5);

        uriInput = findViewById(R.id.uriInput5);
        passcodeInput = findViewById(R.id.protocolPasscodeInput);
        verifyPasscodeButton = findViewById(R.id.verifyPasscodeButton);

        verifyPasscodeButton.setOnClickListener(v -> verifyPasscode(v));

        copyEmailToCache();
    }

    private void copyEmailToCache() {
        AssetManager assetManager = getAssets();
        File cacheFile = new File(getCacheDir(), "email");

        try (InputStream in = assetManager.open("email");
             FileOutputStream out = new FileOutputStream(cacheFile)) {

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            Toast.makeText(this, "File copied to cache successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Failed to copy file to cache.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void loadContent(View view) {
        String inputUri = uriInput.getText().toString().trim();
        if (!inputUri.isEmpty()) {
            Intent intent = new Intent(Level5Activity.this, WebViewActivity.class);
            intent.putExtra("inputUri", inputUri);
            startActivity(intent);
        } else {
            uriInput.setError("Please enter a valid URL or file path.");
        }
    }
    public void verifyPasscode(View view) {
        String enteredPasscode = passcodeInput.getText().toString().trim();
        if (TextUtils.isEmpty(enteredPasscode)) {
            passcodeInput.setError("Please enter the passcode.");
            return;
        }
        if (isValidPasscode(enteredPasscode)) {
            uriInput.setEnabled(true);
            findViewById(R.id.loadButton5).setEnabled(true);
            passcodeInput.setText("");
            Toast.makeText(this, "Passcode verified successfully.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Level5Activity.this, Level6Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid passcode.", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isValidPasscode(String enteredPasscode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(enteredPasscode.getBytes());
            String hashedPasscode = bytesToHex(hashedBytes);

            String storedPasscode = getString(R.string.protocol_passcode);
            return hashedPasscode.equals(storedPasscode);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
