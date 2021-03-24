package com.example.ar_tour;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragmentforAR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragmentforAR extends Fragment {
    protected UnityPlayerActivity mUnityPlayer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragmentforAR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragmentforAR.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragmentforAR newInstance(String param1, String param2) {
        ExploreFragmentforAR fragment = new ExploreFragmentforAR();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_fragmentfor_a_r, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<PoiHelper> poiHelpers =QueryUtils.extractpoi();
        PoiAdapter poiAdapter =new PoiAdapter(getActivity(),poiHelpers);
        ListView listView =(getView()).findViewById(R.id.poilist);
        listView.setAdapter(poiAdapter);
    }
}

//api key for poi open trip map = 5ae2e3f221c38a28845f05b6783632b01e3f50dbacf1025007a4cb04