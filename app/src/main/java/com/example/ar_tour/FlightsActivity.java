package com.example.ar_tour;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

import timber.log.Timber;

public class FlightsActivity extends AppCompatActivity {
    private static final String TAG = FlightsActivity.class.getSimpleName();
   private List<String > arrival =new ArrayList<>();
   private List<String> Depature =new ArrayList<>();
   private final ArrayList<FlightHelper> flightHelpers =new ArrayList<>();
   private ArrayList<String> flightHelpersname =new ArrayList<>();
   private FlightAdapter flightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("FLIGHTS");
        EditText arrivaltxt = findViewById(R.id.arrivalcity);
        EditText depcity = findViewById(R.id.depaturename);
        Button subtn = findViewById(R.id.gobtn);
        subtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String arr= arrivaltxt.getText().toString();
                String dep = depcity.getText().toString();
                 getarrivalCodes(arr,dep);
                 //getdepatureCodes(dep);
                 //callflightmethod();
            }
        });
    }

    private void getarrivalCodes(String arlocation,String deplocation){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String t = arlocation;
        String url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query="+ t;
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root =new JSONObject(response);
                    Log.d(TAG,"cheeking for update : "+response);
                    JSONArray places =root.optJSONArray("Places");
                    for (int i=0;i<places.length();i++){
                        JSONObject placeobj = places.optJSONObject(i);
                        String placeid = placeobj.optString("PlaceId");
                        arrival.add(placeid);
                        Log.d(TAG,"cheeking for update for arrival : "+arrival);
                    }
                    //TODO: call function flight list......
                    getdepatureCodes(deplocation);

                } catch (Exception e) {
                    Log.e(TAG, "error : " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "volley error : " + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "c332486ef4msh1b1ff96daa23844p1236b4jsn662354e9312a");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);

        Log.v(TAG, "Before return : " + arrival);
        //return code;
    }

    private void getdepatureCodes(String depature){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String t = depature;
        String url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query="+t;
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root =new JSONObject(response);
                    JSONArray places =root.optJSONArray("Places");
                    for (int i=0;i<places.length();i++){
                        JSONObject placeobj = places.optJSONObject(i);
                        String placeid = placeobj.optString("PlaceId");
                        Depature.add(placeid);
                        Log.d(TAG,"cheeking for update for depature : "+Depature);
                    }
                    //TODO: call function flight list......
                    callflightmethod();

                } catch (Exception e) {
                    Log.e(TAG, "error : " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "volley error : " + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "c332486ef4msh1b1ff96daa23844p1236b4jsn662354e9312a");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);

        Log.v(TAG, "Before return : " + Depature);
        //return code;
    }

    private void callflightmethod(){
        for (int i=0;i<Depature.size();i++){
            String a = Depature.get(i);
            for (int j=0;j<arrival.size();j++){
                String d = arrival.get(j);
                flightlist(a,d);
            }
        }

    }

    private void flightlist(String arrival,String depature){
        List<String> fromto= new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browseroutes/v1.0/US/USD/en-US/"+depature+"/"+arrival+"/anytime";
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root =new JSONObject(response);
                    Log.e(TAG, "List response : " + response);
                    String name =null;
                    String price =null;
                    JSONArray carriers =root.optJSONArray("Carriers");
                    for (int i=0;i<carriers.length();i++){
                        JSONObject carobj= carriers.getJSONObject(i);
                         name =carobj.optString("Name");
                         flightHelpersname.add(name);
                         //TODO: make another constructor..... for name list
                        Log.e(TAG, "chacking name : " + name);
                        JSONArray places = root.optJSONArray("Places");
                        for (int j=0;j<places.length();j++){
                            JSONObject placeobj = places.optJSONObject(j);
                            String namef = placeobj.optString("Name");
                            fromto.add(namef);
                        }
                        JSONArray routes = root.getJSONArray("Routes");
                        for (int k=0;k<routes.length();k++){
                            JSONObject routeobj = routes.optJSONObject(k);
                            price = routeobj.optString("Price");
                        }
                        flightHelpers.add(new FlightHelper(name,fromto,price));
                    }
                    flightAdapter =new FlightAdapter(FlightsActivity.this,flightHelpers);
                    ListView listView= findViewById(R.id.Flightslist);
                    listView.setAdapter(flightAdapter);
                } catch (Exception e) {
                    Log.e(TAG, "error : " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "volley error : " + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com");
                params.put("X-RapidAPI-Key", "c332486ef4msh1b1ff96daa23844p1236b4jsn662354e9312a");   //changed key
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}