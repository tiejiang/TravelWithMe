package com.demo.travelsociety;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.demo.travelsociety.adapter.HistoryAdapter;
import com.demo.travelsociety.custom_view.DrawableCenterTextView;
import com.demo.travelsociety.custom_view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索页面
 */
public class SearchActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private ImageView back;
    private EditText et_search_text;
    private ImageView iv_search_delete;
    private TextView tv_change, tv_city_change;
    private MyGridView gv_sight_spot, gv_hot_city;
    private ListView list_history;
    private String searchText;
    private String sharePre = "SearchHistory";
    private ArrayList<String> lists;
    private HistoryAdapter historyAdapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> changelist = new ArrayList<Map<String, Object>>();
    private SimpleAdapter sightAdapter, cityAdapter;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getData();
        initView();
        mLocationClient = new LocationClient(getApplicationContext());
        // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        // 注册监听函数


        initLocation();
        initAutoComplete("history", et_search_text);
    }


    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    SharedPreferences pfYL = TravelSocietyApplication.sContext.getSharedPreferences(
            "location", 0);
    SharedPreferences.Editor edYL = pfYL.edit();
    double lon, lat;

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息
            lon = location.getLongitude();
            lat = location.getLatitude();

            edYL.putString("lon", String.valueOf(lon));
            edYL.putString("lat", String.valueOf(lat));
            edYL.commit();
            Log.d("loobot", "loobotdebug lon=" + lon);
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                Log.d("loobot", "loobotdebug GPS定位结果" + location.getAddrStr());
                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                //Log.d("loobot", "loobotdebug 网络定位结果"+sb.toString()+"  city="+location.getCityCode()+location.getCity());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                Log.d("loobot", "loobotdebug 离线定位结果" + location.getAddrStr());
                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Log.d("loobot", "loobotdebug 服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Log.d("loobot", "loobotdebug 网络不同导致定位失败，请检查网络是否通畅");
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Log.d("loobot", "loobotdebug 无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
           // Log.i("BaiduLocationApiDem loobotdebug", sb.toString());
            mLocationClient.stop();
        }

        public void onConnectHotSpotMessage(String arg0, int arg1) {
            // TODO Auto-generated method stub
            Log.d("loobot", "loobotdebug onConnectHotSpotMessage");
        }

    }


    /**
     * 初始化控件
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        et_search_text = (EditText) findViewById(R.id.et_search_text);
        iv_search_delete = (ImageView) findViewById(R.id.iv_search_delete);
        iv_search_delete.setOnClickListener(this);
        et_search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    iv_search_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_search_delete.setVisibility(View.GONE);
                }
            }
        });
        et_search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(et_search_text.getText().toString().trim())) {
                        Toast.makeText(getApplicationContext(), R.string.input_content, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    saveHistory("history", et_search_text);
                    // 先隐藏键盘
                    ((InputMethodManager) et_search_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    searchText = et_search_text.getText().toString().trim();
//                    startSearch(searchText);
                    return true;
                }
                return false;
            }
        });

        View head_view = View.inflate(this, R.layout.head_history_list, null);
        tv_change = (TextView) head_view.findViewById(R.id.tv_change);//景点
        tv_change.setOnClickListener(this);
        gv_sight_spot = (MyGridView) head_view.findViewById(R.id.gv_sight_spot);
        sightAdapter = new SimpleAdapter(this, list,
                R.layout.item_text,
                new String[]{"name"},
                new int[]{R.id.text});
        gv_sight_spot.setAdapter(sightAdapter);
        gv_sight_spot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                String searText = (String) map.get("name");
                et_search_text.setText(searText);
                searchText = searText;

                if (mLocationClient != null) {
                    mLocationClient.start();
                } else {
                    Log.d("loobot", "loobotdebug mLocationClient==null");
                }

                Toast.makeText(SearchActivity.this, "gv_sight_spot" + searText + "经度=" + 	pfYL.getString("lat", "") + "纬度=" + 	pfYL.getString("lon", ""), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tv_city_change = (TextView) head_view.findViewById(R.id.tv_city_change);//城市
        tv_city_change.setOnClickListener(this);
        gv_hot_city = (MyGridView) head_view.findViewById(R.id.gv_hot_city);
        cityAdapter = new SimpleAdapter(this, list,
                R.layout.item_text,
                new String[]{"name"},
                new int[]{R.id.text});
        gv_hot_city.setAdapter(cityAdapter);
        gv_hot_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getAdapter().getItem(i);
                String searText = (String) map.get("name");
                et_search_text.setText(searText);
                searchText = searText;
                Toast.makeText(SearchActivity.this, "gv_hot_city" + searText, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        list_history = (ListView) findViewById(R.id.list_history); // 历史记录
        list_history.setOnItemClickListener(this);
        list_history.addHeaderView(head_view);

    }

    /**
     * demo数据
     *
     * @return
     */
    private void getData() {
        //map.put(参数名字,参数值)
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "大理");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "香格里拉");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "玉龙雪山");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "白云山");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "莲花生");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "玉龙雪山");
        list.add(map);
    }

    /**
     * 保存搜索记录
     */
    protected void saveHistory(String field, EditText auto) {
        String text = auto.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences(sharePre, 0);
        String longhistory = sp.getString(field, "");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString(field, sb.toString()).commit();
        }
    }

    /**
     * 显示历史记录
     */
    private void initAutoComplete(String field, EditText auto) {
        SharedPreferences sp = getSharedPreferences(sharePre, 0);
        String longhistory = sp.getString(field, "");
        if (!TextUtils.isEmpty(longhistory)) {
            String[] hisArrays = longhistory.split(",");
            lists = new ArrayList<>();
            for (int i = 0; i < hisArrays.length; i++) {
                lists.add(hisArrays[i]);
                if (i == 9) {
                    break;
                }
            }
            addFooterView();
        }
        historyAdapter = new HistoryAdapter(this, lists, sharePre);
        list_history.setAdapter(historyAdapter);
    }

    /**
     * 添加底部清空历史记录
     */
    private void addFooterView() {
        final LinearLayout ly = new LinearLayout(this);
        ly.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        ly.setGravity(Gravity.CENTER);
        ly.setBackgroundResource(R.drawable.item_list_bg);
        final DrawableCenterTextView footer_view = new DrawableCenterTextView(this);
        footer_view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, 120));
        footer_view.setTextSize(14);
        Drawable drawable = getResources().getDrawable(R.mipmap.history_delete);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        footer_view.setCompoundDrawables(drawable, null, null, null);
        footer_view.setTextColor(Color.parseColor("#999999"));
        footer_view.setText(R.string.history_record);
        footer_view.setCompoundDrawablePadding(20);
        footer_view.setGravity(Gravity.CENTER);
        ly.addView(footer_view);
        list_history.addFooterView(ly, null, true);
        ly.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(sharePre, 0);
                sp.edit().putString("history", "").commit();
                lists.clear();
                list_history.removeFooterView(ly);
                historyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        String searText = (String) parent.getAdapter().getItem(position);
        et_search_text.setText(searText);
        searchText = searText;
//        startSearch(searchText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.iv_search_delete) {
            et_search_text.setText("");
        } else if (id == R.id.tv_change) {//景点刷新

        } else if (id == R.id.tv_city_change) {//城市刷新

        }
    }
}
