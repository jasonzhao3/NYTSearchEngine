package com.uber.yangz.nytsearchengine.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ArticlesResponse {
    @SerializedName("results")
    private List<Article> articles;

    // public constructor is necessary for collections
    public ArticlesResponse() {
        articles = new ArrayList<Article>();
    }

    public static ArticlesResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        ArticlesResponse articlesResponse = gson.fromJson(response, ArticlesResponse.class);
        return articlesResponse;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
