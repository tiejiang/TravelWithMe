package com.demo.travelsociety.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.demo.travelsociety.R;
import com.demo.travelsociety.adapter.AlbumAdapter;
import com.demo.travelsociety.recyclerview.FullyGridLayoutManager;

/**
 * 相册
 */
public class AlbumFragment extends Fragment {
    private RecyclerView list;
    private int screenWidth;

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamics, container, false);
        list = (RecyclerView)view.findViewById(R.id.list);
        list.setBackgroundResource(R.color.white);
        //设置布局管理器
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(),2);
        manager.setOrientation(OrientationHelper.VERTICAL);
        list.setNestedScrollingEnabled(false);
        list.setHasFixedSize(false);
        list.setLayoutManager(manager);
        //设置adapter
        AlbumAdapter mAdapter = new AlbumAdapter(getActivity());
        list.setAdapter(mAdapter);
        return view;
    }



}
