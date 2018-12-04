package com.example.janez.datadisplay;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by janez on 26/11/2017.
 */

public class JSONUtils {
    public static JSONArray parseData(String jsonString) {
        try {
            JSONObject rootObj = new JSONObject(jsonString);
            JSONArray jsonResults = rootObj.getJSONArray("fixtures");
            JSONArray resultArray = new JSONArray();
            for (int i = jsonResults.length() - 1; i >= 0; i--) {
                if (jsonResults.getJSONObject(i).getString("status").equals("FINISHED") || jsonResults.getJSONObject(i).getString("status").equals("IN_PLAY"))
                    resultArray.put(jsonResults.getJSONObject(i));
            }

            return resultArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
