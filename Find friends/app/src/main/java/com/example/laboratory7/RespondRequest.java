package com.example.laboratory7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RespondRequest extends AppCompatActivity {
    LinearLayout add;
    GoogleMap map;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.respond_request);

        sp = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);
        add = findViewById(R.id.add_friends_linear);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://salty-gorge-74977.herokuapp.com/friendrequests/" + sp.getString("username", "");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("tag ymbe", response);
                            JSONObject root = new JSONObject(response);
                            JSONArray mArray = root.getJSONArray("data");
                            for (int index = 0; index < mArray.length(); index++) {
                                LinearLayout scroll = findViewById(R.id.respond_linear);

                                JSONObject mJsonObject = mArray.getJSONObject(index);

                                String friend_name = mJsonObject.getString("requester_display_name");
                                String friend_user_name = mJsonObject.getString("requester_username");

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View view = inflater.inflate(R.layout.addfrienditem, null);
                                Button add = view.findViewById(R.id.add_friend_button);
                                TextView add_user_name = view.findViewById(R.id.add_user_name);
                                int rand = index % 7 + 1;
                                ImageView user_image = view.findViewById(R.id.friend_pic_add);
                                switch (rand) {
                                    case 1:
                                        user_image.setImageResource(R.mipmap.avatar1_round);
                                        break;
                                    case 2:
                                        user_image.setImageResource(R.mipmap.avatar2_round);
                                        break;
                                    case 3:
                                        user_image.setImageResource(R.mipmap.avatar3_round);
                                        break;
                                    case 4:
                                        user_image.setImageResource(R.mipmap.avatar4_round);
                                        break;
                                    case 5:
                                        user_image.setImageResource(R.mipmap.avatar5_round);
                                        break;
                                    case 6:
                                        user_image.setImageResource(R.mipmap.avatar6_round);
                                        break;
                                    case 7:
                                        user_image.setImageResource(R.mipmap.avatar7_round);
                                        break;
                                    default:
                                        user_image.setImageResource(R.mipmap.avatar1_round);
                                        break;
                                }


                                add.setText(friend_user_name);
                                add_user_name.setText(friend_name);
                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        sp = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);;
                                        Button b = (Button)v;

                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        String url ="https://salty-gorge-74977.herokuapp.com/approvefriendrequest/" + sp.getString("username", "") + '-' + b.getText().toString();
                                        Log.e("URL", url);
                                        // Request a string response from the provided URL.
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.e("DISPLAY_NAME", response);
                                                        try {
                                                            JSONObject root1 = new JSONObject(response);
                                                            if (root1.getBoolean("success")) {
                                                                new SweetAlertDialog(RespondRequest.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setTitleText("Friend request approved")
                                                                        .setConfirmText("Continue")
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                Intent intent = new Intent(RespondRequest.this, MapActivity.class);
                                                                                intent.putExtra("longitude", -122.084);
                                                                                intent.putExtra("latitude", 37.4219983);
                                                                                RespondRequest.this.startActivityForResult(intent, 1);
                                                                            }
                                                                        })
                                                                        .show();
                                                            }
                                                            else {
                                                                new SweetAlertDialog(RespondRequest.this, SweetAlertDialog.ERROR_TYPE)
                                                                        .setTitleText("Friend request already approved")
                                                                        .setConfirmText("Okay")
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                Intent intent = new Intent(RespondRequest.this, MapActivity.class);
                                                                                intent.putExtra("longitude", -122.084);
                                                                                intent.putExtra("latitude", 37.4219983);
                                                                                RespondRequest.this.startActivityForResult(intent, 1);
                                                                            }
                                                                        })
                                                                        .show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
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
                                });
                                scroll.addView(view);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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