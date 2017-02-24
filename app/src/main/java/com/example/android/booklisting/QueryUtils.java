package com.example.android.booklisting;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Books> extractFeatureFromJson(String bookJSON)  {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }


        ArrayList<Books> books = new ArrayList<Books>();

        try {
            JSONObject response = new JSONObject(bookJSON);
            JSONArray bookArray = response.getJSONArray("items");
            for (int i = 0; i < bookArray.length();i++){
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo= currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                String url = volumeInfo.getString("infoLink");

                String author="";

                if (volumeInfo.has("authors")){
                    JSONArray authorArray = volumeInfo.getJSONArray("authors");
                    for(int j=0;j<authorArray.length();j++){
                        author += authorArray.getString(j);
                        author += ", ";
                    }
                }

                Books book = new Books(i+1,title,author,url);
                books.add(book);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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

    public static List<Books> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Books> bookses = extractFeatureFromJson(jsonResponse);

        return bookses;
    }
}
