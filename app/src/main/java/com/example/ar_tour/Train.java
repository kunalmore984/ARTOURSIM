package com.example.ar_tour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Train extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Variables....
    private String TAG = Train.class.getSimpleName();
    public static String API_KEY="f5876c05a73bc01d40367ddf88c99f71";
    public static String API_KEY_PRIVATE="eafe09170546c0c8984a74f16001d4d7";
    private String stntext;
    List<String> trainHelperList =new ArrayList<>();
    ArrayList<TrainHelper> trainList =new ArrayList<>();
    TrainAdapter trainAdapter ;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        spinner =(Spinner) findViewById(R.id.Place_spinner);
        if (trainHelperList.size()!=0){
            trainHelperList.clear();
        }
        arrayAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,trainHelperList);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(Train.this);
        EditText editText =(EditText) findViewById(R.id.searchstn);
        Button searchbtn =(Button) findViewById(R.id.submit);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stntext = editText.getText().toString().trim();
                Log.v("test text : ", stntext);
                tcode(stntext);
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (trainList.size()!=0){
            trainList.clear();
        }
        TrainOnStation(text);
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    //TODO : GET THE STATION CODE :-
    private void tcode(String t){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://indianrailapi.com/api/v2/StationCodeOrName/apikey/f5876c05a73bc01d40367ddf88c99f71/SearchText/"+t+"/";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO : parse json File.....
                try {
                    // build up a list of Earthquake objects with the corresponding data.
                    JSONObject stnroot =new JSONObject(response);
                    JSONArray stationarr =stnroot.optJSONArray("Station");
                    //parsing the array......
                    for (int i=0;i<stationarr.length();i++){
                        JSONObject stationobj = stationarr.optJSONObject(i);
                        String Stncode = stationobj.optString("StationCode");
                        Log.e("code : ",Stncode);
                        //code.add(Stncode);
                        trainHelperList.add(Stncode);
                    }
                    arrayAdapter.notifyDataSetChanged();
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Train.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR","Error check logs for more info");
            }
        });
        requestQueue.add(stringRequest);
    }

    //TODO: DISPLAY ALL TRAINS ON SPECIFIED STATION.....:-
    private void TrainOnStation(String code){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e(TAG, "TrainOnStation: "+code);
        String url ="https://indianrailapi.com/api/v2/AllTrainOnStation/apikey/2520bbf3a5e4858a253aa39f19c02bae/StationCode/"+code+"/";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO : parse json File.....
                try {
                    // build up a list of Earthquake objects with the corresponding data.
                    JSONObject stnroot =new JSONObject(response);
                    JSONArray trainarr = stnroot.optJSONArray("Trains");
                    for (int i=0;i<trainarr.length();i++){
                        JSONObject traininfo =trainarr.optJSONObject(i);
                        String TrainNo = traininfo.optString("TrainNo");
                        String TrainName = traininfo.optString("TrainName");
                        String Source = traininfo.optString("Source");
                        String ArrivalTime = traininfo.optString("ArrivalTime");
                        String Destination = traininfo.optString("Destination");
                        String DepartureTime = traininfo.optString("DepartureTime");
                        trainList.add(new TrainHelper(TrainNo,TrainName,Source,ArrivalTime,Destination,DepartureTime));
                    }
                    trainAdapter =new TrainAdapter(Train.this,trainList);
                    ListView listView = findViewById(R.id.listtrain);
                    listView.setAdapter(trainAdapter);
                    //parsing the array......

                    //arrayAdapter.notifyDataSetChanged();
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Train.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR","Error check logs for more info");
            }
        });
        requestQueue.add(stringRequest);
    }

}