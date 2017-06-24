package com.demo.travelsociety.find.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.travelsociety.Gps;
import com.demo.travelsociety.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {


Button guihua;

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        guihua= (Button) view.findViewById(R.id.guihua);
        guihua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Gps.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
