package com.example.janez.datadisplay;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Created by janez on 25/11/2017.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "http://api.football-data.org/v1/teams/61/fixtures";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Request URL: " + url);
        return url;
    }

    public static String getData(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Log.v(TAG, "PRINTING");
            Log.e(TAG, in.toString());
            String resultString = convertStreamToString(in);
            Log.e(TAG, resultString);
            return resultString;
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String convertStreamToString(InputStream inputStream) {
        try {
            return new java.util.Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}
