package com.demo.travelsociety.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.demo.travelsociety.adapter.HorizontalScrollViewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 *  重写HorizontalScrollView实现手势滑动一次一页
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    /**
     * 滑动回调接口
     */
    public interface CurrentViewChangeListener {
        void onCurrentViewChanged(int position, View viewIndicator);
        }

    /**
     * 点击回调接口
     */
    public interface OnItemClickListener {
        void onClick(View view, int pos);
        }

    private CurrentViewChangeListener mListener;

    private OnItemClickListener mOnClickListener;

    private static final String TAG = "MyHorizontalScrollView";
    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;
    /**
     * 子元素的宽度
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 当前最后一张图片的index
     */
    private int mCurrentIndex;
    /**
     * 当前第一张图片的下标
     */
     private int mFristIndex;
    /**
     * 当前第一个View
     */
     private View mFirstView;
     /**
      * 数据适配器
      */
     private HorizontalScrollViewAdapter mAdapter;
    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen=1;
    /**
     * 屏幕的宽度
     */
    private int mScreenWitdh;
    /**
     * 保存View与位置的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

    private int mPosX;
    private int curX;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWitdh = displayMetrics.widthPixels;
        mChildWidth = mScreenWitdh-60;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mContainer = (LinearLayout) getChildAt(0);
    }
    /**
     * 加载下一个
     */
    protected void loadNextView()	{
        // 数组边界值计算
        if (mFristIndex == mAdapter.getCount() - 1){
            return;
        }
        // 移除第一张图片，且将水平滚动位置置0
        scrollBy(mChildWidth, 0);

        // 当前第一张图片小标
        mFristIndex++;
        // 如果设置了滚动监听则触发
        if (mListener != null){
            notifyCurrentViewChanged();
            }
        /*if (mOnClickListener != null) {
            mOnClickListener.onClick(mContainer.getChildAt(mFristIndex), mFristIndex);
            }*/
        }

    /**
     * 加载上一个
     */
    protected void loadPreView(){
        //如果当前已经是第一个，返回
        if(mFristIndex == 0){
            return;
        }

        //水平滚动位置向左移动view的宽度个像素
        scrollBy(-mChildWidth, 0);
        //当前位置,当前第一个显示的下标
        mFristIndex--;
        if (mListener != null){
            notifyCurrentViewChanged();
        }

    }

    /**
     * 滑动时回调
     */
    public void notifyCurrentViewChanged(){
        mListener.onCurrentViewChanged(mFristIndex, mContainer.getChildAt(mFristIndex));
    }

    /**
     * 初始化数据，设置数据适配器
     *
     * @param mAdapter
     */
    public void initDatas(HorizontalScrollViewAdapter mAdapter, LinearLayout mContainer){
        this.mAdapter = mAdapter;
        this.mContainer = mContainer;
        mContainer.removeAllViews();
        mViewPos.clear();
        for (int i=0; i<mAdapter.getCount(); i++){
            View view = mAdapter.getView(i, null, mContainer);
            mContainer.addView(view);
            mViewPos.put(view, i);
        }
        if (mListener != null){
            notifyCurrentViewChanged();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPosX = (int)ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = (int)ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (curX - mPosX < 0
                        && (Math.abs(curX - mPosX) > 25)) {
                    //向左滑動
                    loadNextView();
                } else if (curX - mPosX > 0
                        && (Math.abs(curX - mPosX) > 25)) {
                    //向右滑动
                    loadPreView();
                }
                break;
        }
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        }

    public void setCurrentViewChangeListener(
            CurrentViewChangeListener mListener) {
        this.mListener = mListener;
        }

}
