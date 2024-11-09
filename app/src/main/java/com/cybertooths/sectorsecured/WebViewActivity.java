package com.cybertooths.sectorsecured;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.wview);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.setWebViewClient(new WebViewClient());

        String url = getIntent().getStringExtra("inputUri");

        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        } else {
            webView.loadData("Invalid URL", "text/html", "UTF-8");
        }
    }
}
