package com.example.kimhyunwoo.runtogether.upperactivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kimhyunwoo.runtogether.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperFragment extends Fragment {

    Button buttonInfo = null;
    Button buttonBluetooth = null;
    Button buttonLogout = null;

    public UpperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upper, container, false);
        // Inflate the layout for this fragment

        buttonInfo = view.findViewById(R.id.btn_info);
        buttonBluetooth = view.findViewById(R.id.btn_bluetooth);
        buttonLogout = view.findViewById(R.id.btn_logout);

        buttonInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }
        });

        buttonBluetooth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}
