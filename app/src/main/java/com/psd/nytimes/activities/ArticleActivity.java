package com.psd.nytimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.psd.nytimes.R;
import com.psd.nytimes.databinding.ActivityArticleBinding;
import com.psd.nytimes.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {
    private ActivityArticleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);
        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));
        setSupportActionBar(binding.toolbar);

        binding.wvArticle.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //can't just loadUrl to house the webview itself
        binding.wvArticle.loadUrl(article.getWebUrl());
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, binding.wvArticle.getUrl());

        shareAction.setShareIntent(shareIntent);

        // Return true to display menu
        return true;

    }

}
