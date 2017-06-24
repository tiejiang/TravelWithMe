package com.demo.travelsociety.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.travelsociety.R;
import com.demo.travelsociety.db.bean.TravellPerson;

import java.util.ArrayList;
import java.util.List;

/**
 *  景点旅友列表适配器
 */

public class SightPersonAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<TravellPerson> datas;

    public SightPersonAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        datas = new ArrayList<>();
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setDate(List<TravellPerson> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * 增加数据
     * @param dataList
     */
    public void addData(Object dataList) {
        List<TravellPerson> list=(ArrayList<TravellPerson>) dataList;
        for(int i=0;i<list.size();i++){
            this.datas.add(list.get(i));
        }
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sight_person, null);

            viewHolder.iv_person_icon = (ImageView)convertView.findViewById(R.id.iv_person_icon);
            viewHolder.iv_person_auth = (ImageView)convertView.findViewById(R.id.iv_person_auth);
            viewHolder.tv_person_name = (TextView)convertView.findViewById(R.id.tv_person_name);
            viewHolder.tv_person_time_distance = (TextView)convertView.findViewById(R.id.tv_person_time_distance);
            viewHolder.tv_person_grade = (TextView)convertView.findViewById(R.id.tv_person_grade);
            viewHolder.tv_person_sex = (TextView)convertView.findViewById(R.id.tv_person_sex);
            viewHolder.tv_person_age = (TextView)convertView.findViewById(R.id.tv_person_age);
            viewHolder.tv_person_profession = (TextView)convertView.findViewById(R.id.tv_person_profession);
            viewHolder.tv_person_desc = (TextView)convertView.findViewById(R.id.tv_person_desc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        return convertView;
    }
    static class ViewHolder{
        ImageView iv_person_icon;
        ImageView iv_person_auth;
        TextView tv_person_name;
        TextView tv_person_time_distance;
        TextView tv_person_grade;
        TextView tv_person_sex;
        TextView tv_person_age;
        TextView tv_person_profession;
        TextView tv_person_desc;
    }
}
