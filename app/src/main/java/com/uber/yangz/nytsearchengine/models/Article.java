package com.uber.yangz.nytsearchengine.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article {
    private String webUrl;
    private String headline;
    private String thumbNailUrl;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNailUrl = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbNailUrl = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                articles.add(new Article(array.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }
}
