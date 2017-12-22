package com.greenapex.callhelper.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenapex.callhelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Add extends Fragment {

    public static Add newInstance() {
        Add fragment = new Add();
        return fragment;
    }

    public Add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

}
