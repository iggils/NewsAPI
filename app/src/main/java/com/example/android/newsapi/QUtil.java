package com.example.android.newsapi;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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


public class QUtil {
    private static final String TAG = QUtil.class.getSimpleName();

    private QUtil() {}

    public static List<NewsArticle> fetchNewsData(String requestedURL) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Log.e(TAG, "fetchNewsData: Interrupted ", ex);
        }

        //Create URL
        URL newsURL = createURL(requestedURL);

        //Perform HTTP request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(newsURL);
        } catch (IOException ioe) {
            Log.e(TAG, "fetchNewsData: Problem making HTTP request", ioe);
        }

        //Extract relevant data
        List<NewsArticle> myNews = extractNewsFromJson(jsonResponse);

        return myNews;
    }

    private static List<NewsArticle> extractNewsFromJson(String jsonResponse) {
        String title;
        String author = "";
        String date;
        String urlSource;
        //check for json if null
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<NewsArticle> myNews = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);

            JSONObject baseJSONEResponseResult = baseJSONResponse.getJSONObject("response");

            JSONArray currentNewsArticles = baseJSONEResponseResult.getJSONArray("results");
            for (int n = 0; n < currentNewsArticles.length(); n++) {
                JSONObject currentArticle = currentNewsArticles.getJSONObject(n);

                title = currentArticle.getString("webTitle");
                urlSource = currentArticle.getString("webUrl");
                date = currentArticle.getString("webPublicationDate");


                JSONArray authorArray = currentArticle.getJSONArray("tags");
                for (int au =0; au < authorArray.length(); au++)
                {
                    JSONObject currentAuthor = authorArray.getJSONObject(au);
                    author = currentAuthor.getString("webTitle");
                }
                NewsArticle newsArticle = new NewsArticle(title,author, date, urlSource );
                myNews.add(newsArticle);

            }

        } catch (JSONException je) {
            Log.e(TAG, "extractNewsFromJson: Problem Parsing results", je);
        }
        return myNews;
    }

    private static String makeHttpRequest(URL newsURL) throws IOException {
        String jsonResponse = "";
        //Check for null
        if (newsURL == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        //Create the connection
        try {
            urlConnection = (HttpURLConnection) newsURL.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(TAG, "makeHTTPRequest: Error Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException ioe){
            Log.e(TAG, "makeHTTPRequest: Couldn't retrieve json", ioe );
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


    private static URL createURL (String requestedURL){
        URL url = null;
        try {
            url = new URL(requestedURL);
        } catch (MalformedURLException mal) {
            Log.e(TAG, "createURL: Problem building URL", mal);
        }
        return url;

    }
}
