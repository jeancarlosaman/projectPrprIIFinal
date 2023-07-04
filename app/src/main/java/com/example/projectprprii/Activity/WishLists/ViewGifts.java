package com.example.projectprprii.Activity.WishLists;

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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Activity.ViewGiftActivity;
import com.example.projectprprii.Activity.VolleySingletone;
import com.example.projectprprii.Adapters.GiftAdapter;
import com.example.projectprprii.Entities.Gift;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.Entities.WishList;
import com.example.projectprprii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewGifts extends AppCompatActivity {
    private int wishlistId;
    private GridView giftsGridView;
    private Button goBackButton;
    private Button addGiftButton;
    private Button editButton;
    private TextView giftListName;
    private WishList wishlist;
    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/wishlists/{id}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gifts);
        wishlistId = getIntent().getIntExtra("id", 0);
        apiUrl = apiUrl.replace("{id}", String.valueOf(wishlistId));
        giftListName = findViewById(R.id.titleTextView);
        giftsGridView = findViewById(R.id.giftListGridView);
        goBackButton = findViewById(R.id.goBackButton);
        addGiftButton = findViewById(R.id.addGiftButton);
        editButton = findViewById(R.id.editWishListButton);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addGiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGift();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWishList();
            }
        });
        getWishlist();
    }

    private void addGift() {
        Intent intent = new Intent(this, ViewGiftActivity.class);
        intent.putExtra("id", wishlistId);
        intent.putExtra("addGift",true);
        startActivity(intent);
    }

    private void getWishlist(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    String name = response.optString("name");
                    giftListName.setText(name);
                    String description = response.optString("description");
                    int userId = response.optInt("user_id");
                    if (userId == User.getAuthenticatedUser().getId()) {
                        addGiftButton.setVisibility(View.VISIBLE);
                        editButton.setVisibility(View.VISIBLE);
                    }
                    String createdAt = response.optString("created_date");
                    String endDate = response.optString("end_date");
                    wishlist = new WishList(wishlistId, name, description,userId, createdAt, endDate);
                    JSONArray gifts = null;
                    try {
                        gifts = response.getJSONArray("gifts");
                        for (int i = 0; i < gifts.length(); i++) {
                            JSONObject gift = gifts.optJSONObject(i);
                            int id = gift.optInt("id");
                            int wishlist_id = gift.optInt("wishlist_id");
                            String productUrl = gift.optString("product_url");
                            int priority = gift.optInt("priority");
                            int booked = gift.optInt("booked");
                            wishlist.addGift(new Gift(id,wishlist_id,productUrl, priority, booked));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    GiftAdapter adapter = new GiftAdapter(ViewGifts.this, wishlist.getGiftsList(),userId == User.getAuthenticatedUser().getId());
                    giftsGridView.setAdapter(adapter);
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data == null) return;
                        if (data.getBooleanExtra("deleted", false)){
                            finish();
                        }
                    }
                }
            });
    private void editWishList() {
        Intent intent = new Intent(this, EditGiftList.class);
        intent.putExtra("id", wishlistId);
        someActivityResultLauncher.launch(intent);
    }



    @Override
    protected void onResume() {
        super.onResume();
        getWishlist();
    }

}