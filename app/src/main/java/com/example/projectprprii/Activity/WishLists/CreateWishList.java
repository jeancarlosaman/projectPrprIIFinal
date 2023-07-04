package com.example.projectprprii.Activity.WishLists;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.VolleySingletone;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.Entities.WishList;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateWishList extends AppCompatActivity {
    private EditText nameEditText, descriptionEditText, deadlineEditText;
    private Button createButton, goBackButton;

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_wish_list);

        nameEditText = findViewById(R.id.selectProductBtn);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        deadlineEditText = findViewById(R.id.deadlineEditText);
        createButton = findViewById(R.id.createButton);
        goBackButton = findViewById(R.id.goBackButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWishList();
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createWishList() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String deadline = deadlineEditText.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
            Toast.makeText(CreateWishList.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        WishList wishList = new WishList(name, description, 0, "", "", deadline);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", wishList.getName());
            requestBody.put("description", wishList.getDescription());
            requestBody.put("end_date", wishList.getEnd_date());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        try {
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String description = response.getString("description");

                            Toast.makeText(CreateWishList.this, "Wish List created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            // Perform any other necessary actions
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateWishList.this, "Error creating Wish List", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = User.getAuthenticatedUser().getToken();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }

}
