package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DELL on 16-02-2017.
 */

public class QueryUtils {

    private static final String SAMPLE_JSON_RESPONSE

    private QueryUtils() {
    }

    public static ArrayList<Books> extractFeatureFromJson(String earthquakeJSON)  {

        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }


        ArrayList<Books> earthquakes = new ArrayList<Books>();

        try {
            JSONObject response = new JSONObject(earthquakeJSON);
            JSONArray eqArray = response.getJSONArray("features");
            for (int i = 0; i < eqArray.length();i++){
                JSONObject currentEq = eqArray.getJSONObject(i);
                JSONObject properties = currentEq.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                DecimalFormat magnitudeFormatter = new DecimalFormat("0.00");
                String magnitudeToDisplay = magnitudeFormatter.format(magnitude);

                String location = properties.getString("place");

                long time = properties.getLong("time");

                //sets date
                Date dateObject = new Date(time);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM ,DD, yyyy");
                String dateToDisplay = dateFormatter.format(dateObject);

                //sets time
                Time timeObject = new Time(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                String timeToDisplay = timeFormat.format(timeObject);

                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, dateToDisplay,timeToDisplay,url);
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }
}
