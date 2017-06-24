package com.demo.travelsociety.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.travelsociety.R;
import com.demo.travelsociety.SightSpotListActivity;
import com.demo.travelsociety.db.bean.SightSpotInfo;

import java.util.List;

public class HorizontalScrollViewAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SightSpotInfo> mDatas;
    private int mScreenWitdh;

    public HorizontalScrollViewAdapter(Context context, List<SightSpotInfo> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWitdh = displayMetrics.widthPixels;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position)	{
        return mDatas.get(position);
    }

    public long getItemId(int position)	{
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_bottom_grid, parent, false);
            viewHolder.ly_item_bottom = (LinearLayout)convertView.findViewById(R.id.ly_item_bottom);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mScreenWitdh-60,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        viewHolder.ly_item_bottom.setLayoutParams(lp);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SightSpotListActivity.class);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView mImg;
        TextView mText;
        LinearLayout ly_item_bottom;
    }
}
