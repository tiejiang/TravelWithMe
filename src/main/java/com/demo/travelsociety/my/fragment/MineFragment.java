package com.demo.travelsociety.my.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.travelsociety.EditPersonalHomePageActivity;
import com.demo.travelsociety.MyAlbumActivity;
import com.demo.travelsociety.MyDynamicsActivity;
import com.demo.travelsociety.R;

/**
 * “我的”页面
 */
public class MineFragment extends Fragment implements OnClickListener{
    private LinearLayout ly_mine_picture, ly_mine_want;
    private LinearLayout ly_mine_mydynamics, ly_mine_myalbum;
    private TextView iv_title_edit;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        iv_title_edit = (TextView)view.findViewById(R.id.iv_title_edit);
        iv_title_edit.setOnClickListener(this);

        ly_mine_picture = (LinearLayout)view.findViewById(R.id.ly_mine_picture);
        ly_mine_want = (LinearLayout)view.findViewById(R.id.ly_mine_want);

        ly_mine_mydynamics = (LinearLayout)view.findViewById(R.id.ly_mine_mydynamics);
        ly_mine_mydynamics.setOnClickListener(this);
        ly_mine_myalbum = (LinearLayout)view.findViewById(R.id.ly_mine_myalbum);
        ly_mine_myalbum.setOnClickListener(this);

        initPictureView(ly_mine_picture, 5);
        initWantView(ly_mine_want, 4);
        return view;
    }

    /**
     * 填充数据
     */
    private void initPictureView(LinearLayout ly, int datas){
        ly.removeAllViews();
        for (int i=0; i<datas; i++){
            View view = View.inflate(getActivity(), R.layout.item_dynamics_picture, null);
            ly.addView(view);
        }
    }

    private void initWantView(LinearLayout ly, int datas){
        ly.removeAllViews();
        for (int i=0; i<datas; i++){
            View view = View.inflate(getActivity(), R.layout.item_dynamics_want, null);
            ly.addView(view);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;
        if (id == R.id.ly_mine_mydynamics){
            intent = new Intent(getActivity(), MyDynamicsActivity.class);

        }else if (id == R.id.ly_mine_myalbum){
            intent = new Intent(getActivity(), MyAlbumActivity.class);

        }else if (id == R.id.iv_title_edit){
            intent = new Intent(getActivity(), EditPersonalHomePageActivity.class);
        }
        startActivity(intent);
    }
}
