package com.demo.travelsociety.wantGo.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.demo.travelsociety.BNDemoGuideActivity;
import com.demo.travelsociety.R;
import com.demo.travelsociety.TravelSocietyApplication;
import com.demo.travelsociety.adapter.PopWantGridAdapter;
import com.demo.travelsociety.adapter.WantBottomAdapter;
import com.demo.travelsociety.db.bean.SightSpotInfo;
import com.demo.travelsociety.utils.BNEventHandler;
import com.demo.travelsociety.utils.MyOrientationListener;
import com.demo.travelsociety.utils.PoiOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.baidu.navisdk.adapter.PackageUtil.getSdcardDir;

/**
 * 想去界面
 */
public class WantFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener,
        OnMapClickListener, BaiduMap.OnMapLongClickListener, OnMapStatusChangeListener {
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private BitmapDescriptor bitmapDescriptor;
    private MyLocationListener mLocationListener;
    private List<SightSpotInfo> lists = new ArrayList<>();
    private List<SightSpotInfo> cityList = new ArrayList<>();// 推荐城市
    private List<SightSpotInfo> sightList = new ArrayList<>(); // 推荐景点
    private List<SightSpotInfo> demo1, demo2, demo3;
    private List<Marker> markers = new ArrayList<>();
    private List<SightSpotInfo> spotInfos; // 推荐景点
    private boolean isFirstLocation = true;
    private RadioGroup want_group;
    private RadioButton tv_want_city, tv_want_sight_spot;
    private BitmapDescriptor ic;
    private BitmapDescriptor markIc, sel;
    private View pop;
    private EditText et_want_head_search;
    private ImageView iv_want;
    private LinearLayout linearWant;
    private Button wantConfirm;
    private EditText want;
    private FrameLayout hzl_want_bottom;
    private PopupWindow popWant;
    private boolean isShowDelete = false;
    private PopWantGridAdapter adapter;
    private List<String> bottomDatas = new ArrayList<>();
    private ViewPager mViewPager;
    private WantBottomAdapter bottomAdapter;
    private boolean isBottomShow;
    private PoiSearch mPoiSearch;
    public static List<Activity> activityList = new LinkedList<Activity>();

    private double Nowlat, Nowlon;
    private double GOlat, GOlon;

    public WantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("loobotdebugWantFragment", "onCreate()");
        super.onCreate(savedInstanceState);
        activityList.add(getActivity());
        ic = BitmapDescriptorFactory.fromResource(R.mipmap.mark);
        markIc = BitmapDescriptorFactory.fromResource(R.mipmap.sight_mark);
        sel = BitmapDescriptorFactory.fromResource(R.mipmap.map_selected);

        //定位
        initLocation();
        initOritationListener();
        //获取推荐景点
        getRecommendSightSpots();
        getDemoZoom();
        BNOuterLogUtil.setLogSwitcher(true);
        if (initDirs()) {
            initNavi();
        }
    }

    @SuppressLint("NewApi")
    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(TravelSocietyApplication.sContext, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(TravelSocietyApplication.sContext, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(TravelSocietyApplication.sContext, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Log.d("loobotdebug", "mSDCardPath=" + mSDCardPath);
                Toast.makeText(TravelSocietyApplication.sContext, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    String authinfo = null;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    Log.e("loobotd", " TTS case1");
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    Log.e("loobotd", " TTS case2");
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9354030");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "stopTTS");
        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "phoneCalling");
        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            Log.e("loobotd test_TTS", "getTTSState");
            return 1;
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
            Log.e("loobotd", " TTS play end");
        }

        @Override
        public void playStart() {
            Log.e("loobotd", " TTS play start");
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 拿到景点信息封装成SightSpotInfo对象
     */
    private void getRecommendSightSpots() {
        SightSpotInfo infos1 = new SightSpotInfo();
        infos1.setLatitude(39.91667);
        infos1.setLongitude(116.41667);
        infos1.setName("北京");
        infos1.setMyWant(false);
        infos1.setId(0);
        infos1.setImageId(R.mipmap.mark);
        infos1.setPosition(0);
        cityList.add(infos1);

        SightSpotInfo infos2 = new SightSpotInfo();
        infos2.setLatitude(34.50000);
        infos2.setLongitude(121.43333);
        infos2.setName("上海");
        infos2.setMyWant(false);
        infos2.setId(1);
        infos2.setImageId(R.mipmap.mark);
        infos2.setPosition(1);
        cityList.add(infos2);

        SightSpotInfo infos3 = new SightSpotInfo();
        infos3.setLatitude(34.76667);
        infos3.setLongitude(113.65000);
        infos3.setName("郑州");
        infos3.setMyWant(false);
        infos3.setId(2);
        infos3.setImageId(R.mipmap.mark);
        infos3.setPosition(2);
        cityList.add(infos3);

        SightSpotInfo infos4 = new SightSpotInfo();
        infos4.setLatitude(36.04);
        infos4.setLongitude(103.51);
        infos4.setName("兰州");
        infos4.setMyWant(false);
        infos4.setId(3);
        infos4.setImageId(R.mipmap.mark);
        infos4.setPosition(3);
        cityList.add(infos4);

        SightSpotInfo infos5 = new SightSpotInfo();
        infos5.setLatitude(43.76667);
        infos5.setLongitude(87.68333);
        infos5.setName("乌鲁木齐");
        infos5.setMyWant(false);
        infos5.setId(4);
        infos5.setImageId(R.mipmap.mark);
        infos5.setPosition(4);
        cityList.add(infos5);
        lists.addAll(cityList);
    }

    private void getDemoZoom() {
        demo1 = new ArrayList<>();
        demo2 = new ArrayList<>();
        demo3 = new ArrayList<>();

        SightSpotInfo infos1 = new SightSpotInfo();
        infos1.setLatitude(22.481111);
        infos1.setLongitude(108.192222);
        infos1.setName("长白山");
        infos1.setMyWant(false);
        infos1.setId(5);
        infos1.setImageId(R.mipmap.mark);
        infos1.setPosition(0);
        sightList.add(infos1);

        SightSpotInfo infos2 = new SightSpotInfo();
        infos2.setLatitude(25.171111);
        infos2.setLongitude(110.172222);
        infos2.setName("神农架");
        infos2.setMyWant(false);
        infos2.setId(6);
        infos2.setImageId(R.mipmap.mark);
        infos2.setPosition(1);
        sightList.add(infos2);

        SightSpotInfo infos3 = new SightSpotInfo();
        infos3.setLatitude(23.061111);
        infos3.setLongitude(109.362222);
        infos3.setName("白云山");
        infos3.setMyWant(false);
        infos3.setId(7);
        infos3.setImageId(R.mipmap.mark);
        infos3.setPosition(2);
        sightList.add(infos3);

/////////////////////////////////////////////////////////
        SightSpotInfo infos4 = new SightSpotInfo();
        infos4.setLatitude(36.041111);
        infos4.setLongitude(103.512222);
        demo2.add(infos4);

        SightSpotInfo infos5 = new SightSpotInfo();
        infos5.setLatitude(35.371111);
        infos5.setLongitude(103.122222);
        demo2.add(infos5);

        SightSpotInfo infos6 = new SightSpotInfo();
        infos6.setLatitude(40.081111);
        infos6.setLongitude(94.412222);
        demo2.add(infos6);

/////////////////////////////////////////////////////////
        SightSpotInfo infos7 = new SightSpotInfo();
        infos7.setLatitude(23.161111);
        infos7.setLongitude(116.362222);
        demo3.add(infos7);

        SightSpotInfo infos8 = new SightSpotInfo();
        infos8.setLatitude(23.401111);
        infos8.setLongitude(116.382222);
        demo3.add(infos8);

        SightSpotInfo infos9 = new SightSpotInfo();
        infos9.setLatitude(23.281111);
        infos9.setLongitude(116.462222);
        demo3.add(infos9);

    }

    //显示marker
    private void addOverlay(final List<SightSpotInfo> list, int curPosition) {
        //清空地图
        mBaiduMap.clear();
        markers.clear();
        LatLng latLng = null;
        BitmapDescriptor mIconMaker = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (int i = 0; i < list.size(); i++) {
            // 位置
            latLng = new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude());
            //图标
            mIconMaker = BitmapDescriptorFactory.fromResource(list.get(i).getImageId());

            overlayOptions = new MarkerOptions().position(latLng).icon(mIconMaker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));

            Bundle bundle = new Bundle();
            bundle.putParcelable("info", list.get(i));
            marker.setExtraInfo(bundle);
            marker.setTitle(list.get(i).getName());
            markers.add(marker);
        }
        if (isBottomShow) {
            Marker curMarker = markers.get(curPosition);
            curMarker.setIcon(sel);
            pop = View.inflate(getActivity(), R.layout.marker_pop, null);
            TextView text = (TextView) pop.findViewById(R.id.text);
            text.setText(curMarker.getTitle());
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(pop);
            LatLng ll = curMarker.getPosition();
            InfoWindow infoWindow = new InfoWindow(descriptor, ll, -20, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    mBaiduMap.hideInfoWindow();
                }
            });
            mBaiduMap.showInfoWindow(infoWindow);
            //将地图移到该位置
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.setMapStatus(u);
        } else {
            //将地图显示在最后一个marker的位置
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(msu);
        }


    }

    /**
     * 点击地图覆盖物底部弹出相关信息
     */
    private void showSightSpotInfo(int position) {
        hzl_want_bottom.setVisibility(View.VISIBLE);
        isBottomShow = true;
        mViewPager.setCurrentItem(position);
    }

    /**
     * 重置图标
     */
    private void resetMarker() {
        mBaiduMap.hideInfoWindow();
        hzl_want_bottom.setVisibility(View.GONE);
        isBottomShow = false;
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).setIcon(ic);
        }
    }

    private void initLocation() {
        //定义自己图标
//        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        //定位客户端的设置
        mLocationClient = new LocationClient(getActivity());
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(60000);//1000毫秒定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    int searchType = 0;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);

    private Marker mMarkerA;
    private InfoWindow mInfoWindow;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("WantFragment", "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_want, container, false);

        et_want_head_search = (EditText) view.findViewById(R.id.et_want_head_search);//搜索
        et_want_head_search.setOnClickListener(this);

        iv_want = (ImageView) view.findViewById(R.id.iv_want);
        iv_want.setOnClickListener(this);

        wantConfirm = (Button) view.findViewById(R.id.want_confirm);
        want = (EditText) view.findViewById(R.id.want);
        linearWant = (LinearLayout) view.findViewById(R.id.linear_want);
        linearWant.setVisibility(View.INVISIBLE);
        wantConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TIEJIANG", "POI button clicked");
                mPoiSearch.searchInCity((new PoiCitySearchOption()).city("黑龙江").keyword("美食").pageNum(10));
            }
        });


        want_group = (RadioGroup) view.findViewById(R.id.want_group);
        want_group.setOnCheckedChangeListener(this);
        tv_want_city = (RadioButton) view.findViewById(R.id.tv_want_city);
        tv_want_sight_spot = (RadioButton) view.findViewById(R.id.tv_want_sight_spot);

        hzl_want_bottom = (FrameLayout) view.findViewById(R.id.hzl_want_bottom);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setPageMargin(20);
        bottomAdapter = new WantBottomAdapter(getActivity(), lists);
        mViewPager.setAdapter(bottomAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("WantFragment", "" + position);
                addOverlay(lists, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        mapView.removeViewAt(1);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMaxAndMinZoomLevel(15, 5);
        mBaiduMap.setTrafficEnabled(false);
        // 改变地图状态，使地图显示在恰当的缩放大小
//        LatLng center = new LatLng(22.361292, 114.943531);
        LatLng center = new LatLng(47.361195, 123.943459);
        MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(14).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
//        addOverlay(lists, 0);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);

        //poi检索


        //覆盖物
        LatLng llA = new LatLng(59.963175, 116.400244);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        // 掉下动画
        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(TravelSocietyApplication.sContext);
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA) {
                    button.setText("更改位置");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng ll = marker.getPosition();
                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                                    ll.longitude + 0.005);
                            marker.setPosition(llNew);
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        TravelSocietyApplication.sContext,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });

        //地图单击事件监听
