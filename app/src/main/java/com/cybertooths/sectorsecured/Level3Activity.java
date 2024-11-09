package com.cybertooths.sectorsecured;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Level3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3);

        EditText keyInput = findViewById(R.id.passwordInput3);
        Button submitButton = findViewById(R.id.submitButton3);

        submitButton.setOnClickListener(v -> {
            String key = keyInput.getText().toString().trim();
            if (TextUtils.isEmpty(key)) {
                Toast.makeText(Level3Activity.this, "Please enter the key", Toast.LENGTH_SHORT).show();
                return;
            }

            String decodedKey = ConfigManager.getDecodedKey();
            if (key.equals(decodedKey)) {
                Intent intent = new Intent(Level3Activity.this, Level4Activity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Level3Activity.this, "Invalid key", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
