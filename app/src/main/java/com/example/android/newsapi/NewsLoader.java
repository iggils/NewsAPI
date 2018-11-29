package com.example.android.newsapi;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsArticle>> {
    public  static  final String LOG_TAG = NewsArticle.class.getName();

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsArticle> newsArticles = QUtil.fetchNewsData(mUrl);
        return newsArticles;
    }
}
