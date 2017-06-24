package com.demo.travelsociety.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.demo.travelsociety.R;
import com.demo.travelsociety.recyclerview.DividerItemDecoration;
import com.demo.travelsociety.recyclerview.WrappingLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  动态
 */
public class DynamicsFragment extends Fragment {
    private RecyclerView list;

    public DynamicsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private List<Map<String, Object>> getData() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamics, container, false);
        list = (RecyclerView)view.findViewById(R.id.list);
        //设置布局管理器
        WrappingLinearLayoutManager manager = new WrappingLinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        list.setNestedScrollingEnabled(false);
        list.setHasFixedSize(false);
        list.setLayoutManager(manager);
        //设置adapter
        DynamicsAdapter mAdapter = new DynamicsAdapter(getActivity());
        list.setAdapter(mAdapter);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        return view;
    }

    public class DynamicsAdapter extends RecyclerView.Adapter<DynamicsAdapter.ViewHolder>{
        private LayoutInflater mInflater;

        public DynamicsAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        /**
         * item显示
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_dynamics, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 数据的绑定显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                    getData(),
                    R.layout.item_photo,
                    new String[]{"img"},
                    new int[]{R.id.iv_item_grid});
            holder.gv_item_dynamics.setAdapter(adapter);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private GridView gv_item_dynamics;

            public ViewHolder(View itemView) {
                super(itemView);
                gv_item_dynamics = (GridView)itemView.findViewById(R.id.gv_item_dynamics);
            }
        }
    }
}
