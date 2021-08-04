package com.example.ar_tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TripAdapter extends ArrayAdapter<TripsHelper> {


    public TripAdapter(Context context, List<TripsHelper> tripsHelpers){
        super(context,0,tripsHelpers);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.offerlist,parent,false);
        }
        TripsHelper tripsHelper =getItem(position);
        TextView textView =(TextView)listview.findViewById(R.id.textviewoffer);
        textView.setText(tripsHelper.getTripsOffer());

        TextView titletext =(TextView)listview.findViewById(R.id.title_place);
        titletext.setText(tripsHelper.getMtitle());

        ImageView imageView =(ImageView)listview.findViewById(R.id.imagecard);
        imageView.setImageResource(tripsHelper.getmImage_id());
        imageView.setVisibility(View.VISIBLE);
        return listview;
    }
}
