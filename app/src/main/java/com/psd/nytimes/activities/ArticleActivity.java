package com.psd.nytimes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.psd.nytimes.R;
import com.psd.nytimes.models.Article;

public class ArticleActivity extends AppCompatActivity {
    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wvArticle = (WebView) findViewById(R.id.wvArticle);
        Article article = (Article) getIntent().getSerializableExtra("article");

        wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //can't just loadUrl to house the webview itself
        wvArticle.loadUrl(article.getWebUrl());
    }

}
