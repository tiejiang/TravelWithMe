package com.demo.travelsociety.adapter;


import android.widget.SimpleAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.travelsociety.R;
import com.demo.travelsociety.db.bean.DynamicsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态适配器
 */
public class DynamicsAdapter extends BaseQuickAdapter<DynamicsItem, BaseViewHolder>{

    public DynamicsAdapter(List<DynamicsItem> data) {
        super( R.layout.item_dynamics, data);
        getData();
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicsItem item) {
        helper.setText(R.id.tv_item_dynamics_title, item.getName())
            .setAdapter(R.id.gv_item_dynamics, new SimpleAdapter(mContext,
                    getPictureData(),
                    R.layout.item_photo,
                    new String[]{"img"},
                    new int[]{R.id.iv_item_grid}));
    }
    private List<Map<String, Object>> getPictureData() {
        //map.put(参数名字,参数值)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.mipmap.demo2);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.mipmap.demo2);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.mipmap.demo2);
        list.add(map);
        return list;
    }
}
