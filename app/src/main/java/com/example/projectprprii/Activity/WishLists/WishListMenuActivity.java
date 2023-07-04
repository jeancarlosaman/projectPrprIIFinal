package com.example.projectprprii.Activity.WishLists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprprii.Activity.ProfileActivity;
import com.example.projectprprii.R;

public class WishListMenuActivity extends AppCompatActivity {

    private Button createWishListButton;
    private Button viewWishListsButton;
    private Button goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wish_lists_menu);

        createWishListButton = findViewById(R.id.createWishListButton);
        viewWishListsButton = findViewById(R.id.viewWishListsButton);
        goBackButton = findViewById(R.id.goBackButton);

        createWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WishListMenuActivity.this, CreateWishList.class);
                startActivity(intent);
                finish();
            }
        });

        viewWishListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishListMenuActivity.this, ViewWishLists.class);
                startActivity(intent);
                finish();
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WishListMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
