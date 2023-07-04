package com.example.projectprprii.Activity;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;


public class VolleySingletone {
    private static VolleySingletone mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingletone(Context context) {
        mRequestQueue = (RequestQueue) Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingletone getInstance(Context context) {
        return mInstance == null ? new VolleySingletone(context) : mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void addToRequestQueue(Request request) {
        mRequestQueue.add(request);
    }
}
