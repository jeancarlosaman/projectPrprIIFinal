package com.example.projectprprii.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Entities.Gift;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewGiftActivity extends AppCompatActivity {
    private Gift gift;
    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/{id}";
    private String apiUrl2 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts";
    private boolean editGift;
    private boolean addGift;

    private TextView bookedBy;

    private EditText priority;
    private TextView priorityText;


    private TextView nameText;

    private Button editButton;
    private Button selectButton;
    private Button goBackButton;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) return;
                        String product_url = data.getStringExtra("product_url");
                        gift.setProduct_url(product_url);
                        getProduct_name(gift,nameText);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        editGift = intent.getBooleanExtra("editGift", false);
        addGift = intent.getBooleanExtra("addGift", false);
        if (editGift || addGift){
            setContentView(R.layout.activity_edit_gift);
            priority = findViewById(R.id.priorityEditText);
            editButton = findViewById(R.id.editButton);
            selectButton = findViewById(R.id.selectProductBtn);
            if (addGift){
                editButton.setText("Add Gift");
            }
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addGift){
                        addGift();
                    }else{
                        editGift(id);
                    }
                }
            });

            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewGiftActivity.this, SelectProductActivity.class);
                    someActivityResultLauncher.launch(intent);
                }
            });

        } else {
            setContentView(R.layout.activity_view_gift);
            priorityText = findViewById(R.id.priorityValueTextView);
        }

        nameText = findViewById(R.id.nameValueTextView);
        bookedBy = findViewById(R.id.bookedByName);
        goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        System.out.println("ID: "+id);
        if (!addGift){
            getGift(id);
        }else {
            gift = new Gift();
            gift.setWishlist_id(id);
        }

    }
    private void addGift() {
        if (gift.getProduct_url() == null || gift.getProduct_url().isEmpty()) {
            Toast.makeText(this, "Please select a product first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priority.getText().toString().isEmpty()) {
            Toast.makeText(this, "Priority is required", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("wishlist_id", gift.getWishlist_id());
            jsonBody.put("product_url", gift.getProduct_url());
            jsonBody.put("priority", Integer.parseInt(priority.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl2, jsonBody,
                response -> {
                    Toast.makeText(this, "Gift added", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    if (error != null && error.networkResponse != null) {
                        System.out.println(error.networkResponse);
                    }
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


    private void editGift(int id) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("wishlist_id", gift.getWishlist_id());
            jsonBody.put("product_url", gift.getProduct_url());
            if (priority.getText().toString().length()<= 0){
                Toast.makeText(this, "Priority is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gift.getProduct_url().length()<= 0){
                Toast.makeText(this, "Product is required", Toast.LENGTH_SHORT).show();
                return;
            }
            jsonBody.put("priority", Integer.parseInt(priority.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,apiUrl.replace("{id}",Integer.toString(id)), jsonBody,
                response -> {
                    Toast.makeText(this, "Gift updated", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    System.out.println(error.networkResponse);
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

    private void getGift(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,apiUrl.replace("{id}",Integer.toString(id)), null,
                response -> {
                    try {
                        gift = new Gift(response.getInt("id"),response.getInt("wishlist_id"), response.getString("product_url"),response.optInt("priority"),response.getInt("booked"));
                        if (editGift){
                            priority.setText(String.valueOf(gift.getPriority()));
                        } else {
                            priorityText.setText(String.valueOf(gift.getPriority()));
                        }
                        getProduct_name(gift,nameText);
                        getBookedByName(gift,bookedBy);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void getBookedByName(Gift gift, TextView bookedBy) {
    }

    public void getProduct_name(Gift gift,TextView pdName) {

        if (gift.getProduct_url().startsWith("https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products/")) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,gift.getProduct_url(), null,
                    response -> {
                        System.out.println(response);
                        String name = response.optString("name");
                        pdName.setText(name);
                    },
                    error -> {
                        pdName.setText(gift.getProduct_url());
                    } ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                    return headers;
                }
            };
            VolleySingletone.getInstance(this).addToRequestQueue(request);

        }else {
            pdName.setText(gift.getProduct_url());
        }


    }


}