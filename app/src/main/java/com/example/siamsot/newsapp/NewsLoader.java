package com.example.siamsot.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String LOG_TAG = NewsLoader.class.getName();
    private String mQueryUrl;

    NewsLoader(Context context, String url) {
        super(context);
        mQueryUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        String urlToFetch = mQueryUrl;
        // get JSON_Response_String from the urlToFetch
        URL url = Utils.createURL(urlToFetch);
        String JSON_RESPONSE = Utils.fetchJSON(url);
        // get News arraylist to display in listview
        List<News> newsList = Utils.extractNews(JSON_RESPONSE);
        return newsList;
    }
}
