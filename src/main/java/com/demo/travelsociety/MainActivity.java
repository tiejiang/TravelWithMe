package com.demo.travelsociety;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.demo.travelsociety.find.fragment.FindFragment;
import com.demo.travelsociety.fragment.AroundHomeFragment;
import com.demo.travelsociety.my.fragment.MineFragment;
import com.demo.travelsociety.wantGo.fragment.WantFragment;

/**
 *  主类（容器）
 *  包含“想去”，“市内游”，“发现”，“消息”和“我的”界面
 *  默认“想去”界面
 */
public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener {
    private RadioGroup bottom_menu;
    private RadioButton bottom_want, bottom_city_tour, bottom_find, bottom_im, bottom_mine;
    private Fragment wantFragment, cityTourFragment, findFragment, imFragment, mineFragment, currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setDefaultFragment();
    }

    /**
     * 初始化widget
     */
    private void initView(){
        bottom_menu = (RadioGroup)findViewById(R.id.bottom_menu);
        bottom_menu.setOnCheckedChangeListener(this);
        bottom_want = (RadioButton)findViewById(R.id.bottom_want);
        bottom_city_tour = (RadioButton)findViewById(R.id.bottom_city_tour);
        bottom_find = (RadioButton)findViewById(R.id.bottom_find);
        bottom_im = (RadioButton)findViewById(R.id.bottom_im);
        bottom_mine = (RadioButton)findViewById(R.id.bottom_mine);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < bottom_menu.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) bottom_menu.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }

    /**
     *  起始默认显示的fragment
     */
    private void setDefaultFragment(){
        if (wantFragment == null) {
            wantFragment = new WantFragment();
        }
        if (!wantFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.fl_content, wantFragment, "want").commit();
            currentFragment = wantFragment;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.bottom_want){
            if (wantFragment == null){
                wantFragment = new WantFragment();
            }
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), wantFragment, "want");

        }else if (checkedId == R.id.bottom_city_tour){
            if (cityTourFragment == null){
                cityTourFragment = new AroundHomeFragment();
            }
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), cityTourFragment, "city_tour");

        }else if (checkedId == R.id.bottom_find){
            if (findFragment == null){
                findFragment = new FindFragment();
            }
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), findFragment, "find");

        }else if (checkedId == R.id.bottom_im){

            Intent mIntent = new Intent(MainActivity.this, xunfeiActivity.class);
            startActivity(mIntent);
//            if (imFragment == null){
//                imFragment = new IMFragment();
//            }
//            addOrShowFragment(getSupportFragmentManager().beginTransaction(), imFragment, "im");

        }else if (checkedId == R.id.bottom_mine){
            if (mineFragment == null){
                mineFragment = new MineFragment();
            }
            addOrShowFragment(getSupportFragmentManager().beginTransaction(), mineFragment, "mine");

        }
    }

    /**
     * 添加或者显示fragment
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment, String tag) {
        if (currentFragment == fragment) return;
        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
                transaction.hide(currentFragment).add(R.id.fl_content, fragment, tag).commit();
            } else {
                transaction.hide(currentFragment).show(fragment).commit();
                }
        currentFragment = fragment;
        }

}
