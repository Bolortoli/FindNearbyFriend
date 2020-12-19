package com.example.laboratory7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationResult;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.laboratory7.UPDATE_LOCATION";
    SharedPreferences sp;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Toast.makeText(context, "Location", Toast.LENGTH_LONG);
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {

                    Location location = result.getLastLocation();
                    String location_string = new StringBuilder("" + location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
                    Toast.makeText(context, location_string, Toast.LENGTH_LONG).show();
                     Log.e("SDA", "WE SDA CHIN" + location_string);
                    sp = context.getSharedPreferences("MyUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putFloat("latitude", (float) location.getLatitude());
                    editor.putFloat("longitude", (float) location.getLongitude());
                    editor.commit();
//                    try {
//                        MapActivity.class.se
//                    }
//                    catch (Exception e) {
//                        Toast.makeText(context, location_string, Toast.LENGTH_LONG).show();
//                    }
//
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url ="https://salty-gorge-74977.herokuapp.com/setLocation/" + sp.getString("username", "") + '-' + location.getLatitude() + "-" + location.getLongitude();
                    Log.e("URL", url);
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("location", response);
                                    try {
                                        Log.e("set location", response);
                                        JSONObject root1 = new JSONObject(response);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("err","onresponseerror");
//                            Toast.makeText(getApplicationContext(), "Arai2", Toast.LENGTH_LONG);
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
//
                }
            }
        }
    }
}
