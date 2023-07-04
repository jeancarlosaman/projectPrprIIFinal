package com.example.projectprprii.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class SessionManager {
    private static final String SESSION_TOKEN_KEY = "accessToken";
    private static final String API_URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

    private SharedPreferences preferences;
    private OkHttpClient httpClient;

    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        httpClient = new OkHttpClient();
    }

    public void saveSessionToken(String sessionToken) {
        preferences.edit().putString(SESSION_TOKEN_KEY, sessionToken).apply();
    }

    public String getSessionToken() {
        return preferences.getString(SESSION_TOKEN_KEY, null);
    }

    public void clearSessionToken() {
        preferences.edit().remove(SESSION_TOKEN_KEY).apply();
    }

    public void fetchAndStoreUserInfo(final FetchUserInfoCallback callback) {
        String sessionToken = getSessionToken();
        if (sessionToken != null) {
            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + sessionToken)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Failed to fetch user information");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int statusCode = response.code();
                    if (statusCode == 200) {
                        try {
                            String responseBody = response.body().string();
                            JSONArray usersArray = new JSONArray(responseBody);
                            if (usersArray.length() > 0) {
                                JSONObject user = usersArray.getJSONObject(0);
                                String userName = user.getString("name");
                                String email = user.getString("email");

                                preferences.edit()
                                        .putString("userName", userName)
                                        .putString("email", email)
                                        .apply();

                                callback.onSuccess();
                            } else {
                                callback.onError("No users found");
                            }
                        } catch (JSONException e) {
                            callback.onError("Failed to parse user information");
                        }
                    } else if (statusCode == 401) {
                        callback.onError("Unauthorized: Invalid token");
                    } else {
                        callback.onError("Error fetching user information");
                    }
                }
            });
        } else {
            callback.onError("Session token not available");
        }
    }







    public interface FetchUserInfoCallback {
        void onSuccess();

        void onError(String errorMessage);
    }
}
