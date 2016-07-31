package com.uber.yangz.nytsearchengine.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ArticleClient {
    private static final String API_KEY_NAME = "api-key";
    private static final String API_KEY_VALUE = "9046e3be75ca448883a72b60fdca1c54";
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private AsyncHttpClient client;

    public ArticleClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getArticles(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("search.json?q=");
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