//        OnMapClickListener listener = new OnMapClickListener() {
//            /**
//             * 地图单击事件回调函数
//             * @param point 点击的地理坐标
//             */
//            public void onMapClick(LatLng point){
//
//            }
//            /**
//             * 地图内 Poi 单击事件回调函数
//             * @param poi 点击的 poi 信息
//             */
//            public boolean onMapPoiClick(MapPoi poi){
//                Log.d("TIEJIANG", "onMapClicked");
//
//                return false;
//            }
//        };
//        mBaiduMap.setOnMapClickListener(listener);
        //地图POI检索


        //添加marker点击事件的监听
//        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                resetMarker();
//                //从marker中获取info信息
//                Bundle bundle = marker.getExtraInfo();
//                SightSpotInfo infoUtil = bundle.getParcelable("info");
//                marker.setIcon(sel);
//                int position = infoUtil.getPosition();
//                pop = View.inflate(getActivity(), R.layout.marker_pop, null);
//                TextView text = (TextView)pop.findViewById(R.id.text);
//                text.setText(infoUtil.getName());
//                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(pop);
//
//                LatLng ll = marker.getPosition();
//
//                InfoWindow infoWindow = new InfoWindow(descriptor, ll, -20, new InfoWindow.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick() {
//                        mBaiduMap.hideInfoWindow();
//                    }
//                });
//                mBaiduMap.showInfoWindow(infoWindow);
//                //将地图移到该位置
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                mBaiduMap.setMapStatus(u);
//
//                showSightSpotInfo(position);
//                return true;
//            }
//        });
        return view;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("TIEJIANG", "onMapClicked");
        resetMarker();
        linearWant.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
       /*int zoomlevel = (int)mapStatus.zoom;
        if (zoomlevel == 8){
            addOverlay(demo1 , type);
        }else if (zoomlevel == 11){
            addOverlay(demo2, type);
        }else if (zoomlevel == 14){
            addOverlay(demo3, type);
        }*/
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
    }

    private void poisearch(String city) {
        mPoiSearch = PoiSearch.newInstance();
        searchType = 1;
        // String citystr = "深圳";
        String keystr = et_want_head_search.getText().toString();
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(city).keyword(keystr).pageNum(0));
        OnGetPoiSearchResultListener mPoilistener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {

                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(TravelSocietyApplication.sContext, "未找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    mBaiduMap.clear();
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result);
                    overlay.addToMap();
                    overlay.zoomToSpan();

                    switch (searchType) {
                        case 2:
                            // showNearbyArea(center, radius);
                            break;
                        case 3:
                            // showBound(searchbound);
                            break;
                        default:
                            break;
                    }

                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
//                    Toast.makeText(TravelSocietyApplication.sContext, strInfo.split(",")[0], Toast.LENGTH_LONG)
//                            .show();
                    poisearch(strInfo.split(",")[0]);
                }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(TravelSocietyApplication.sContext, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(TravelSocietyApplication.sContext, " 纬度" + result.getLocation().latitude + " 经度 " + result.getLocation().longitude, Toast.LENGTH_SHORT)
                            .show();
                    GOlat = result.getLocation().latitude;
                    GOlon = result.getLocation().longitude;
                    GoAddr = result.getName();
                    dialog(result.getName() + "," + result.getAddress(), GoAddr);
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(mPoilistener);

    }


    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_want_head_search) { // 跳转到SearchActivity
//            Intent intent = new Intent(getActivity(), SearchActivity.class);
//            getActivity().startActivity(intent);
            //et_want_head_search
            //poisearch();
        } else if (id == R.id.iv_want) {// 想去按钮
            //initWantPopupWindow();
            poisearch("深圳");
        }
    }

    protected void dialog(String content, String lbs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否要去" + content);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                GPS();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void GPS() {
        Toast.makeText(TravelSocietyApplication.sContext, "go=" + BaiduNaviManager.isNaviInited(), Toast.LENGTH_SHORT).show();
        if (BaiduNaviManager.isNaviInited()) {
            routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(TravelSocietyApplication.sContext, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            routeplanToNavi(mCoordinateType);
        }

    }

    private boolean hasRequestComAuth = false;
    private boolean hasInitSuccess = false;
    private BNRoutePlanNode.CoordinateType mCoordinateType = null;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    private String mSDCardPath = null;

    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {

        mCoordinateType = coType;

        if (!hasInitSuccess) {
            Toast.makeText(TravelSocietyApplication.sContext, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(TravelSocietyApplication.sContext, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                Toast.makeText(TravelSocietyApplication.sContext, "GCJ02", Toast.LENGTH_SHORT).show();
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "", null, coType);
                break;
            }
            case WGS84: {
                Toast.makeText(TravelSocietyApplication.sContext, "WGS84", Toast.LENGTH_SHORT).show();
                sNode = new BNRoutePlanNode(Nowlon, Nowlat, NowAddr, null, coType);
                eNode = new BNRoutePlanNode(GOlon, GOlat, GoAddr, null, coType);
                //sNode = new BNRoutePlanNode(Nowlon, Nowlat, NowAddr, null, coType);

                // eNode = new BNRoutePlanNode(GOlon, GOlat, GoAddr, null, coType);
                Log.d("loobotdebug", "Nowlon=" + Nowlon + " Nowlat=" + Nowlat + " NowAddr=" + NowAddr + " sNode=" + sNode);

                Log.d("loobotdebug", "GOlon=" + GOlon + " GOlat=" + GOlat + " GoAddr=" + GoAddr + " eNode=" + eNode);
                break;
            }
            case BD09_MC: {
                Toast.makeText(TravelSocietyApplication.sContext, "BD09_MC", Toast.LENGTH_SHORT).show();
                sNode = new BNRoutePlanNode(12947471, 4846474, "", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "", null, coType);
                break;
            }
            case BD09LL: {
                Toast.makeText(TravelSocietyApplication.sContext, "BD09LL", Toast.LENGTH_SHORT).show();
                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "", null, coType);
                break;
            }
            default:
                ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new DemoRoutePlanListener(sNode),
                    eventListerner);
        }
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            Log.d("loobotdebug", "onCommonEventCall");
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
                /*
                 * 设置途径点以及resetEndNode会回调该接口
	             */
            Log.d("loobotdebug", "onJumpToNavigator-----------------------" + mBNRoutePlanNode);
