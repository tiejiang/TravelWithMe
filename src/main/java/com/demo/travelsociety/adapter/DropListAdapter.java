package com.demo.travelsociety.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.demo.travelsociety.R;
import com.demo.travelsociety.db.bean.OrgBean;

import java.util.List;

/**
 *  赛选下拉List适配器
 */
public class DropListAdapter extends BaseAdapter{
    private Context mContext;
    private List<OrgBean> datas;
    private LayoutInflater inflater;
    private int checked = -1;

    public DropListAdapter(Context mContext, List<OrgBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setDate(List<OrgBean> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setChecked(int checked){//设定一个选中的标志位，在activity中传入值。
        this.checked = checked;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_drop_list, null);
            holder.tv_name = (TextView)view.findViewById(R.id.tv_name);
            holder.iv_selected = (ImageView)view.findViewById(R.id.iv_selected);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.tv_name.setText(datas.get(i).getName());
        if(checked == i){
            view.setBackgroundResource(R.color.item_drop_list_bg);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.iv_selected.setVisibility(View.VISIBLE);
        }else{
            view.setBackgroundResource(R.color.white);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.radioText));
            holder.iv_selected.setVisibility(View.GONE);
        }
        return view;
    }
    class ViewHolder{
        public TextView tv_name;
        public ImageView iv_selected;
    }
}
