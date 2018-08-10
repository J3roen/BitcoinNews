/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.bitcoinnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /*use one of 2 sample URL's (first one gives 0 hits)*/
    //private final String sampleUrl = "https://content.guardianapis.com/search?q=bitcoinfezrtr&lang=en&page-size=50&api-key=609fcb55-3f4d-40df-b260-4ec04f0c3cd7";
    private final String sampleUrl = "https://content.guardianapis.com/search?q=bitcoin&lang=en&page-size=50&show-tags=contributor&api-key=609fcb55-3f4d-40df-b260-4ec04f0c3cd7";
    private static final int ARTICLE_LOADER_ID = 1;
    private static final String LOG_TAG = ArticleListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(checkConnection())
            super.onCreate(savedInstanceState);
        else
            super.onCreate(null);
        //check internet connection, if false -> set SwipeRefreshLayout in error_message.xml
        setContentView();
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;
        else
            return false;
    }

    private void setContentView (final boolean hasArticles) {
        if (checkConnection() && (!hasArticles)) {
            setErrorLayout(getResources().getString(R.string.no_articles_found));
        } else {
            setContentView();
        }
    }

    private void setContentView() {
        //if isConnected -> set activity_article_list.xml layout and continue app
        if (checkConnection()) {
            setListLayout();
        } else {
            setErrorLayout(getResources().getString(R.string.no_connection));
        }
    }

    private void setErrorLayout(String message) {
        //if no internet connection -> set error_message.xml, no further action + refresh listener
        setContentView(R.layout.error_message);
        TextView error = findViewById(R.id.errorMessage);
        error.setText(message);
        SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //on refresh, check internet connection and set contentView accordingly
                        setContentView();
                    }
                }
        );
    }

    private void setListLayout() {
        setContentView(R.layout.activity_article_list);
        //create Loadermanager to manage AsyncTaskLoader to fetch earthquakes from url
        LoaderManager loaderManager = getLoaderManager();
        Log.d(LOG_TAG, "TEST: LoaderManager.initLoader called");
        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "TEST: onCreateLoader method called");
        ArticleLoader articleLoader = new ArticleLoader(this.getApplicationContext(), sampleUrl);
        return articleLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articleList) {
        Log.d(LOG_TAG, "TEST: onLoadFinished method called");

        //if articleList == 0 (no results) -> show TextView saying no results found
        if (articleList.size() == 0) {
            setContentView(false);
        }
        else { //if articleList > 0, set adapter
            // Find a reference to the {@link RecyclerView} in the layout
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
            //use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            // Create & set custom adapter
            mAdapter = new ArticleListAdapter((ArrayList) articleList, this);
            mRecyclerView.setAdapter(mAdapter);}
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.d(LOG_TAG, "TEST: onLoaderReset method called");
        mAdapter = null;
    }
}
