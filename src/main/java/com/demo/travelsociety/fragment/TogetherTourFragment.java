package com.demo.travelsociety.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.travelsociety.EditPersonalTogetherActivity;
import com.demo.travelsociety.LoginActivity;
import com.demo.travelsociety.R;
import com.demo.travelsociety.adapter.TogetherAdapter;
import com.hyg.dropdownmenu.DropDownMenu;
import com.hyg.dropdownmenu.DropDownMenuUtils;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TogetherTourFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemClickListener{

    private DropDownMenu mDropDownMenu;
    private ImageView btnPublish;
    private List<String> feeList;
    private List<String> zoneList;
    private List<String> plaTypList;
    private List<String> heads;
    private ListView list;

    private TogetherAdapter togetherAdapter;
    public TogetherTourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_together_tour, container, false);
        btnPublish=(ImageView)view.findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(this);
        list = (ListView)view.findViewById(R.id.list);
        togetherAdapter = new TogetherAdapter(getActivity());
        list.setAdapter(togetherAdapter);
        list.setOnItemClickListener(this);

        demoDatas();
       // initView(view);
        return view;
    }



    private void demoDatas(){


        zoneList = Arrays.asList("天河区", "白云区", "越秀区", "番禺区", "海珠区");
        feeList = Arrays.asList("不限", "免费", "50以下", "50-100","100-200");
        plaTypList = Arrays.asList("公园", "博物馆", "动物园");
        heads = Arrays.asList("免费", "天河区", "公园");


    }
    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "view.getId():"+view.getId(), Toast.LENGTH_SHORT).show();
        int id = view.getId();
        if (true){

            Toast.makeText(getActivity(), "id == R.id.btn_publish", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(getActivity(), EditPersonalTogetherActivity.class);
             startActivity(intent);
        }
//        if (id == R.id.btn_publish){
//            Toast.makeText(getActivity(), "id == R.id.btn_publish", Toast.LENGTH_SHORT).show();
//             Intent intent = new Intent(getActivity(), EditPersonalTogetherActivity.class);
//             startActivity(intent);
//        }else if (id == R.id.iv_title_search){
            // Intent intent = new Intent(getActivity(), SearchActivity.class);
            // startActivity(intent);
//        }else if (id == R.id.iv_title_more){
//            // initPopupMenu();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Intent intent = new Intent(getActivity(), PersonalHomePageActivity.class);
        //startActivity(intent);
    }

    private void initView(View view){
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        DropDownMenuUtils.addIconTextList(getActivity(), heads, mDropDownMenu, new DropDownMenuUtils.OnMenuClickListener() {
            @Override
            public void onMenuClick(int viewPosition, int itemPosition) {
                Toast.makeText(getActivity(), "viewPosition="+viewPosition + "itemPosition="+itemPosition, Toast.LENGTH_SHORT).show();
            }
        }, zoneList, feeList, plaTypList);
    }
}
