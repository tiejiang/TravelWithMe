package com.demo.travelsociety.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.demo.travelsociety.R;

/**
 *  弹出框工具类
 */
public class PopupWindowUtil {
    private static PopupWindow popupWindow;

    public interface OnPopupWindowClickListener {

        void onClickIndex(int index);
    }

    public static void showWindow(Context context, View view, final OnPopupWindowClickListener onPopupWindowClickListener){
        if (popupWindow == null){
            View popupView = View.inflate(context, R.layout.layout_popupwindow,
                    null);
            popupWindow = new PopupWindow(popupView,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.BottomPopupWindow);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 点击PopWindow外界面关闭弹窗
            //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });
            View rl_pop_bg = popupView.findViewById(R.id.rl_pop_bg);
            rl_pop_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hide();
                }
            });
            TextView tv_pop_one = (TextView)popupView.findViewById(R.id.tv_pop_one);
            tv_pop_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPopupWindowClickListener != null){
                        onPopupWindowClickListener.onClickIndex(0);
                        hide();
                    }
                }
            });

            TextView tv_pop_two = (TextView)popupView.findViewById(R.id.tv_pop_two);
            tv_pop_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPopupWindowClickListener != null){
                        onPopupWindowClickListener.onClickIndex(1);
                        hide();
                    }
                }
            });

            TextView tv_pop_cancel = (TextView)popupView.findViewById(R.id.tv_pop_cancel);
            tv_pop_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hide();
                }
            });
        }

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public static void hide() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
