package com.example.ar_tour;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.ClipData.Item;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trip#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trip extends Fragment {

    final ArrayList<TripsHelper> Trips= new ArrayList<TripsHelper>();
    private String location[] = {"London","Manali,_Himachal_Pradesh","Delhi","New York","Mumbai"};
    private String location_title[] = {"London","Manali","Delhi","New York","Mumbai"};
    private int image_id[] = {
            R.drawable.palace_of_westminster__london___feb_2007,
            R.drawable.solangvalley,
            R.drawable.delhi_india_gate,
            R.drawable.new_york_city_statue_of_liberty,
            R.drawable.taj_hotel_mumbai
    };
    private SharedviewModel model;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private ArrayList<TripsHelper> Trips;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Trip() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trip.
     */
    // TODO: Rename and change types and number of parameters
    public static Trip newInstance(String param1, String param2) {
        Trip fragment = new Trip();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerview intialization......
        EditText custom =view.findViewById(R.id.add_custom);
       SharedviewModel smodel =new ViewModelProvider(requireActivity()).get(SharedviewModel.class);
        Button gobtn =view.findViewById(R.id.gbtn);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = custom.getText().toString();
                smodel.select(location);
                Fragment fragment = new ViewerFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_main, fragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        infocall();
    }

    private void infocall(){
        String title;
        String place;
        int id;
        for (int i=0;i<location.length;i++){
            if (!location[i].equals("\0")){
                place=location[i];
                title = location_title[i];
                location[i] = "\0";
                id=image_id[i];
                InfocallManali(place,title,id);
            }
        }
        adaptercall();

    }

    private void InfocallManali(String place,String title,int imgid){
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
                        Trips.add(new TripsHelper(data,title,imgid));
                    }
                    //adaptercall();
                    infocall();
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }
            }
        }, error -> Log.v("ERROR", "Error check logs for more info"));
        requestQueue.add(stringRequest);
    }

    private void adaptercall(){
        model =new ViewModelProvider(requireActivity()).get(SharedviewModel.class);

        ListView listView = (getView()).findViewById(R.id.listviewtrip);
        TripAdapter adapter =new TripAdapter(getActivity(),Trips);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TripsHelper helper =Trips.get(i);
                String title = helper.getMtitle();
                Log.e("Check","Checking title : "+title);
                model.select(title);
                Fragment fragment = new ViewerFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_main, fragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

    }
}