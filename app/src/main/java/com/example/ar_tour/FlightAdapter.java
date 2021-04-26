package com.example.ar_tour;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FlightAdapter extends ArrayAdapter<FlightHelper> {

    public FlightAdapter(Activity context, List<FlightHelper> flightHelpers){
        super(context,0,flightHelpers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.single_flight,parent,false);
        }
        FlightHelper flightHelper =getItem(position);
        TextView airname =(TextView)listview.findViewById(R.id.airlinename);
        TextView fromflight =(TextView)listview.findViewById(R.id.fromflight);
        TextView toflight =(TextView)listview.findViewById(R.id.toflight);
        TextView pricetag =(TextView)listview.findViewById(R.id.pricetag);

        airname.setText(flightHelper.getmAname());
        fromflight.setText(flightHelper.getmFrom());
        toflight.setText(flightHelper.getmTo());
        pricetag.setText(flightHelper.getmPrice());

        return listview;
    }
}
