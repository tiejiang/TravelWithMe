package com.demo.travelsociety.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.demo.travelsociety.R;

/**
 * 相册适配器
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;

    public AlbumAdapter(Context context) {
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
        View view = mInflater.inflate(R.layout.item_album, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mOnItemClickLitener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_album;
        private TextView tv_album_name, tv_album_number;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_album = (ImageView)itemView.findViewById(R.id.iv_album);
            tv_album_name = (TextView)itemView.findViewById(R.id.tv_album_name);
            tv_album_number = (TextView)itemView.findViewById(R.id.tv_album_number);
        }
    }

}
