package com.example.ar_tour;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.ar_tour.Register.TAG;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> Number;
    ArrayList<Integer> ids;
    ArrayList<String> title =new ArrayList<>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    CustomAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    private String location[] = {"London","Manali,_Himachal_Pradesh"};
    private String location_title[] = {"London","Manali"};
    private int img_id[] = {R.drawable.palace_of_westminster__london___feb_2007,R.drawable.solangvalley};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        //tabbed view for trips offer......
        recyclerView = (getActivity()).findViewById(R.id.Recylerview);

        RecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        Number = new ArrayList<String >();
        ids = new ArrayList<>();
        // Adding items to RecyclerView.
        //AddItemsToRecyclerViewArrayList();

        Infocall();
        TextView trains = (view).findViewById(R.id.trains);
        trains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trainsintent = new Intent(getActivity(), Train.class);
                startActivity(trainsintent);
            }
        });
        TextView hotes = (view).findViewById(R.id.hotels);
        hotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hoteli = new Intent(getActivity(), Hotel.class);
                startActivity(hoteli);
            }
        });

        TextView flights = (view).findViewById(R.id.flights);
        flights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent flightintent = new Intent(getActivity(), FlightsActivity.class);
                startActivity(flightintent);
            }
        });

        //Infocall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home2, container, false);


    }

    private void Infocall() {
        String place;
        String title;
        int id;
        for (int i=0;i<location.length;i++){
            if (location[i] != "\0"){
                place=location[i];
                location[i] = "\0";
                id=img_id[i];
                title=location_title[i];
                InfocallManali(place,id,title);
            }
        }
        adaptercall();
    }

    private void InfocallManali(String place,int imgid,String t){

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles="+place+"&prop=extracts&exintro&explaintext&redirects=1&indexpageids";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO : parse json File.....
                try {
                    JSONObject root =new JSONObject(response);
                    JSONObject queryobj = root.getJSONObject("query");
                    JSONArray pageid = queryobj.getJSONArray("pageids");
                    for (int i = 0 ;i<pageid.length();i++){
                        String id = pageid.getString(i);
                        Log.e("Home frag card","checking page id  : "+id);
                        JSONObject pagesobj = queryobj.getJSONObject("pages");
                        JSONObject num = pagesobj.optJSONObject(id);
                        String data = num.getString("extract");
                        Log.e("Home frag card","checking data : "+data);
                        Number.add(data);
                        ids.add(imgid);
                        title.add(t);
                    }
                    //adaptercall();

                    Infocall();
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("ERROR", "Error check logs for more info");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void adaptercall(){
        //start...
        RecyclerViewHorizontalAdapter = new CustomAdapter(Number,ids,title);

        HorizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        // Adding on item click listener to RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    // Showing clicked item value on screen using toast message.


                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //end
    }
}

