package com.demo.travelsociety;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * 选择系统相机和系统图库弹出框
 */
public class AddPhotoActivity extends Activity implements View.OnClickListener{
    private TextView tv_camera, tv_gallery;
    public static final int CHOOSE_PICTURE = 1; // 选择相册
    public static final int TAKE_CANERA = 2;  // 选择相机
    public static final int CROP_IMAGE = 3;  // 裁剪
    private static final String IMAGE_FILE_LOCATION = Environment.getExternalStorageDirectory()+"/temp.jpg";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_photo);
        imageUri = Uri.fromFile(new File(IMAGE_FILE_LOCATION));
        initView();
    }

    private void initView(){
        tv_camera = (TextView)findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(this);
        tv_gallery = (TextView)findViewById(R.id.tv_gallery);
        tv_gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_camera){// 拍照
            takeCamera();
        }else if (id == R.id.tv_gallery){//图库
            selectPhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CHOOSE_PICTURE:
                Uri uri = data.getData();
                if (uri == null){
                    Toast.makeText(this, "uri为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                cropImage(uri, 300, 300, CROP_IMAGE);
                break;

            case TAKE_CANERA:
                cropImage(imageUri, 300, 300, CROP_IMAGE);
                break;

            case CROP_IMAGE:
                Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/temp.jpg");
                Intent intent = new Intent();
                intent.putExtra("imagepath", Environment.getExternalStorageDirectory()+"/temp.jpg");
                setResult(RESULT_OK, intent);
                finish();
                /*Bitmap photo = null;
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    photo = BitmapFactory.decodeFile(photoUri.getPath());
                }
                if (photo == null) {
                    Bundle extra = data.getExtras();
                    if (extra != null) {
                        photo = extra.getParcelable("data");
                        if (photo == null){
//                            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/temp.jpg");

                        }else {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                }*/

                break;
        }
    }

    private void takeCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        startActivityForResult(intent, TAKE_CANERA);
    }

    private void cropImage(Uri uri, int outputX, int outputY, int requestCode){
            //裁剪图片意图
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            //裁剪框的比例，1：1
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //裁剪后输出图片的尺寸大小
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            //图片格式
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", "JPEG");
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", false);
            startActivityForResult(intent, requestCode);

    }

    private void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    /**
     * 新增相册页面
     */
    public static class AddAlbumActivity extends AppCompatActivity implements View.OnClickListener{
        private ImageView back;
        private TextView tv_add_album;
        private EditText ed_in, ed_desc_in;
        private ImageView iv_delete, iv_desc_delete;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_album);

            initView();
        }

        private void initView(){
            back = (ImageView)findViewById(R.id.back);
            back.setOnClickListener(this);
            tv_add_album = (TextView)findViewById(R.id.tv_add_album);
            tv_add_album.setOnClickListener(this);

            ed_in = (EditText)findViewById(R.id.ed_in);
            ed_in.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)){
                        iv_delete.setVisibility(View.GONE);
                    }else {
                        iv_delete.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            iv_delete = (ImageView)findViewById(R.id.iv_delete);
            iv_delete.setOnClickListener(this);

            ed_desc_in = (EditText)findViewById(R.id.ed_desc_in);
            ed_desc_in.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)){
                        iv_desc_delete.setVisibility(View.GONE);
                    }else {
                        iv_desc_delete.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            iv_desc_delete = (ImageView)findViewById(R.id.iv_desc_delete);
            iv_desc_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.back){
                finish();
            }else if (id == R.id.tv_add_album){
                //提交相册
                String name = ed_in.getText().toString();
                String desc = ed_desc_in.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(this, "请输入名称！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(desc)){
                    Toast.makeText(this, "请输入描述！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else if (id == R.id.iv_delete){
                ed_in.setText("");
            }else if (id == R.id.iv_desc_delete){
                ed_desc_in.setText("");
            }
        }
    }
}
