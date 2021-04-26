package com.example.ar_tour;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HotelAdapter extends ArrayAdapter<Hotelhelper> {

    public HotelAdapter(Activity context, ArrayList<Hotelhelper> hotelhelpers){
        super(context,0,hotelhelpers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.single_hotels,parent,false);
        }
        Hotelhelper hotelhelper =getItem(position);

        TextView HotelName = (TextView)listview.findViewById(R.id.hotel_name);
        HotelName.setText(hotelhelper.getmHname());

        TextView Haddress = (TextView)listview.findViewById(R.id.haddress);
        Haddress.setText(hotelhelper.getMaddress());

        TextView Price = (TextView)listview.findViewById(R.id.price);
        Price.setText(hotelhelper.getmPriceinfo());

        return listview;
    }
}
