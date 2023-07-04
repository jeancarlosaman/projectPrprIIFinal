package com.example.projectprprii.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projectprprii.Entities.User;
import com.example.projectprprii.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;

    private String apiUrl = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/login";
    private String apiUrl2 = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users";

    private SessionManager sessionManager;

    private String LoggedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        sessionManager = new SessionManager(this);

        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        String email = emailEditText.getText().toString().trim();
        String myEmail = email;
        String password = passwordEditText.getText().toString().trim();

        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("email", email);
            requestBodyJson.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBodyJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accessToken = response.optString("accessToken");

                        sessionManager.saveSessionToken(accessToken);

                        String savedToken = sessionManager.getSessionToken();
                        Log.d("Session Token", savedToken);

                        setLoggedUser(savedToken, myEmail);

                        Toast.makeText(SignInActivity.this, "User authenticated successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SignInActivity.this, "Failed to sign in. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }

    private void setLoggedUser(String token, String email) {
        String url = apiUrl2;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            System.out.println(response);
            boolean found = false;
            for (int i = 0; i < response.length() && !found; i++) {

                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String auxEmail = jsonObject.getString("email");
                    User user = new User(id, name, auxEmail);
                    if (user.getEmail().equals(email)) {
                        User.setAuthenticatedUser(user);
                        User.getAuthenticatedUser().setToken(token);
                        found = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        } ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        VolleySingletone.getInstance(this).addToRequestQueue(request);
    }







}
