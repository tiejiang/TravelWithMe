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

public class TogetherAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<TravellPerson> datas;



    public TogetherAdapter(Context mContext) {
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
            convertView = inflater.inflate(R.layout.item_together, null);

            viewHolder.iv_person_icon = (ImageView)convertView.findViewById(R.id.iv_person_icon);
            viewHolder.tv_person_name = (TextView)convertView.findViewById(R.id.tv_person_name);

            viewHolder.tv_qi = (TextView)convertView.findViewById(R.id.tv_qi);
            viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            viewHolder.tv_wantToCount = (TextView)convertView.findViewById(R.id.tv_wantToCount);
            viewHolder.tv_da1 = (TextView)convertView.findViewById(R.id.tv_da1);
            viewHolder.tv_line1 = (TextView)convertView.findViewById(R.id.tv_line1);
            viewHolder.tv_da1_cont = (TextView)convertView.findViewById(R.id.tv_da1_cont);
            viewHolder.tv_line1_cont = (TextView)convertView.findViewById(R.id.tv_line1_cont);
            viewHolder.tv_msg = (TextView)convertView.findViewById(R.id.tv_msg);
            viewHolder.tv_bottom_want = (TextView)convertView.findViewById(R.id.tv_bottom_want);
            viewHolder.tv_bottom_team = (TextView)convertView.findViewById(R.id.tv_bottom_team);
            viewHolder.tv_bottom_conductor = (TextView)convertView.findViewById(R.id.tv_bottom_conductor);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        return convertView;
    }
    static class ViewHolder{
        ImageView iv_person_icon;
        TextView tv_person_name;
        TextView tv_qi;
        TextView tv_time;
        TextView tv_wantToCount;
        TextView tv_da1;
        TextView tv_line1;
        TextView tv_da1_cont;

        TextView tv_line1_cont;
        TextView tv_msg;
        TextView tv_bottom_want;
        TextView tv_bottom_team;
        TextView tv_bottom_conductor;

    }
}
