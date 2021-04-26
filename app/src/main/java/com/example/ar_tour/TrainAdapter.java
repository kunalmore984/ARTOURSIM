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

public class TrainAdapter extends ArrayAdapter<TrainHelper> {

    public TrainAdapter(Activity context, List<TrainHelper> trainHelperList){
        super(context,0,trainHelperList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if (listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.single_item,parent,false);
        }
        TrainHelper trainHelper =getItem(position);
        TextView arrival =(TextView)listview.findViewById(R.id.train_time_);
        arrival.setText(trainHelper.getmArrivalTime());

        TextView trainno = (TextView)listview.findViewById(R.id.train_No_);
        trainno.setText(trainHelper.getmTrainNo());

        TextView trainfrom = (TextView)listview.findViewById(R.id.train_from_);
        trainfrom.setText(trainHelper.getmSource());

        TextView trainto = (TextView)listview.findViewById(R.id.train_to_);
        trainto.setText(trainHelper.getmDestination());

        TextView traindept = (TextView)listview.findViewById(R.id.train_depature_);
        traindept.setText(trainHelper.getmDepatureTime());

        return listview;
    }
}
