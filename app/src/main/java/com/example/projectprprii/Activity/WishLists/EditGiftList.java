package com.example.projectprprii.Activity.WishLists;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.VolleySingletone;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditGiftList extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, deadlineEditText;
    private Button deleteButton;
    private Button editButton, goBackButton;

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/{id}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", -1);
        setContentView(R.layout.activity_edit_gift_list);

        nameEditText = findViewById(R.id.selectProductBtn);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        deadlineEditText = findViewById(R.id.deadlineEditText);
        editButton = findViewById(R.id.editButton);
        goBackButton = findViewById(R.id.goBackButton);
        deleteButton = findViewById(R.id.deleteWishListButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGiftList();
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWishList();
            }
        });

        getWishlist();
    }

    private void editGiftList() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String deadline = deadlineEditText.getText().toString().trim();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("description", description);
            requestBody.put("end_date", deadline);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiUrl = apiUrl.replace("{id}", String.valueOf(getIntent().getIntExtra("id", -1)));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, apiUrl, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String description = response.getString("description");

                            Toast.makeText(EditGiftList.this, "Wish List edited successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        Toast.makeText(EditGiftList.this, "Error editing Wish List", Toast.LENGTH_SHORT).show();
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

    private void getWishlist(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiUrl.replace("{id}", String.valueOf(getIntent().getIntExtra("id", -1))),                null,
                response -> {
                    String name = response.optString("name");
                    nameEditText.setText(name);
                    String description = response.optString("description");
                    descriptionEditText.setText(description);
                    String endDate = response.optString("end_date");
                    deadlineEditText.setText(endDate);

                },
                error -> {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteWishList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                apiUrl.replace("{id}",String.valueOf(getIntent().getIntExtra("id",-1))),
                null,
                response -> {
                    Toast.makeText(this, "Wishlist deleted", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deleted", true);
                    this.setResult(Activity.RESULT_OK, resultIntent);
                    this.finish();
                },
                error -> {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}