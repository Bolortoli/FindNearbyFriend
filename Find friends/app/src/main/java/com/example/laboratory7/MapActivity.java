package com.example.laboratory7;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.maps.DirectionsApi;
//import com.google.maps.DirectionsApiRequest;
//import com.google.maps.GeoApiContext;
//import com.google.maps.model.DirectionsLeg;
//import com.google.maps.model.DirectionsResult;
//import com.google.maps.model.DirectionsRoute;
//import com.google.maps.model.DirectionsStep;
//import com.google.maps.model.EncodedPolyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SharedPreferences sp;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button add_friend, friend_reqs;

    public void setMyLocation(String latitude, String longitude) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        sp = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);
        Log.e("SHARE", sp.getString("username", "aaa"));
//        , sp.getString("display_name", "")
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://salty-gorge-74977.herokuapp.com/friends/" + sp.getString("username", "");
        // Request a string response from the provided URL.
        add_friend = findViewById(R.id.add_friend_from);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, AddFriends.class);
                startActivity(intent);
            }
        });

        friend_reqs = findViewById(R.id.friend_req);
        friend_reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, RespondRequest.class);
                startActivity(intent);
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            JSONArray mArray = root.getJSONArray("data");
                            for (int index = 0; index < mArray.length(); index++) {
                                LinearLayout scroll = findViewById(R.id.friends_list_linear);
                                JSONObject mJsonObject = mArray.getJSONObject(index);
                                String friend_name = mJsonObject.getString("display_name");
                                String phone = mJsonObject.getString("phone");
                                LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.friend_item, null);
                                Button username = view.findViewById(R.id.friend_user_name);
                                username.setText(friend_name);
                                username.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        LatLng myMap = new LatLng(47.88, 106.9076);
//                                        map.addMarker(new MarkerOptions().position(myMap).title("Current location"));
                                        LatLng myMap = null;
                                        try {
                                            myMap = new LatLng(mJsonObject.getDouble("latitude"), mJsonObject.getDouble("longitude"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myMap, 19));
                                    }
                                });
                                Button friend_phone = view.findViewById(R.id.friend_phone);
                                friend_phone.setText(phone);
                                friend_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Button b = (Button)v;
                                        String number = b.getText().toString();
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + number));
                                        startActivity(callIntent);
                                    }
                                });
//                                friend_phone.setId(mJsonObject.getInt("phone"));
                                Log.e("ttt", String.valueOf(friend_phone.getId()));

//            int rand = random.nextInt(7 - 1 + 1) + 1;
                                int rand = index % 7 + 1;
                                ImageView user_image = view.findViewById(R.id.friend_pic);
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

        LinearLayout scroll = (LinearLayout) findViewById(R.id.friends_list_linear);

    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://salty-gorge-74977.herokuapp.com/friends/" + sp.getString("username", "");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            JSONArray mArray = root.getJSONArray("data");
                            for (int index = 0; index < mArray.length(); index++) {
                                JSONObject mJsonObject = mArray.getJSONObject(index);
                                double lat = mJsonObject.getDouble("latitude");
                                double long1 = mJsonObject.getDouble("longitude");

                                LatLng myMap = new LatLng(lat, long1);

                                map.addMarker(new MarkerOptions().position(myMap).title(mJsonObject.getString("display_name")));
                                sp = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);

                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myMap, 15));
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

        sp = getApplicationContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        double lat = sp.getFloat("latitude", (float) 47.9216);
        double long1 = sp.getFloat("longitude", (float) 106.8940);

        LatLng myMap = new LatLng(lat, long1);

        map.addMarker(new MarkerOptions().position(myMap).title(sp.getString("username", "me")));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myMap, 15));

//        Log.e("Latitude", String.valueOf(lat));
//        Toast.makeText(this, lat, Toast.LENGTH_SHORT).show();
//        System.out.println("asd" + long1);
//        map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

    }
    private void updateLocation() {
        buildLocatoinRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocatoinRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(20000);
//        locationRequest.setFastestInterval(10000);
        locationRequest.setSmallestDisplacement(10f);
    }
}
