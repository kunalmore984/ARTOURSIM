package com.example.ar_tour;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewerFragment extends Fragment {

    private SharedviewModel model;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewerFragment newInstance(String param1, String param2) {
        ViewerFragment fragment = new ViewerFragment();
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
        return inflater.inflate(R.layout.fragment_viewer, container, false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model =new ViewModelProvider(requireActivity()).get(SharedviewModel.class);

        String title = String.valueOf(model.getSelcted());
        Log.e("Viewerfragment","checking title : "+title);
        TextView info =(view).findViewById(R.id.infotitle);
        info.setText(title);
        filldata(title);
        //String data = String.valueOf(model.getData());


        Button flight =(view).findViewById(R.id.flight_search);
        Button hotel = (view).findViewById(R.id.search_hotel);
        Button train = (view).findViewById(R.id.train_search);

        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment flightfrag =new FlightFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_main, flightfrag); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment hotelfrag =new HotelFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_main, hotelfrag); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment Trainfrag =new TrainFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_main, Trainfrag); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        String f_name = model.getResultf();
        TextView flight_name =view.findViewById(R.id.flight_name);
        if(f_name == null){
            f_name = "Search for flights";
            flight_name.setText(f_name);
        }else {
            flight_name.setText(f_name);
        }

        String h_name = model.getResultsh();
        TextView hotel_name = view.findViewById(R.id.hotel_title);
        if(h_name == null){
            h_name = "Search for Hotels";
            hotel_name.setText(h_name);
        }else {
            hotel_name.setText(h_name);
        }

        String T_name = model.getResultst();
        TextView Train_name = view.findViewById(R.id.train_name);
        if(T_name == null){
            T_name = "Search for Trains";
            Train_name.setText(T_name);
        }else {
            Train_name.setText(T_name);
        }
    }

    private void filldata(String title){
        TextView datainfo =(getView()).findViewById(R.id.info_container);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles="+title+"&prop=extracts&exintro&explaintext&redirects=1&indexpageids";
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
                        Log.e("Viewer frag card","checking data : "+data);
                        datainfo.setText(data);
                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }
            }
        }, error -> Log.v("ERROR", "Error check logs for more info"));
        requestQueue.add(stringRequest);
    }
}