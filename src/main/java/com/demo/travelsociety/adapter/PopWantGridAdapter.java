package com.demo.travelsociety.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.travelsociety.R;

import java.util.HashMap;
import java.util.List;

/**
 * "想去"界面右上角"我想去"适配器
 */
public class PopWantGridAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> myList;
    private Context mContext;
    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public PopWantGridAdapter(Context mContext, List<HashMap<String, Object>> myList) {
        this.mContext = mContext;
        this.myList = myList;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_want, null);

            viewHolder.delete = (ImageView)convertView.findViewById(R.id.delete);
            viewHolder.text = (TextView)convertView.findViewById(R.id.text);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.text.setText(myList.get(position).get("name").toString());
        viewHolder.delete.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
        return convertView;
    }
    static class ViewHolder{
        ImageView delete;
        TextView text;

    }

}
