package com.example.ar_tour;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlightFragment extends Fragment {

    private EditText mDepature;
    private EditText mArrival;
    private Button mGo;
    String dep = "no input";
    String arr = "no input";
    String Time = "08/08/1999";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FlightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlightFragment newInstance(String param1, String param2) {
        FlightFragment fragment = new FlightFragment();
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
        //ArrayList<PoiHelper> poiHelpers =QueryUtils.extractpoi();
        /*ArrayList<PoiHelper> poiHelpers =new ArrayList<PoiHelper>();
        mDepature =(view).findViewById(R.id.depaturecity);
        mArrival =(view).findViewById(R.id.arrivalcity);
        mGo =(view).findViewById(R.id.gobtn);

        mGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dep = mDepature.getText().toString();
                Log.d("DEPATURE",dep);
                arr = mArrival.getText().toString();
                Log.d("Arrival",arr);
            }
        });
        poiHelpers.add(new PoiHelper(dep, arr,Time));
        PoiAdapter poiAdapter =new PoiAdapter(getActivity(),poiHelpers);
        ListView listView =(getView()).findViewById(R.id.flightlist);
        listView.setAdapter(poiAdapter);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight, container, false);
    }
}