package com.example.ar_tour;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel extends AppCompatActivity {
    private EditText edloc;
    private static final String TAG = Hotel.class.getSimpleName();
    private ArrayList<String> destid =new ArrayList<>();
    ArrayList<Hotelhelper> hotelhelpers =new ArrayList<>();
    HotelAdapter hotelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        edloc=(EditText) findViewById(R.id.searchloc);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("HOTELS");
        Button submit =(Button) findViewById(R.id.subitbtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Edloc=edloc.getText().toString();
                if (hotelhelpers.size()!=0){
                    hotelhelpers.clear();
                }
                getLocationid(Edloc);
            }
        });
    }

    private void getLocationid(String EDloc){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.v(TAG,"Destination String : "+EDloc);
        String t = EDloc;
        String url = "https://hotels4.p.rapidapi.com/locations/search?query="+t+"&locale=en_US";
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VolleyResponse", "response: " + response);
                try {
                    JSONObject root = new JSONObject(response);
                    //TODO: Parse JSON array......
                    JSONArray suggestions = root.getJSONArray("suggestions");
                    for (int i=0;i<suggestions.length();i++){
                        JSONObject sugobj =suggestions.optJSONObject(i);
                        JSONArray entites = sugobj.optJSONArray("entities");
                        for (int j=0;j<entites.length();j++){
                            JSONObject etobj = entites.optJSONObject(j);
                            String destinationid = etobj.optString("destinationId");
                            Log.v(TAG,"Destination ID : "+destinationid);
                            //destid.add(destinationid);
                            getHotellist(destinationid);
                        }
                    }
                    //Log.v(TAG,"Destination ID : "+destid);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Host", "hotels4.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "*****");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Get hotels list on specific location.....
    private void getHotellist(String destid){
        String test =destid;
        Log.v(TAG,"Destination Message :"+test);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String  url ="https://hotels4.p.rapidapi.com/properties/list?destinationId="+test+"&pageNumber=1&checkIn=2020-01-08&checkOut=2020-01-15&pageSize=25&adults1=1&currency=USD&locale=en_US&sortOrder=PRICE";
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        StringRequest stringRequest =new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //TODO: parse hotels list json file.....
                    JSONObject root =new JSONObject(response);
                    String testst = root.optString("result");
                    Log.v(TAG,"RESULTS : "+testst);
                    JSONObject dataobj = root.optJSONObject("data");
                    JSONObject bodyobj = dataobj.optJSONObject("body");
                    JSONObject searchobj = bodyobj.optJSONObject("searchResults");
                    JSONArray results = searchobj.optJSONArray("results");
                    for (int i =0;i<results.length();i++){
                        JSONObject resobj = results.optJSONObject(i);
                        String Hname = resobj.optString("name");
                        JSONObject addobj = resobj.optJSONObject("address");
                        String sadd = addobj.optString("streetAddress");
                        String ladd = addobj.optString("locality");
                        String reg = addobj.optString("region");
                        String country = addobj.optString("countryName");
                        /*JSONObject reviewz = resobj.optJSONObject("guestReviews");
                        String  rating = reviewz.optString("rating");
                        Log.v(TAG,"Rate :"+rating);*/
                        JSONObject rate = resobj.optJSONObject("ratePlan");
                        JSONObject price = rate.optJSONObject("price");
                        String priceinfo = price.getString("current");
                        String address = sadd+","+ladd+","+reg+","+country;
                        hotelhelpers.add(new Hotelhelper(Hname,address,priceinfo));

                    }
                    hotelAdapter =new HotelAdapter(Hotel.this,hotelhelpers);
                    ListView listView =(ListView) findViewById(R.id.hotelsid);
                    listView.setAdapter(hotelAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Host", "hotels4.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "*****");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}