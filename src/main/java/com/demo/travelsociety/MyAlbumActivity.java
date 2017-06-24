package com.demo.travelsociety;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.travelsociety.adapter.AlbumMyAdapter;
import com.demo.travelsociety.db.bean.AlbumItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的相册页面
 */
public class MyAlbumActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener{
    private ImageView back;
    private TextView tv_title_name;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AlbumMyAdapter albumMyAdapter;
    private List<AlbumItem> list = new ArrayList<>();
    private int mCurrentCounter = 0;
    private static final int PAGE_SIZE = 5;
    private int delayMillis = 1000;
    private boolean isErr;
    private GridView gv_album;
    private MyAlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_album);
        initDemoDatas();
        initView();
//        initAdapter();
    }

    private void initDemoDatas(){
        AlbumItem albumItem;
        for (int i=0; i<5; i++){
            albumItem = new AlbumItem();
            albumItem.setName("北京");
            albumItem.setCount(23);
            list.add(albumItem);
        }
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_title_name = (TextView)findViewById(R.id.tv_title_name);
        tv_title_name.setText("我的相册");
        gv_album = (GridView)findViewById(R.id.gv_album);
        albumAdapter = new MyAlbumAdapter(this);
        gv_album.setAdapter(albumAdapter);
        gv_album.setOnItemClickListener(this);
        /*mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));*/
    }

    class MyAlbumAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater inflater;

        public MyAlbumAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list == null ? 1 : list.size()+1;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

                if (view == null){
                    viewHolder = new ViewHolder();
                    view = inflater.inflate(R.layout.item_album, null);

                    viewHolder.iv_album = (ImageView)view.findViewById(R.id.iv_album);
                    viewHolder.tv_album_name = (TextView)view.findViewById(R.id.tv_album_name);
                    viewHolder.tv_album_number = (TextView)view.findViewById(R.id.tv_album_number);

                    view.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder)view.getTag();
                }
                if (i == 0){
                    viewHolder.iv_album.setBackgroundResource(R.mipmap.album_add);
                    viewHolder.tv_album_name.setText("新建相册");
                }else {
                    viewHolder.iv_album.setBackgroundResource(R.mipmap.demo1);
                    viewHolder.tv_album_name.setText(list.get(i-1).getName());
                    viewHolder.tv_album_number.setText(list.get(i-1).getCount()+"张");
                }

                return view;

        }

        class ViewHolder{
            TextView tv_album_name;
            ImageView iv_album;
            TextView tv_album_number;
        }
    }
    /*private void initAdapter(){
        albumMyAdapter = new AlbumMyAdapter(list);
        albumMyAdapter.openLoadAnimation();
        albumMyAdapter.openLoadMore(PAGE_SIZE);
        mRecyclerView.setAdapter(albumMyAdapter);
        mCurrentCounter = albumMyAdapter.getData().size();
        albumMyAdapter.setOnLoadMoreListener(this);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MyAlbumActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0){
            Intent intent = new Intent(this, AddPhotoActivity.AddAlbumActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK){
            AlbumItem albumItem = new AlbumItem();
            albumItem.setName("新相册");
            albumItem.setCount(6);
            list.add(albumItem);
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back){
            finish();
        }
    }
}
