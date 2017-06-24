package com.demo.travelsociety;

import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.widget.PopupWindow;

import com.demo.travelsociety.fragment.AlbumFragment;
import com.demo.travelsociety.fragment.DynamicsFragment;

/**
 * 个人主页
 */
public class PersonalHomePageActivity extends AppCompatActivity implements OnTabSelectedListener, View.OnClickListener{
    private ImageView back, iv_title_more;
    private LinearLayout ly_personal_picture, ly_personal_want;
    private TabLayout tab_layout;
    private DynamicsFragment dynamicsFragment;
    private AlbumFragment albumFragment;
    private PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home_page);

        initView();
        initPictureView(ly_personal_picture, 5);
        initWantView(ly_personal_want, 4);
        initPopupMenu();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        iv_title_more = (ImageView)findViewById(R.id.iv_title_more);
        iv_title_more.setOnClickListener(this);
        //个人图片
        ly_personal_picture = (LinearLayout)findViewById(R.id.ly_personal_picture);
        //想去的地方
        ly_personal_want = (LinearLayout)findViewById(R.id.ly_personal_want);

        //动态与相册tab
        tab_layout = (TabLayout)findViewById(R.id.tab_layout);
        tab_layout.setOnTabSelectedListener(this);

        TabLayout.Tab tab1 = tab_layout.newTab().setText("他的动态").setTag("dynamics");
        tab_layout.addTab(tab1, true);
        TabLayout.Tab tab2 = tab_layout.newTab().setText("旅游相册").setTag("album");
        tab_layout.addTab(tab2, false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back){
            finish();
        }else if (id == R.id.iv_title_more){
//            initPopupMenu();
        }
    }
    /**
     * 弹出菜单
     */
    private void initPopupMenu(){
        View view = getLayoutInflater().inflate(R.layout.pop_layout, null);
        pop = new PopupWindow(view, getResources().getDimensionPixelSize(R.dimen.pop_width), ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true); // PopupWindow获取焦点
        pop.setTouchable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable()); // 点击PopWindow外界面关闭弹窗
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        backgroundAlpha(0.5f);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        if (pop.isShowing()){
            pop.dismiss();
        }else {
            pop.showAsDropDown(iv_title_more, 0, -10);
        }
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 填充数据
     */
    private void initPictureView(LinearLayout ly, int datas){
        ly.removeAllViews();
        for (int i=0; i<datas; i++){
            View view = View.inflate(this, R.layout.item_dynamics_picture, null);
            ly.addView(view);
        }
    }

    private void initWantView(LinearLayout ly, int datas){
        ly.removeAllViews();
        for (int i=0; i<datas; i++){
            View view = View.inflate(this, R.layout.item_dynamics_want, null);
            ly.addView(view);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        String tag = (String)tab.getTag();
        if ("dynamics".equals(tag)){
            if (dynamicsFragment == null) {
                dynamicsFragment = new DynamicsFragment();
            }
            DynamicsFragment fragment = (DynamicsFragment)fm.findFragmentByTag("dynamics");
            if (fragment != null) {
                tx.hide(albumFragment);
                tx.show(dynamicsFragment);
            }else {
                tx.add(R.id.content, dynamicsFragment, "dynamics");
            }
        }else if ("album".equals(tag)){
            if (albumFragment == null) {
                albumFragment = new AlbumFragment();
            }
            AlbumFragment fragment = (AlbumFragment)fm.findFragmentByTag("album");
            tx.hide(dynamicsFragment);
            if (fragment != null) {
                tx.show(albumFragment);
            }else {
                tx.add(R.id.content, albumFragment, "album");
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
