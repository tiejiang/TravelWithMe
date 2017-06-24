package com.demo.travelsociety.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.travelsociety.R;

import java.util.ArrayList;

/**
 *  搜索历史记录适配器
 */

public class HistoryAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> datas;
    private LayoutInflater inflater;
    private SharedPreferences sp;
    private String sbName; // 存储文件名

    public HistoryAdapter(Context context, ArrayList<String> datas, String sbName){
        this.context = context;
        this.datas = datas;
        this.sbName = sbName;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_search_history, null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.tv_search_history_name);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.iv_search_history_delete);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(datas.get(position));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datas.remove(position);
                StringBuilder sb = new StringBuilder();
                for(int i=0; i<datas.size(); i++){
                    sb.append(datas.get(i)+",");
                }
                sp = context.getSharedPreferences(sbName, 0);
                sp.edit().putString("history", sb.toString()).commit();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        ImageView delete;
    }
}
