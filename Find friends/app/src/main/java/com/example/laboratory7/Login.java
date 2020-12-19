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

public class Login extends AppCompatActivity {
    EditText username, password;
    Button submit, go_signup_button;
    SharedPreferences sp;
    TextView logger, go_signup_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        logger = findViewById(R.id.logger);
        go_signup_button = findViewById(R.id.go_signup_button);

        submit = findViewById(R.id.submit_login);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        go_signup_button.setOnClickListener(new View.OnClickListener() {
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
        if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG);
            Log.e("SD", "NO");
        }
        else {
            loginRequest(username.getText().toString(), password.getText().toString());
        }
    }

    public void signup() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }
    private void loginRequest(String username, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://salty-gorge-74977.herokuapp.com/login/" + username + "-" + password;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String username = "asd", display_name = "", friends_list = "";
                        try {
                            JSONObject root = new JSONObject(response);
                            boolean success = root.getBoolean("success");
//                            Log.e("success", String.valueOf(success));
                            if (!success) {
                                logger.setText("Нэвтрэх нэр эсвэл нууц үг буруу");

                            }
                            else {
                                logger.setText("");
                                int phone = root.getInt("phone");
                                display_name = root.getString("display_name");
                                username = root.getString("username");
//                                Log.e("SDr", display_name);

                                sp = getSharedPreferences("MyUser", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", username);
                                editor.putString("display_name", display_name);
                                editor.putInt("phone", phone);
                                editor.commit();
                                new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Successfully logged in")
                                        .setContentText("It's nice to see you")
                                        .setConfirmText("Continue ->")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Intent intent = new Intent(Login.this, MapActivity.class);
                                                intent.putExtra("longitude", -122.084);
                                                intent.putExtra("latitude", 37.4219983);
                                                Login.this.startActivityForResult(intent, 1);
                                            }
                                        })
                                        .show();
                            }
//                            pass = root.getString("password");
//                            Log.e("SD", pass);
//                            JSONAraray arr = root.getJSONArray("arr");
//                            objs = new Obj[arr.length()];
//                            for (int i = 0; i < arr.length(); i++) {
//                                JSONObject obj = arr.getJSONObject(i);
//                                int id = obj.getInt("index");
//                                String arrname = obj.getString("arrname");
//                                Obj obj1 = new Obj(id, arrname);
//                                objs[i] = obj1;
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("SDA", display_name);
//                        Toast.makeText(getApplicationContext(), pass, Toast.LENGTH_LONG);
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