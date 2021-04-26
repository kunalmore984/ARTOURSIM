package com.example.ar_tour;

public class TrainHelper {
    private String  mTrainNo;
    private String mDepatureTime;
    private String mTrainName;
    private String mSource;
    private  String mArrivalTime;
    private String mDestination;

    public TrainHelper(String TrainNo, String TrainName,String source,String ArrivalTime,String Destination,String DepartureTime){
        mTrainNo = TrainNo;
        mArrivalTime= ArrivalTime;
        mDepatureTime = DepartureTime;
        mDestination = Destination;
        mSource = source;
        mTrainName = TrainName;
    }

    public String getmTrainNo() {
        return mTrainNo;
    }

    public String getmArrivalTime() {
        return mArrivalTime;
    }

    public String getmDepatureTime() {
        return mDepatureTime;
    }

    public String getmDestination() {
        return mDestination;
    }

    public String getmSource() {
        return mSource;
    }

    public String getmTrainName() {
        return mTrainName;
    }
}
