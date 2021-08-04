package com.example.ar_tour;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyView> {
    // List with String type
    private final List<String > list;
    private final ArrayList<Integer>  idslist;
    private final ArrayList<String> titlelist;


    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {


        holder.textView.setText(list.get(position));
        holder.imageView.setImageResource(idslist.get(position));
        holder.titletext.setText(titlelist.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public TextView titletext;

        public MyView(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textview1);
            imageView =(ImageView) view.findViewById(R.id.imageoffer);
            titletext =(TextView) view.findViewById(R.id.home_title);

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public CustomAdapter(List<String> horizontalList, ArrayList<Integer> ids,ArrayList<String> tite)
    {
        this.list = horizontalList;
        this.idslist = ids;
        this.titlelist= tite;
    }

}
