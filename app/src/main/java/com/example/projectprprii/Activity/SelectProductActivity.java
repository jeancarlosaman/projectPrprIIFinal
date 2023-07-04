package com.example.projectprprii.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.projectprprii.Activity.WishLists.ViewWishLists;
import com.example.projectprprii.Adapters.ProductAdapter;
import com.example.projectprprii.Adapters.WishListAdapter;
import com.example.projectprprii.Entities.Product;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.Entities.WishList;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectProductActivity extends AppCompatActivity {

    private String apiUrl = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";
    private GridView productsGridView;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        goBackButton = findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productsGridView = findViewById(R.id.productGridView);
        getProducts();
    }

    private void getProducts() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    List<Product> products = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject productObject = response.getJSONObject(i);
                            int id = productObject.getInt("id");
                            String name = productObject.getString("name");
                            String description = productObject.getString("description");
                            float price = (float) productObject.getDouble("price");
                            String link = productObject.getString("link");
                            String photo = productObject.getString("photo");
                            Product product = new Product(id, name, description, price, link, photo,0);
                            products.add(product);

                        }

                        ProductAdapter adapter = new ProductAdapter(this, products);
                        productsGridView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + User.getAuthenticatedUser().getToken());
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}