package com.inetgoes.fangdd.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inetgoes.fangdd.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class RentHouseFragment extends Fragment {


    public RentHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sec_hand, container, false);
    }


}
