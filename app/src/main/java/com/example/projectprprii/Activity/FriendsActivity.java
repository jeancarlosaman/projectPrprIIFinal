package com.example.projectprprii.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/search";
    private EditText searchUserBar;
    private Button searchButton;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        searchUserBar = findViewById(R.id.searchUserBar);
        searchButton = findViewById(R.id.searchButton);
        goBackButton = findViewById(R.id.goBack);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClicked(v);
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void onSearchClicked(View view) {
        String query = apiUrl +"?s=" + searchUserBar.getText().toString();
        System.out.println(query);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, query, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response.toString());
                    JSONObject user = response.getJSONObject(0);
                    String username = user.getString("name");
                    String email = user.getString("email");
                    int id = user.getInt("id");

                    User user1 = new User(id, username, email);

                    Intent intent = new Intent(FriendsActivity.this, ViewProfileFriend.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error: " + error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = User.getAuthenticatedUser().getToken();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        VolleySingletone.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

}
