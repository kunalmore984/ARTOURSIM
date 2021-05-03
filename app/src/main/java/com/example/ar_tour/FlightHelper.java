package com.example.ar_tour;

import java.util.List;

public class FlightHelper {

    private String mAname;
    private String mFrom;
    private String mTo;
    private String mPrice;
    public FlightHelper(String name ,List<String> fromto,String price){
        mAname = "Airlines: "+ name;
        mFrom ="From: "+ fromto.get(0);
        mTo ="To: "+fromto.get(1);
        mPrice ="Price: $"+ price;
    }

    public String getmTo() {
        return mTo;
    }

    public String getmFrom() {
        return mFrom;
    }

    public String getmAname() {
        return mAname;
    }

    public String getmPrice() {
        return mPrice;
    }

}
