package com.uber.yangz.nytsearchengine.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uber.yangz.nytsearchengine.R;
import com.uber.yangz.nytsearchengine.adapters.ArticlesAdaptor;
import com.uber.yangz.nytsearchengine.fragments.FilterDialogFragment;
import com.uber.yangz.nytsearchengine.lib.EndlessScrollListener;
import com.uber.yangz.nytsearchengine.models.Article;
import com.uber.yangz.nytsearchengine.models.FilterSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

    // TODO: refactor this into ArticleClient
    private static final String API_KEY_NAME = "api-key";
    private static final String API_KEY_VALUE = "9046e3be75ca448883a72b60fdca1c54";
    private static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private ArticlesAdaptor adapter;
    private FilterSetting filterSetting;
    private String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        filterSetting = new FilterSetting();
        setupArticleListWithAdapter();
    }

    public void onFilterAction(MenuItem mi) {
        // inflate the filter fragment
        Log.d("DEBUG", mi.toString());
        showFilterDialog();
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Edit Filter");
        filterDialogFragment.show(fm, "fragment_filter");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                SearchActivity.this.query = query;
                fetchArticles(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchArticles(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(API_KEY_NAME, API_KEY_VALUE);
        params.put("page", page);
        params.put("q", query);
        populateFilterParams(params);

        final boolean needClearAdapter = page == 0;

        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                        loadArticles(adapter, res, needClearAdapter);
                        // TODO: dismiss spinner after fetching articles
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Log.d("DEBUG", "Fetch articles error: " + t.toString());

                    }
                }
        );
    }

    private void setupArticleListWithAdapter() {
        ArrayList<Article> articles = new ArrayList<>();
        adapter = new ArticlesAdaptor(this, articles);
        GridView gvArticles = (GridView) findViewById(R.id.gv_articles);
        gvArticles.setAdapter(adapter);

        gvArticles.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                fetchArticles(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    private void loadArticles(ArticlesAdaptor adapter, JSONObject response, boolean needClearAdapter) {
        try {
            JSONArray articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
            if (needClearAdapter) {
                adapter.clear();
            }
            adapter.addAll(Article.fromJSONArray(articleJsonResults));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateFilterParams(RequestParams params) {
        if (!TextUtils.isEmpty(filterSetting.getBeginDateStr())) {
            params.put("begin_date", filterSetting.getBeginDateStr());
        }

        if (!TextUtils.isEmpty(filterSetting.getSortOrder())) {
            params.put("sort", filterSetting.getSortOrder());
        }

        if (!TextUtils.isEmpty(filterSetting.getDeskValuesStr())) {
            params.put("fq", filterSetting.getDeskValuesStr());
        }
    }

    @Override
    public void onFinishFilterDialog(FilterSetting filterSetting) {
        Log.d("DEBUG", "finish filter dialog setting");
        this.filterSetting = filterSetting;
    }
}
