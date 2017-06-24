package com.demo.travelsociety;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 编辑文本界面
 */
public class EditTextActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back, iv_edit_delete;
    private TextView tv_title_name;
    private EditText edit_text;
    private String title, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        initDatabase();
        initView();
    }

    private void initDatabase(){
        title = getIntent().getStringExtra("title");
        edit = getIntent().getStringExtra("edit");
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_title_name = (TextView)findViewById(R.id.tv_title_name); // 标题名
        tv_title_name.setText(title);

        edit_text = (EditText)findViewById(R.id.edit_text);  //编辑框
        edit_text.setText(edit);
        edit_text.setSelection(TextUtils.isEmpty(edit) ? 0 : edit.length());

        iv_edit_delete = (ImageView)findViewById(R.id.iv_edit_delete);
        iv_edit_delete.setOnClickListener(this);

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)){
                    iv_edit_delete.setVisibility(View.GONE);
                }else {
                    iv_edit_delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back){
            Intent intent = new Intent();
            intent.putExtra("name", edit_text.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }else if (id == R.id.iv_edit_delete){
            edit_text.setText("");
        }
    }
}
