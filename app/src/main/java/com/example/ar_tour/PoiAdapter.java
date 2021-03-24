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

public class PoiAdapter extends ArrayAdapter<PoiHelper> {

    public PoiAdapter(Activity context, ArrayList<PoiHelper> place){
        super(context,0,place);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //to check that existing view is reused ,if not not then inflate the view
        View listview =convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.poilist,parent,false);
        }
        PoiHelper poiHelper =getItem(position);
        TextView Placeview = listview.findViewById(R.id.poiplace);
        Placeview.setText(poiHelper.getPlace());
        return listview;
    }
}
