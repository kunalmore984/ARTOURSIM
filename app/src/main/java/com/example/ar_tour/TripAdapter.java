package com.example.ar_tour;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private List<TripsHelper> mTripsHelpers;

    public TripAdapter(List<TripsHelper> tripsHelpers){
        mTripsHelpers = tripsHelpers;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);
        //inflating custom layout....
        View TripView = inflater.inflate(R.layout.offerlist,parent,false);
        ViewHolder viewHolder =new ViewHolder(TripView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //getting the position of data in list.....
        TripsHelper tripsHelper =mTripsHelpers.get(position);
        //setting items in textview...
        TextView textView = holder.mtextview;
        textView.setText(tripsHelper.getTripsOffer());

    }

    @Override
    public int getItemCount() {
        return mTripsHelpers.size();
    }

    /*    public TripAdapter(Activity context, ArrayList<TripsHelper> list) {

            super(context,0,list);
            // List with String type
        }

        //
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //to check whether existing layout is used or not.....
            View listitem = convertView;
            if (listitem == null){
                listitem = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trip,parent,false);
            }
            TripsHelper positionadapter = getItem(position);
            TextView textview =(TextView) listitem.findViewById(R.id.textviewoffer);
            textview.setText(positionadapter.getTripsOffer());
            return listitem;
        }*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mtextview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mtextview =(TextView) itemView.findViewById(R.id.textviewoffer);
    }
}

}
