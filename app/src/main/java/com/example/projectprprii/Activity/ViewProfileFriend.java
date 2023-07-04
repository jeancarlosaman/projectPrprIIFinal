package com.example.projectprprii.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.WishLists.ViewWishLists;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewProfileFriend extends AppCompatActivity {

    private TextView userNameTextView;
    private TextView emailTextView;
    private Button goBackButton;
    private TextView numGifts;
    private TextView numEvents;
    private Button viewWishlistsButton;
    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/{id}/gifts/reserved";
    private String apiUrl2 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/{id}/wishlists";
    private Button viewBookingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_friend);

        userNameTextView = findViewById(R.id.friendUserName);
        emailTextView = findViewById(R.id.newMailTextView);
        goBackButton = findViewById(R.id.newViewProfileButton);
        viewBookingsButton = findViewById(R.id.viewBookingsButton);

        numGifts = findViewById(R.id.numGiftsTv);
        numEvents = findViewById(R.id.numEventsTv);

        viewWishlistsButton = findViewById(R.id.viewWishListsButton);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileFriend.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        getUserById(id);
        getNumEvents(id);
        getNumGifts(id);

        viewWishlistsButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(ViewProfileFriend.this, ViewWishLists.class);
            intent1.putExtra("id", id);
            startActivity(intent1);
        });

        viewBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileFriend.this, ViewBookings.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void getUserById(int id) {
        String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    int id = response.getInt("id");
                    String username = response.getString("name");
                    String lastName = response.getString("last_name");
                    String email = response.getString("email");
                    String image = response.getString("image");


                    User user = new User(id, username, email);
                    userNameTextView.setText(user.getUsername());
                    emailTextView.setText(user.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
            return headers;
        }
        };

        VolleySingletone.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void getNumGifts(int id) {
        System.out.println(apiUrl.replace("{id}",Integer.toString(id)));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl.replace("{id}",Integer.toString(User.getAuthenticatedUser().getId())), null,
                response -> {
                    System.out.println(response);
                    numGifts.setText(Integer.toString(response.length()));
                },
                error -> {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                } ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }
    private void getNumEvents(int id) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl2.replace("{id}",Integer.toString(id)), null,
                response -> {
                    System.out.println(response);
                    numEvents.setText(Integer.toString(response.length()));
                },
                error -> {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                } ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }
}