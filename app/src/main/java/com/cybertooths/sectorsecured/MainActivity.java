package com.cybertooths.sectorsecured;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String password = passwordInput.getText().toString().trim();

        if (password.equals(getHardcodedPassword())) {
            Intent intent = new Intent(MainActivity.this, Level2Activity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "I think you are not worthy for this task.", Toast.LENGTH_SHORT).show();
        }
    }

    // Static method to return the hardcoded password
    private static String getHardcodedPassword() {
        return "CtrlAltDefe4t!";  // Hardcoded password
    }
}
