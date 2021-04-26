package com.example.ar_tour;

public class Hotelhelper {

    private String  mHname;
    private String  maddress;
    private String  mPriceinfo;
   // private String  mrating;

    public Hotelhelper(String Hname,String address,String priceinfo){
        maddress = address;
        mHname = Hname;
        mPriceinfo = priceinfo;
       // mrating = rating + "/10";

    }

    public String getMaddress() {
        return maddress;
    }

    public String getmHname() {
        return mHname;
    }

    public String getmPriceinfo() {
        return mPriceinfo;
    }

   /* public String getMrating() {
        return mrating;
    }*/
}
