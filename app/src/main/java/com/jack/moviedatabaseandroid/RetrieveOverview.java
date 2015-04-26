package com.jack.moviedatabaseandroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class RetrieveOverview {

    private static final String TAG = RetrieveOverview.class.getSimpleName();

    private static String url;
    private static String plot;



    public static final String retrieveOverview(Context context, String name) {

        generateURL(name);

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Iterator<String> keys = response.keys();

                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                String value = response.getString(key);

                                if(key.equalsIgnoreCase("Plot")){
                                    plot = value;
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VolleyError: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
        return plot;
    }

    private static void generateURL(String title){
        String[] split = title.split(" ");
        String beg = "http://www.omdbapi.com/?t=";
        String end = "&y=&plot=short&r=json";
        String movie = "";
        for(int i = 0; i < split.length - 1; i++) {
            movie += split[i] + "+";
        }
        movie += split[split.length - 1];
        String finished = beg + movie + end;
        url = finished;
    }
}