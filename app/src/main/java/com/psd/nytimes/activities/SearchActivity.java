package com.psd.nytimes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.psd.nytimes.EndlessRecyclerViewScrollListener;
import com.psd.nytimes.R;
import com.psd.nytimes.SettingsFragment;
import com.psd.nytimes.adapters.ArticleAdapter;
import com.psd.nytimes.models.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;
    RecyclerView rvArticles;
    String mainQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        articles = new ArrayList<>();

        //setting up RecyclerView with adapter
        articleAdapter = new ArticleAdapter(this, articles);
        final StaggeredGridLayoutManager glm = new StaggeredGridLayoutManager(3, 1);
        rvArticles.setLayoutManager(glm);
        rvArticles.setHasFixedSize(true);
        rvArticles.setAdapter(articleAdapter);
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(glm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchArticles(mainQuery, page);
            }
        });
        articleAdapter.setOnItemClickListener((itemView, position) -> {
            Intent i = new Intent(SearchActivity.this, ArticleActivity.class);
            Article article = articles.get(position);
            i.putExtra("article", Parcels.wrap(article));
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // get menu items
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here by fetching the data remotely
                mainQuery = query;
                articles.clear();
                articleAdapter.notifyDataSetChanged();
                fetchArticles(mainQuery, 0);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getSupportFragmentManager();
            SettingsFragment sf = SettingsFragment.newInstance("Settings");
            sf.show(fm, "fragment_settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchArticles(String query, int page) {
        if (isNetworkAvailable() && isOnline()) {
            AsyncHttpClient client = new AsyncHttpClient();
            //String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=2b2963c8aeed4486a0e235eb43dc5160";
            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
            RequestParams params = new RequestParams();
            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            String beginDate = sharedPreferences.getString("begin_date", "");
            String sortOrder = sharedPreferences.getString("sort", "");
            boolean artsChecked = sharedPreferences.getBoolean("cbArtsChecked", false);
            boolean fashionChecked = sharedPreferences.getBoolean("cbFashionChecked", false);
            boolean sportsChecked = sharedPreferences.getBoolean("cbSportsChecked", false);

            if (!beginDate.equals("")) params.put("begin_date", beginDate);
            if (!sortOrder.equals("")) params.put("sort", sortOrder);
            StringBuilder newsDeskValues = new StringBuilder();
            int count = 0;
            if(artsChecked) {
                newsDeskValues.append("\"Arts\"");
                count++;
            }
            if(fashionChecked) {
                newsDeskValues.append("\"Fashion & Style\"");
                count++;
            }
            if(sportsChecked) {
                newsDeskValues.append("\"Sports\"");
                count++;
            }

            if (count > 0) params.put("fq", "news_desk:("+newsDeskValues+")");

            params.put("api-key", "2b2963c8aeed4486a0e235eb43dc5160");
            params.put("page", page);
            params.put("q", query);

            Log.d("PARAMS", params.toString());
            client.get(url, params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleResults;
                    try {
                        articleResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJSONArray(articleResults));
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getApplicationContext(), "Error " + statusCode + ": Unable to look for articles. Check your internet connection", Toast.LENGTH_LONG).show();
                }

            });
        } else {
            Toast.makeText(this, "Unable to look for " +  query + " articles. Check your internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}
