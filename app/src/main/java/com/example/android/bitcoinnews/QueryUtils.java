package com.example.android.bitcoinnews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving article data from Guardian.
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    //fetch article data from url & return data list
    public static List<Article> fetchArticleData(String url) {
        Log.d(LOG_TAG, "TEST: fetchArticleData method called");
        //get JSONResponse from param url
        String JSONResponse = getJSONResponse(url);
        //get article list from JSONResponse
        List<Article> articles = extractArticles(JSONResponse);
        return articles;
    }

    private static String getJSONResponse(String stringUrl) {
        //create URL using inner method
        URL url = createURL(stringUrl);

        //perform HTTP request to the URL and receive JSON back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream");
        }
        return jsonResponse;
    }

    //inner method for creating URL object from String
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url");
        }
        return url;
    }

    //inner method for performing HTTP request & returning JSON as String
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if url is null, return early
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //inner class for parsing inputstream to jsonResponse, used in makeHTTPRequest method
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Article> extractArticles(String JSONResponse) {

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the JSONResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject response = new JSONObject(JSONResponse);
            JSONObject object = response.getJSONObject("response");
            JSONArray array = object.getJSONArray("results");
            String header;
            String body;
            String section;
            String datePublished = null;
            String author;
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                header = object.getString("webTitle");
                body = object.getString("webUrl");
                section = object.getString("sectionName");
                if (object.getString("webPublicationDate") != null)
                    datePublished = object.getString("webPublicationDate");
                JSONArray tagArray = object.getJSONArray("tags");
                object = tagArray.getJSONObject(0);
                author = object.getString("webTitle");
                articles.add(new Article(header, body, section, datePublished, author));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Something went wrong in extractArticles method: " + e.toString());
        }

        // Return the list of articles
        return articles;
    }

}