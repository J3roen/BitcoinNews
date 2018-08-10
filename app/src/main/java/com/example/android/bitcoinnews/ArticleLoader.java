package com.example.android.bitcoinnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    //Query URL
    private String url;
    //tag for log messages
    private static final String LOG_TAG = Article.class.getSimpleName();

    public ArticleLoader (Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG,"TEST: onStartLoading method called");
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        Log.d(LOG_TAG,"TEST: loadInBackground method called");
        if(this.url == null) { return null;}
        return QueryUtils.fetchArticleData(this.url);
    }
}