//            for (Activity ac : activityList) {
//
//                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
//
//                    return;
//                }
//            }
            Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
            Log.d("loobotdebug", "onJumpToNavigator-----------------------2" + mBNRoutePlanNode);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            Log.d("loobotdebug", "onJumpToNavigator-----------------------3" + getActivity());
            intent.putExtras(bundle);
            Log.d("loobotdebug", "onJumpToNavigator-----------------------4" + intent);
            getActivity().startActivity(intent);
            Log.d("loobotdebug", "onJumpToNavigator-----------------------5" + intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(TravelSocietyApplication.sContext, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = TravelSocietyApplication.sContext.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = TravelSocietyApplication.sContext.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 想去的景点弹出框
     */
    private void initWantPopupWindow() {
        if (popWant == null) {
            HashMap<String, Object> maps;
            final List<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < 7; i++) {
                maps = new HashMap<>();
                if (i == 0) {
                    maps.put("name", "北京");
                }
                if (i == 1) {
                    maps.put("name", "天津");
                }
                if (i == 2) {
                    maps.put("name", "上海");
                }
                if (i == 3) {
                    maps.put("name", "杭州");
                }
                if (i == 4) {
                    maps.put("name", "苏州");
                }
                if (i == 5) {
                    maps.put("name", "九江");
                }
                if (i == 6) {
                    maps.put("name", "武汉");
                }
                datas.add(maps);
            }
            View view = View.inflate(getActivity(), R.layout.layout_pop_want, null);
            final TextView tv_pop_want_count = (TextView) view.findViewById(R.id.tv_pop_want_count);
            final GridView auto_pop_want_sight = (GridView) view.findViewById(R.id.auto_pop_want_sight);
            adapter = new PopWantGridAdapter(getActivity(), datas);
            auto_pop_want_sight.setAdapter(adapter);
            auto_pop_want_sight.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    if (isShowDelete) {
                        isShowDelete = false;
                    } else {
                        isShowDelete = true;
                        adapter.setIsShowDelete(isShowDelete);
                        auto_pop_want_sight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (isShowDelete) {
                                    datas.remove(i);
                                    isShowDelete = false;
                                }
                                adapter = new PopWantGridAdapter(getActivity(), datas);
                                auto_pop_want_sight.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    adapter.setIsShowDelete(isShowDelete);
                    return true;
                }
            });
            (view.findViewById(R.id.downlist)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popWant.dismiss();
                }
            });

            popWant = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popWant.setFocusable(true);
            popWant.setTouchable(true);
            popWant.setOutsideTouchable(true);
            popWant.setBackgroundDrawable(new BitmapDrawable());
        }
        popWant.showAsDropDown(iv_want, 0, 30);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.tv_want_city) { //城市
            lists.clear();
            lists.addAll(cityList);
            bottomAdapter.notifyDataSetChanged();
            addOverlay(lists, 0);
        } else if (checkedId == R.id.tv_want_sight_spot) { //景点
            lists.clear();
            lists.addAll(sightList);
            bottomAdapter.notifyDataSetChanged();
            addOverlay(lists, 0);
        }
    }

    @Override
    public void onStart() {
        Log.i("loobotd WantFragment", "onStart()" + mLocationClient.isStarted());
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();

        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i("WantFragment", "onStop()");
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }


        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.i("WantFragment", "onResume()");
        if (mapView != null) {
            mapView.onResume();
        }


        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("WantFragment", "onPause()");
        if (mapView != null) {
            mapView.onPause();
        }

        mapView = null;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("WantFragment", "onDestroy()");
        if (mapView != null) {
            mapView.onDestroy();
        }

        mapView = null;
        mPoiSearch.destroy();
        super.onDestroy();

    }


    /**
     * 长安长按出现POI搜索框
     **/
    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d("TIEJIANG", "onMapLongClicked");

    }

    private String NowAddr, GoAddr;

    /**
     * 当前的精度
     */
    private float mCurrentAccracy;
    /**
     * 方向传感器的监听器
     */
    private MyOrientationListener myOrientationListener;
    /**
     * 方向传感器X方向的值
     */
    private int mXDirection;

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                TravelSocietyApplication.sContext);
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;

                        // 构造定位数据
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(Nowlat)
                                .longitude(Nowlon).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);
                        // 设置自定义图标
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.drawable.navi_map_gps_locked);


                        MyLocationConfiguration cof = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
                        mBaiduMap.setMyLocationConfigeration(cof);

                    }
                });
    }


    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            mBaiduMap.setMyLocationConfigeration(configuration);
            //获取经纬度
            // if (isFirstLocation) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            //mBaiduMap.setMapStatus(status);//直接到中间
            mBaiduMap.animateMapStatus(status);//动画的方式到中间
            isFirstLocation = false;
            Log.d("loobotdebug", "地址" + location.getAddress() + " getAddrStr=" + location.getAddrStr());
            Toast.makeText(TravelSocietyApplication.sContext, " 纬度" + location.getLatitude() + " 经度 " + location.getLongitude(), Toast.LENGTH_SHORT)
                    .show();
            Nowlat = location.getLatitude();
            Nowlon = location.getLongitude();
            NowAddr = location.getAddrStr();
            //}

        }
    }

}
