package com.example.projectprprii.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.projectprprii.Activity.WishLists.ViewGifts;
import com.example.projectprprii.Adapters.GiftAdapter;
import com.example.projectprprii.Entities.Gift;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBookings extends AppCompatActivity {

    private GridView giftsGridView;
    private Button goBackButton;
    private ArrayList<Gift> bookings;

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/{id}/gifts/reserved";
    private String apiUrl2 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/gifts/{id}/book";
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gifts);

        giftsGridView = findViewById(R.id.giftListGridView);
        goBackButton = findViewById(R.id.goBackButton);
        userId = getIntent().getIntExtra("id", 0);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getGifts(userId);

    }



    private void getGifts(int id) {
        System.out.println(apiUrl.replace("{id}", Integer.toString(id)));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, apiUrl.replace("{id}", Integer.toString(id)), null,
                response -> {
                    System.out.println(response);
                    bookings = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int giftId = jsonObject.getInt("id");
                            String product_url = jsonObject.getString("product_url");
                            int wishlist_id = jsonObject.getInt("wishlist_id");
                            int priority = jsonObject.getInt("priority");
                            int booked = jsonObject.getInt("booked");
                            bookings.add(new Gift(giftId, wishlist_id, product_url, priority, booked));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    GiftAdapter adapter = new GiftAdapter(this, bookings, false);
                    giftsGridView.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }



    @Override
    protected void onResume() {
        super.onResume();
        getGifts(userId);
    }

}