package com.demo.travelsociety;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.demo.travelsociety.adapter.DynamicsAdapter;
import com.demo.travelsociety.db.bean.DynamicsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的动态页面
 */
public class MyDynamicsActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        OnClickListener{
    private ImageView back;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DynamicsAdapter adapter;
    private int mCurrentCounter = 0;
    private static final int PAGE_SIZE = 5;
    private int delayMillis = 1000;
    private List<DynamicsItem> list = new ArrayList<>();
    private boolean isErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamics);

        initDemoDatas();
        initView();
        initAdapter();
    }

    private void initDemoDatas(){
        DynamicsItem dynamicsItem;
        for (int i=0; i<5; i++){
            dynamicsItem = new DynamicsItem();
            dynamicsItem.setName("风生水起");
            dynamicsItem.setDate("2016-01-22 12:21:40");
            dynamicsItem.setAddress("广州白云区");
            dynamicsItem.setDesc("卡机的垃圾的法律奥克兰的算法的法兰蝶阀爱看的看啊啊大幅度发打了卡地方");
            dynamicsItem.setCommentCount(i);
            dynamicsItem.setPraiseCount(i);
            list.add(dynamicsItem);
        }
    }

    /**
     * 初始化控件
     */
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * 初始化适配器
     */
    private void initAdapter(){
        adapter = new DynamicsAdapter(list);
        adapter.openLoadAnimation();
        adapter.openLoadMore(PAGE_SIZE);
        mRecyclerView.setAdapter(adapter);
        mCurrentCounter = adapter.getData().size();
        adapter.setOnLoadMoreListener(this);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MyDynamicsActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= 10) {
                    adapter.loadComplete();
                    /*if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
                    }
                    adapter.addFooterView(notLoadingView);*/
                } else {
                    if (isErr) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addData(list);
                                mCurrentCounter = adapter.getData().size();
                            }
                        }, delayMillis);
                    } else {
                        isErr = true;
                        Toast.makeText(MyDynamicsActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        adapter.showLoadMoreFailedView();

                    }
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(list);
                adapter.openLoadMore(PAGE_SIZE);
                adapter.removeAllFooterView();
                mCurrentCounter = PAGE_SIZE;
                mSwipeRefreshLayout.setRefreshing(false);
                isErr = false;
            }
        }, delayMillis);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back){
            finish();
        }
    }
}
