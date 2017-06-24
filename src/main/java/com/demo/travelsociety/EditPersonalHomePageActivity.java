package com.demo.travelsociety;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.demo.travelsociety.utils.PopupWindowUtil;

/**
 * 编辑个人主页页面
 */
public class EditPersonalHomePageActivity extends AppCompatActivity implements OnClickListener{
    private TextView tv_title_cancel, tv_title_ok;
    private RelativeLayout rl_edit_nick, ly_edit_sex, ly_edit_birthday, ly_edit_region, ly_edit_sign, ly_edit_job;
    private TextView tv_edit_nickname,tv_edit_sex,tv_edit_birthday,tv_edit_region,tv_edit_sign,tv_edit_job;
    private ImageView iv_edit_bg;
    private TextView tv_edit_bg;
    private RelativeLayout rl_edit_more_photo;
    private int mYear = 1990;
    private int mMonth = 0;
    private int mDay = 1;
    private ImageView iv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_home_page);

        initView();
    }

    private void initView(){
        tv_title_cancel = (TextView)findViewById(R.id.tv_title_cancel); //取消
        tv_title_cancel.setOnClickListener(this);
        tv_title_ok = (TextView)findViewById(R.id.tv_title_ok); // 完成
        tv_title_ok.setOnClickListener(this);

        iv_edit_bg = (ImageView)findViewById(R.id.iv_edit_bg);// 人物封面背景
        tv_edit_bg = (TextView)findViewById(R.id.tv_edit_bg); // 点击添加封面
        tv_edit_bg.setOnClickListener(this);
        rl_edit_more_photo = (RelativeLayout)findViewById(R.id.rl_edit_more_photo);// 显示添加更多图片按钮
        iv_add = (ImageView)findViewById(R.id.iv_add); // 加图片
        iv_add.setOnClickListener(this);

        rl_edit_nick = (RelativeLayout)findViewById(R.id.rl_edit_nick);//昵称
        rl_edit_nick.setOnClickListener(this);
        tv_edit_nickname = (TextView)findViewById(R.id.tv_edit_nickname);

        ly_edit_sex = (RelativeLayout)findViewById(R.id.ly_edit_sex);//性别
        ly_edit_sex.setOnClickListener(this);
        tv_edit_sex = (TextView)findViewById(R.id.tv_edit_sex);

        ly_edit_birthday = (RelativeLayout)findViewById(R.id.ly_edit_birthday);//生日
        ly_edit_birthday.setOnClickListener(this);
        tv_edit_birthday = (TextView)findViewById(R.id.tv_edit_birthday);

        ly_edit_region = (RelativeLayout)findViewById(R.id.ly_edit_region);//地区
        ly_edit_region.setOnClickListener(this);
        tv_edit_region = (TextView)findViewById(R.id.tv_edit_region);

        ly_edit_sign = (RelativeLayout)findViewById(R.id.ly_edit_sign);//签名
        ly_edit_sign.setOnClickListener(this);
        tv_edit_sign = (TextView)findViewById(R.id.tv_edit_sign);

        ly_edit_job = (RelativeLayout)findViewById(R.id.ly_edit_job);//职业
        ly_edit_job.setOnClickListener(this);
        tv_edit_job = (TextView)findViewById(R.id.tv_edit_job);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_title_cancel){
            finish();

        }else if (id == R.id.tv_edit_bg){//点击添加封面图片
            Intent intent = new Intent(this, AddPhotoActivity.class);
            startActivityForResult(intent, 2);

        }else if (id == R.id.iv_add){
            Intent intent = new Intent(this, AddPhotoActivity.class);
            startActivityForResult(intent, 3);

        } else if (id == R.id.tv_title_ok){
            //完成
            finish();

        }else if (id == R.id.rl_edit_nick){ //昵称
            Bundle bundle = new Bundle();
            bundle.putString("title", "昵称");
            bundle.putString("edit", tv_edit_nickname.getText().toString());
            Intent intent = new Intent(this, EditTextActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);

        }else if (id == R.id.ly_edit_sex){ //性别
            PopupWindowUtil.showWindow(this, view, new PopupWindowUtil.OnPopupWindowClickListener(){
                @Override
                public void onClickIndex(int index) {
                    if (index == 0){// 男
                        tv_edit_sex.setText("男");
                    }else if (index == 1){ //女
                        tv_edit_sex.setText("女");
                    }
                }
            });

        }else if (id == R.id.ly_edit_birthday){ //生日
            showDatePickerCancelDialog();

        }else if (id == R.id.ly_edit_region){ //地区
            Intent intent = new Intent(this, CitySelecterActivity.class);
            startActivityForResult(intent, 4);
            /*String[] regions = getResources().getStringArray(R.array.area);
            selectDataDialog(regions, tv_edit_region);*/

        }else if (id == R.id.ly_edit_sign){ //签名
            Bundle bundle = new Bundle();
            bundle.putString("title", "签名");
            bundle.putString("edit", tv_edit_sign.getText().toString());
            Intent intent = new Intent(this, EditTextActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);

        }else if (id == R.id.ly_edit_job){ //职业
            String[] jobs = getResources().getStringArray(R.array.job);
            selectDataDialog(jobs, tv_edit_job);
        }
    }

    /**
     * 选择数据
     */
    private void selectDataDialog(final String[] datas, final TextView view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(datas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                view.setText(datas[i]);
            }
        });
        builder.create().show();
    }

    /**
     * 选择日期
     */
    private void showDatePickerCancelDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, mDateSetListener, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
    /**
     * 日期选择监听
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateBirthdayDisplay();
        }
    };

    /**
     * 更新日期
     */
    private void updateBirthdayDisplay() {
        StringBuilder birthday = new StringBuilder();
        birthday.append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).toString();
        tv_edit_birthday.setText(birthday);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK){
            tv_edit_nickname.setText(data.getStringExtra("name"));

        } else if (requestCode == 1 && resultCode == RESULT_OK){
            tv_edit_sign.setText(data.getStringExtra("name"));

        } else if (requestCode == 2 && resultCode == RESULT_OK){
            tv_edit_bg.setVisibility(View.GONE);
            rl_edit_more_photo.setVisibility(View.VISIBLE);

            String imagePath = data.getStringExtra("imagepath");
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            iv_edit_bg.setImageBitmap(bm);

        } else if (requestCode == 3 && resultCode == RESULT_OK){


        } else if (resultCode == RESULT_OK && requestCode == 4){
            if (data != null){
                String cityName = data.getStringExtra("cityName");
                tv_edit_region.setText(cityName);

            }
        }
    }
}
