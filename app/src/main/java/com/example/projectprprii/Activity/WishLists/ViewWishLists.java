package com.example.projectprprii.Activity.WishLists;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.VolleySingletone;
import com.example.projectprprii.Adapters.WishListAdapter;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.Entities.WishList;
import com.example.projectprprii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewWishLists extends AppCompatActivity {
    private GridView wishListsGridView;
    private Button goBackButton;

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";
    private String apiUrl2 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/{id}/wishlists";
    private String apiUrl3 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/{id}";

    private int userId;

    private TextView userNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_wish_lists);
        userNameTextView = findViewById(R.id.titleTextView);
        wishListsGridView = findViewById(R.id.wishListsGridView);
        goBackButton = findViewById(R.id.goBackButton);

        if (getIntent().hasExtra("id")) {
            userId = getIntent().getIntExtra("id", 0);
            apiUrl = apiUrl2.replace("{id}", String.valueOf(userId));

            getUserById(userId);
        }

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        getWishLists();
    }



    private void getWishLists() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<WishList> wishLists = new ArrayList<>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject wishlistObject = response.getJSONObject(i);
                                int id = wishlistObject.getInt("id");
                                String name = wishlistObject.getString("name");
                                String description = wishlistObject.getString("description");

                                WishList wishList = new WishList(id, name, description, 0, "", "", "");
                                wishLists.add(wishList);
                            }

                            WishListAdapter adapter = new WishListAdapter(ViewWishLists.this, wishLists);
                            wishListsGridView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewWishLists.this, "Error retrieving Wish Lists", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = User.getAuthenticatedUser().getToken();

                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        VolleySingletone.getInstance(this).addToRequestQueue(request);
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
                    userNameTextView.setText(username+" "+lastName+"'s wishlists");
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

    @Override
    protected void onResume() {
        super.onResume();
        getWishLists();
    }
}
