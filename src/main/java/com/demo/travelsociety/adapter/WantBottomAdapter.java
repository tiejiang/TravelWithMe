package com.demo.travelsociety.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.travelsociety.R;
import com.demo.travelsociety.SightSpotListActivity;
import com.demo.travelsociety.db.bean.SightSpotInfo;

import java.util.List;

/**
 *  想去页面底部适配器
 */

public class WantBottomAdapter extends PagerAdapter{
    private Context mContext;
    private List<SightSpotInfo> datas;
    private LayoutInflater inflater;
    private View mCurrentView;

    public WantBottomAdapter(Context mContext, List<SightSpotInfo> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (float)0.9;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View bottom_view = inflater.inflate(R.layout.layout_bottom_grid, null);
        bottom_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SightSpotListActivity.class);
                mContext.startActivity(intent);
            }
        });
        bottom_view.findViewById(R.id.tv_bottom_want);
        bottom_view.findViewById(R.id.tv_bottom_team);
        bottom_view.findViewById(R.id.tv_bottom_info);


        container.addView(bottom_view);
        return bottom_view;
    }


}
