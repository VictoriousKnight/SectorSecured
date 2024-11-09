package com.cybertooths.sectorsecured;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class Level4Activity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level4);

        usernameEditText = findViewById(R.id.usernameInput4);
        passwordEditText = findViewById(R.id.passwordInput4);
        loginButton = findViewById(R.id.loginButton4);

        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            String storedUsername = databaseHelper.authenticateUser(username, password);

            if (storedUsername != null) {
                if ("PhantomHack".equals(storedUsername)) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Level4Activity.this, Level5Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Level4Activity.this, ResultActivity.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
