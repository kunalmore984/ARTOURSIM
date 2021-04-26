package com.example.ar_tour;

import java.util.ArrayList;

public class PoiHelper{
    public String mTrainNo;
    public String mFrom;
    public String mTo;
    public String mTime;

    public PoiHelper(String trainno, String from,String to,String time){
        mTrainNo =trainno;
        mFrom = from;
        mTo = to;
        mTime = time;

    }

    public String getmFrom() {
        return mFrom;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmTo() {
        return mTo;
    }

    public String getmTrainNo() {
        return mTrainNo;
    }
}
