package com.demo.travelsociety.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.demo.travelsociety.R;
import com.demo.travelsociety.db.bean.AlbumItem;
import java.util.List;

/**
 *
 */
public class AlbumMyAdapter extends BaseQuickAdapter<AlbumItem, BaseViewHolder>{

    public AlbumMyAdapter(List<AlbumItem> data) {
        super(R.layout.item_album, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumItem item) {
        helper.setText(R.id.tv_album_name, item.getName())
                .setText(R.id.tv_album_number, String.valueOf(item.getCount()));
        Glide.with(mContext).load(item.getIcon()).crossFade().placeholder(R.mipmap.demo2);
    }
}
