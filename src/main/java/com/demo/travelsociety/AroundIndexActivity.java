package com.demo.travelsociety;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.demo.travelsociety.fragment.CityTourFragment;
import com.demo.travelsociety.fragment.TogetherTourFragment;
import com.demo.travelsociety.travelNear.fragment.AroundTourFragment;
/**
 * 周边游主页
 * author:xjj
 */
public class AroundIndexActivity extends AppCompatActivity implements OnTabSelectedListener{
    private TabLayout tab_layout;
    private CityTourFragment cityTourFragment;
    private AroundTourFragment aroundTourFragment;
    private TogetherTourFragment togetherTourFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_index);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        //动态与相册tab
        tab_layout = (TabLayout)findViewById(R.id.tab_layout);
        tab_layout.setOnTabSelectedListener(this);

        TabLayout.Tab tab1 = tab_layout.newTab().setText("市内游").setTag("citytour");
        tab_layout.addTab(tab1, true);
        TabLayout.Tab tab2 = tab_layout.newTab().setText("周边游").setTag("aroundtour");
        tab_layout.addTab(tab2, false);

        TabLayout.Tab tab3 = tab_layout.newTab().setText("结伴").setTag("togethertour");
        tab_layout.addTab(tab3, false);
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        String tag = (String)tab.getTag();



        if ("citytour".equals(tag)){
            if (cityTourFragment == null) {
                cityTourFragment = new CityTourFragment();
            }
            CityTourFragment fragment = (CityTourFragment)fm.findFragmentByTag("citytour");
            if (fragment != null) {
                tx.hide(togetherTourFragment);
                tx.hide(aroundTourFragment);
                tx.show(cityTourFragment);
            }else {
                tx.add(R.id.content, cityTourFragment, "dynamics");
            }
        }else if ("aroundtour".equals(tag)){

            if (aroundTourFragment == null) {
                aroundTourFragment = new AroundTourFragment();
            }
            CityTourFragment fragment = (CityTourFragment)fm.findFragmentByTag("aroundtour");
            tx.hide(cityTourFragment);
            tx.hide(togetherTourFragment);
            if (fragment != null) {
                tx.show(aroundTourFragment);
            }else {
                tx.add(R.id.content, aroundTourFragment, "aroundtour");
            }
        }else if ("togethertour".equals(tag)){


            if (togetherTourFragment == null) {
                togetherTourFragment = new TogetherTourFragment();
            }
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
