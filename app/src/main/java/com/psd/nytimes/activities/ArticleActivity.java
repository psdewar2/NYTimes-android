package com.psd.nytimes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.psd.nytimes.R;
import com.psd.nytimes.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wvArticle = (WebView) findViewById(R.id.wvArticle);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.item_share);
        // Fetch reference to the share action provider
        ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // pass in the URL currently being used by the WebView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

        shareAction.setShareIntent(shareIntent);

        // Return true to display menu
        return true;

    }

}
