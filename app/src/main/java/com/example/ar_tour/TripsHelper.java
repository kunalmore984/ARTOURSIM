package com.example.ar_tour;

public class TripsHelper {
    private String Trips;
    private String mtitle;
    private int mImage_id;

    public TripsHelper(String mTrips,String title,int image_id){
        Trips = mTrips;
        mtitle = title;
        mImage_id = image_id;
    }


    public String getTripsOffer(){
        return Trips;
     }

    public String getMtitle() {
        return mtitle;
    }

    public int getmImage_id() {
        return mImage_id;
    }
}
