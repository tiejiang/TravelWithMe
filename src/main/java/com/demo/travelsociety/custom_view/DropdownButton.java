package com.demo.travelsociety.custom_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.travelsociety.R;

/**
 *  下拉button
 */
public class DropdownButton extends RelativeLayout {
    TextView textView;
    boolean isChecked;

    public DropdownButton(Context context) {
        this(context, null);
    }

    public DropdownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.dropdown_button, this, true);
        textView = (TextView) view.findViewById(R.id.textview);
    }

    public void setText(CharSequence s){
        textView.setText(s);
    }

    public boolean getChecked(){
        return isChecked;
    }

    public void setChecked(boolean checked){
        isChecked = !checked;
        Drawable icon;
        if (checked) {
            icon = getResources().getDrawable(R.mipmap.dropdown_2);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else{
            icon = getResources().getDrawable(R.mipmap.dropdown_1);
            textView.setTextColor(getResources().getColor(R.color.text_black));
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }

}
