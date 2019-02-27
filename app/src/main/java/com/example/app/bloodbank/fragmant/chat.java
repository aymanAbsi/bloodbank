package com.example.app.bloodbank.fragmant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.bloodbank.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class chat extends Fragment {

private  String cahtuser ;
    public chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chat, container, false);
        Bundle bundle = new Bundle();
         //cahtuser = bundle.getArguments().getString("user_id");

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(cahtuser);


        return v ;
    }

}
