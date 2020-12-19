package com.example.laboratory7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends AppCompatActivity {
    EditText username_signup, password_signup, password_again_signup, display_name_signup, phone_signup;
    Button submit;
    SharedPreferences sp;
    TextView logger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username_signup = findViewById(R.id.username_signup);
        password_signup = findViewById(R.id.password_signup);
        password_again_signup = findViewById(R.id.password_again_signup);
        display_name_signup = findViewById(R.id.display_name_signup);
        logger = findViewById(R.id.logger_signup);
        phone_signup = findViewById(R.id.phone_signup);


        submit = findViewById(R.id.submit_login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    private void login() {

    }


//    SharedPreferences sharep = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);
//    String username = sharep.getString("username", "Unknown");
//
//    String url ="https://salty-gorge-74977.herokuapp.com/friends/" + username;

    public void signup() {
        if (!password_signup.getText().toString().equals(password_again_signup.getText().toString())) {
            logger.setText("Password verification violated");
        }
        if (username_signup.getText().toString().equals("") || password_signup.getText().toString().equals("") || password_again_signup.getText().toString().equals("") || display_name_signup.getText().toString().equals("") || phone_signup.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Fields shouldn't be empty", Toast.LENGTH_LONG);
            Log.e("SD", "NO");
            logger.setText("Password verification violated");
        }
        else {
            signupRequest(username_signup.getText().toString(), password_signup.getText().toString(), display_name_signup.getText().toString(), phone_signup.getText().toString());
        }
    }
    private void signupRequest(String username, String password, String display_name, String phone) {
        String url ="https://salty-gorge-74977.herokuapp.com/signup/" + username + "-" + password + "-" + display_name + "-" + phone;
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            new SweetAlertDialog(SignUp.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Successfully signed up")
                                    .setContentText("It's nice to see you")
                                    .setConfirmText("Continue ->")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent intent = new Intent(SignUp.this, Login.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        }
                        else {
                            new SweetAlertDialog(SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Name error")
                                    .setContentText("Username or display name already taken")
                                    .setConfirmText("Try again ->")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Log.e("Saaad", "thh");
                                        }
                                    })
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Arai2", Toast.LENGTH_LONG);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}