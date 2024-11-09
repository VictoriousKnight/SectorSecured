package com.cybertooths.sectorsecured;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Level6Activity extends AppCompatActivity {

    private Button decryptButton;
    private TextView decryptedOutputTextView;
    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level6);

        decryptButton = findViewById(R.id.decryptButton);
        decryptedOutputTextView = findViewById(R.id.decryptedOutputTextView);
        infoTextView = findViewById(R.id.infoTextView6);

        decryptButton.setOnClickListener(v -> executeDecryption());
    }

    private void executeDecryption() {
        String encryptedData = getResources().getString(R.string.final_task);
        String decryptedOutput = decryptData(encryptedData);
        decryptedOutputTextView.setText(decryptedOutput);
    }

    private String decryptData(String encryptedData) {
        StringBuilder decryptedData = new StringBuilder();
        int shift = 13;

        for (int i = 0; i < encryptedData.length(); i++) {
            char c = encryptedData.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                decryptedData.append((char) (c - shift));
            } else if (c >= 'a' && c <= 'z') {
                decryptedData.append((char) (c - shift));
            } else {
                decryptedData.append(c);
            }
        }

        return decryptedData.toString();
    }
}
