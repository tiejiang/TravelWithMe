package com.demo.travelsociety.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.travelsociety.R;
import com.demo.travelsociety.travelNear.fragment.AroundTourFragment;



/**
 * A simple {@link Fragment} subclass.
 */
public class AroundHomeFragment extends Fragment   implements TabLayout.OnTabSelectedListener {
    private TabLayout tab_layout;
    private CityTourFragment cityTourFragment;

    private AroundTourFragment aroundTourFragment;
    private TogetherTourFragment togetherTourFragment;
    public AroundHomeFragment() {
        // Required empty public constructor
    }


    /**
     * 初始化控件
     */
    private void initView(){
        //动态与相册tab



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_around_index, container, false);
        tab_layout = (TabLayout)view.findViewById(R.id.tab_layout);
        tab_layout.setOnTabSelectedListener(this);

        TabLayout.Tab tab1 = tab_layout.newTab().setText("市内游").setTag("citytour");
        tab_layout.addTab(tab1, true);
        TabLayout.Tab tab2 = tab_layout.newTab().setText("周边游").setTag("aroundtour");
        tab_layout.addTab(tab2, false);

        TabLayout.Tab tab3 = tab_layout.newTab().setText("结伴").setTag("togethertour");
        tab_layout.addTab(tab3, false);

       // btnPublish.setOnClickListener(this);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        String tag = (String)tab.getTag();
        if (cityTourFragment == null) {
            cityTourFragment = new CityTourFragment();
        }
        if (aroundTourFragment == null) {
            aroundTourFragment = new AroundTourFragment();
        }
        if (togetherTourFragment == null) {
            togetherTourFragment = new TogetherTourFragment();
        }
        if ("citytour".equals(tag)){

            CityTourFragment fragment = (CityTourFragment)fm.findFragmentByTag("citytour");
            if (fragment != null) {
                tx.hide(togetherTourFragment);
                tx.hide(aroundTourFragment);
                tx.show(cityTourFragment);
            }else {
                tx.add(R.id.content, cityTourFragment, "citytour");
            }
        }else if ("aroundtour".equals(tag)){


            AroundTourFragment fragment = (AroundTourFragment)fm.findFragmentByTag("aroundtour");
            tx.hide(cityTourFragment);
            tx.hide(togetherTourFragment);
            if (fragment != null) {
                tx.show(aroundTourFragment);
            }else {
                tx.add(R.id.content, aroundTourFragment, "aroundtour");
            }
        }else if ("togethertour".equals(tag)){



            TogetherTourFragment fragment = (TogetherTourFragment)fm.findFragmentByTag("togethertour");
            tx.hide(cityTourFragment);
            tx.hide(aroundTourFragment);
            if (fragment != null) {
                tx.show(togetherTourFragment);
            }else {
                tx.add(R.id.content, togetherTourFragment, "togethertour");
            }
        }
        tx.commit();
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
