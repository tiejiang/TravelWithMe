package com.demo.travelsociety;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.demo.travelsociety.adapter.DropListAdapter;
import com.demo.travelsociety.adapter.SightPersonAdapter;
import com.demo.travelsociety.custom_view.DropdownButton;
import com.demo.travelsociety.db.bean.OrgBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  景点页面
 */
public class SightSpotListActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener{
    private ImageView back, iv_title_search, iv_title_more;
    private List<OrgBean> ageDatas;
    private List<OrgBean> sexDatas;
    private List<OrgBean> jobDatas;
    private List<OrgBean> authDatas;
    private ListView list;
    private SightPersonAdapter sightPersonAdapter;
    private PopupWindow pop;
    private DropdownButton dbtn_area, dbtn_age, dbtn_sex, dbtn_job, dbtn_auth;
    private String areaName = "地区";
    private String ageName = "年龄";
    private String sexName = "性别";
    private String jobName = "职业";
    private String authName = "认证";
    private String areaCode = "";
    private String ageCode = "";
    private String sexCode = "";
    private String jobCode = "";
    private String authCode = "";
    private LinearLayout popLay;
    private ListView popList;
    private LinearLayout blankSpace;
    private DropListAdapter dropListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_spot_list);

        demoDatas();
        initView();
    }

    private void demoDatas(){
        ageDatas = new ArrayList<>();
        List<String> ageList = Arrays.asList("不限", "18岁以下", "18-22岁","23-26岁","27-35岁","35岁以上");
        for (int i = 0; i<ageList.size(); i++){
            OrgBean ageBean = new OrgBean();
            ageBean.setName(ageList.get(i));
            ageBean.setOptType("age");
            ageDatas.add(ageBean);
        }
        sexDatas = new ArrayList<>();
        List<String> sexList = Arrays.asList("不限", "男", "女");
        for (int i = 0; i<sexList.size(); i++){
            OrgBean ageBean = new OrgBean();
            ageBean.setName(sexList.get(i));
            ageBean.setOptType("sex");
            sexDatas.add(ageBean);
        }
        jobDatas = new ArrayList<>();
        List<String> jobList = Arrays.asList("不限", "学生", "上班族","企业家","自由职业","个体经营", "行业高管");
        for (int i = 0; i<jobList.size(); i++){
            OrgBean ageBean = new OrgBean();
            ageBean.setName(jobList.get(i));
            ageBean.setOptType("job");
            jobDatas.add(ageBean);
        }
        authDatas = new ArrayList<>();
        List<String> scoreList = Arrays.asList("不限", "已认证", "未认证");
        for (int i = 0; i<scoreList.size(); i++){
            OrgBean ageBean = new OrgBean();
            ageBean.setName(scoreList.get(i));
            ageBean.setOptType("auth");
            authDatas.add(ageBean);
        }
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        iv_title_search = (ImageView)findViewById(R.id.iv_title_search);
        iv_title_search.setOnClickListener(this);

        iv_title_more = (ImageView)findViewById(R.id.iv_title_more);
        iv_title_more.setOnClickListener(this);

        list = (ListView)findViewById(R.id.list);
        sightPersonAdapter = new SightPersonAdapter(this);
        list.setAdapter(sightPersonAdapter);
        list.setOnItemClickListener(this);

        //选择栏
        dbtn_area = (DropdownButton)findViewById(R.id.dbtn_area); //地区选择
        dbtn_area.setOnClickListener(this);
        dbtn_area.setChecked(false);
        dbtn_area.setText(areaName);

        dbtn_age = (DropdownButton)findViewById(R.id.dbtn_age); //年龄选择
        dbtn_age.setOnClickListener(this);
        dbtn_age.setChecked(false);
        dbtn_age.setText(ageName);

        dbtn_sex = (DropdownButton)findViewById(R.id.dbtn_sex); //性别选择
        dbtn_sex.setOnClickListener(this);
        dbtn_sex.setChecked(false);
        dbtn_sex.setText(sexName);

        dbtn_job = (DropdownButton)findViewById(R.id.dbtn_job); //职业选择
        dbtn_job.setOnClickListener(this);
        dbtn_job.setChecked(false);
        dbtn_job.setText(jobName);

        dbtn_auth = (DropdownButton)findViewById(R.id.dbtn_auth); //认证选择
        dbtn_auth.setOnClickListener(this);
        dbtn_auth.setChecked(false);
        dbtn_auth.setText(authName);

        popLay = (LinearLayout)findViewById(R.id.pop); // 下拉界面
        popLay.setVisibility(View.GONE);
        popList = (ListView)findViewById(R.id.lv_more_select); //下拉显示的内容
        blankSpace = (LinearLayout)findViewById(R.id.downlist); //下拉显示的空白区域
        blankSpace.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back){
            finish();
        }else if (id == R.id.iv_title_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }else if (id == R.id.iv_title_more){
            initPopupMenu();
        }else if (id == R.id.dbtn_area){//点击地区
            Intent intent = new Intent(this, CitySelecterActivity.class);
            startActivityForResult(intent, 1);
        }else if (id == R.id.dbtn_age){//点击年龄
            if (dbtn_age.getChecked()) {
                initPopupwindowData(ageDatas);
                dbtn_age.setChecked(true);
                dbtn_sex.setChecked(false);
                dbtn_job.setChecked(false);
                dbtn_auth.setChecked(false);
                popLay.setVisibility(View.VISIBLE);
            }else {
                reset();
            }
        }else if (id == R.id.dbtn_sex){//点击性别
            if (dbtn_sex.getChecked()) {
                initPopupwindowData(sexDatas);
                dbtn_age.setChecked(false);
                dbtn_sex.setChecked(true);
                dbtn_job.setChecked(false);
                dbtn_auth.setChecked(false);
                popLay.setVisibility(View.VISIBLE);
            }else {
                reset();
            }
        }else if (id == R.id.dbtn_job){//点击职业
            if (dbtn_job.getChecked()) {
                initPopupwindowData(jobDatas);
                dbtn_age.setChecked(false);
                dbtn_sex.setChecked(false);
                dbtn_job.setChecked(true);
                dbtn_auth.setChecked(false);
                popLay.setVisibility(View.VISIBLE);
            }else {
                reset();
            }
        }else if (id == R.id.dbtn_auth){//点击认证
            if (dbtn_auth.getChecked()) {
                initPopupwindowData(authDatas);
                dbtn_age.setChecked(false);
                dbtn_sex.setChecked(false);
                dbtn_job.setChecked(false);
                dbtn_auth.setChecked(true);
                popLay.setVisibility(View.VISIBLE);
            }else {
                reset();
            }
        }else if (id == R.id.downlist){
            popLay.setVisibility(View.GONE);
            reset();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1){
            if (data != null){
                String cityName = data.getStringExtra("cityName");
                dbtn_area.setText(cityName);
                areaName = cityName;
//                areaCode = code;
            }
        }
    }

    /**
     * 下拉框赋值
     * @param data
     */
    private void initPopupwindowData(List<OrgBean> data){
        dropListAdapter = new DropListAdapter(this, data);
        popList.setAdapter(dropListAdapter);
        popList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrgBean itemOrg = (OrgBean)adapterView.getItemAtPosition(i);
                String type = itemOrg.getOptType();
                String name = itemOrg.getName();
                String code = itemOrg.getCode();
                dropListAdapter.setChecked(i);
                dropListAdapter.notifyDataSetInvalidated();

                if ("age".equals(type)){
                    dbtn_age.setText(name);
                    ageName = name;
                    ageCode = code;
                }else if ("sex".equals(type)) {
                    dbtn_sex.setText(name);
                    sexName = name;
                    sexCode = code;
                }else if ("job".equals(type)) {
                    dbtn_job.setText(name);
                    jobName = name;
                    jobCode = code;
                }else if ("auth".equals(type)) {
                    dbtn_auth.setText(name);
                    authName = name;
                    authCode = code;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                }, 500);
            }
        });
    }

    /**
     * 重置控件
     */
    private void reset(){
        dbtn_age.setChecked(false);
        dbtn_sex.setChecked(false);
        dbtn_job.setChecked(false);
        dbtn_auth.setChecked(false);
        popLay.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, PersonalHomePageActivity.class);
        startActivity(intent);
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
}
