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
            //listview = LayoutInflater.from(getContext()).inflate(R.layout.single_item_train,parent,false);
        }
        PoiHelper poiHelper =getItem(position);
        TextView trainno =(TextView) listview.findViewById(R.id.train_No_);
        assert poiHelper != null;
        trainno.setText(poiHelper.getmTrainNo());

        TextView tfrom =(TextView) listview.findViewById(R.id.train_from_);
        tfrom.setText(poiHelper.getmFrom());

        TextView tto =(TextView) listview.findViewById(R.id.train_to_);
        tto.setText(poiHelper.getmTo());

        TextView ttime =(TextView) listview.findViewById(R.id.train_time_);
        ttime.setText(poiHelper.getmTime());


        return listview;
    }
}
