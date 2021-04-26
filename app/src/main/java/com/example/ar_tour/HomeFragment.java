package com.example.ar_tour;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> Number;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    CustomAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout ;
    View ChildView ;
    int RecyclerViewItemPosition ;


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

    // function to add items in RecyclerView.
    public void AddItemsToRecyclerViewArrayList(){

        Number = new ArrayList<>();
        Number.add("Trip Offer 1");
        Number.add("Trip Offer 2");
        Number.add("Trip Offer 3");
        Number.add("Trip Offer 4");
        Number.add("Trip Offer 5");
        Number.add("Trip Offer 6");
        Number.add("Trip Offer 7");
        Number.add("Trip Offer 8");
        Number.add("Trip Offer 9");
        Number.add("Trip Offer 10");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //settings fragment....
        ImageButton settings=(view).findViewById(R.id.settings_buttons);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsintent =new Intent(getActivity(),setting.class);
                startActivity(settingsintent);
            }
        });
        //tabbed view for trips offer......
        recyclerView = (getActivity()).findViewById(R.id.Recylerview);

        RecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList();
        RecyclerViewHorizontalAdapter = new CustomAdapter(Number);

        HorizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(RecyclerViewHorizontalAdapter);
        // Adding on item click listener to RecyclerView.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting clicked value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    // Showing clicked item value on screen using toast message.
                    Toast.makeText(getContext(), Number.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();

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
        TextView trains=(view).findViewById(R.id.trains);
        trains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trainsintent =new Intent(getActivity(),Train.class);
                startActivity(trainsintent);
            }
        });
        TextView hotes =(view).findViewById(R.id.hotels);
        hotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hoteli = new Intent(getActivity(),Hotel.class);
                startActivity(hoteli);
            }
        });

        TextView flights =(view).findViewById(R.id.flights);
        flights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent flightintent =new Intent(getActivity(),FlightsActivity.class);
                startActivity(flightintent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);


    }
}