package com.uber.yangz.nytsearchengine.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.uber.yangz.nytsearchengine.R;

public class ArticleDetailActivity extends AppCompatActivity {

    private WebView articleWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        setupWebview();

        String url = getIntent().getStringExtra("url");
        // Load the initial URL
        articleWebview.loadUrl(url);
    }


    private void setupWebview() {
        articleWebview = (WebView) findViewById(R.id.article_webview);
        // Configure related browser settings
        articleWebview.getSettings().setLoadsImagesAutomatically(true);
        articleWebview.getSettings().setJavaScriptEnabled(true);

        // view port configure
        articleWebview.getSettings().setUseWideViewPort(true);
        articleWebview.getSettings().setLoadWithOverviewMode(true);

        articleWebview.getSettings().setSupportZoom(true);
        articleWebview.getSettings().setBuiltInZoomControls(true); // allow pinch to zooom
        articleWebview.getSettings().setDisplayZoomControls(false); // disable the default zoom controls on the page

        articleWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        articleWebview.setWebViewClient(new ArticleBrowser());
    }

    private class ArticleBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
