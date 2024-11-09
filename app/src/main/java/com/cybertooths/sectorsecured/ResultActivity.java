package com.cybertooths.sectorsecured;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView trustMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        trustMessageTextView = findViewById(R.id.trustMessageTextView);
        trustMessageTextView.setText("Nice try, but we trust you.");

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(ResultActivity.this, Level4Activity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
