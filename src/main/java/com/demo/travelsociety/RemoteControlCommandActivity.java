package com.demo.travelsociety;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.travelsociety.chat.ChatMsgEntity;
import com.demo.travelsociety.chat.ChatMsgViewAdapter;
import com.demo.travelsociety.helper.IMChattingHelper;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2016/12/14.
 */

public class RemoteControlCommandActivity extends Activity implements IMChattingHelper.OnMessageReportCallback
    ,View.OnClickListener{
    private Button mSendButton;
    private EditText mReceiveEditText, mSendEditText;
    private Button mBtnSend;
    private Button mBtnBack;
    private EditText mEditTextContent;
    //聊天内容的适配器
    private ChatMsgViewAdapter mAdapter;
    private ListView mListView;
    //聊天的内容
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private String receiveString = "暂无消息";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);

        mSendButton = (Button)findViewById(R.id.btn_command_send);
        mReceiveEditText = (EditText)findViewById(R.id.received_command);
        mSendEditText = (EditText)findViewById(R.id.send_info);
        initView();
        initData();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mSendEditText.getText())){
                    String text = mSendEditText.getText().toString();
                    handleSendTextMessage(text);
                }else {
                    Toast.makeText(RemoteControlCommandActivity.this, "发送文本为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IMChattingHelper.setOnMessageReportCallback(this);
        Log.d("TIEJIANG", "TIEJIANG-skyee---[RemoteControlCommandActivity]-onResume");// add by tiejiang
    }


    @Override
    public void onMessageReport(ECError error, ECMessage message) {

    }

    @Override
    public void onPushMessage(String sessionId, List<ECMessage> msgs) {
        int msgsSize = msgs.size();
        String  message = " ";
        for (int i = 0; i < msgsSize; i++){
            message = ((ECTextMessageBody) msgs.get(i).getBody()).getMessage();
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]" + "i = " + i + ", message = " + message);// add by tiejiang
        }
        Log.d("TIEJIANG", "[RemoteControlCommandActivity]" + ",sessionId = " + sessionId);// add by tiejiang
        mReceiveEditText.setText(message);
//        receiveString = message;
        displayData(message);
    }

    /**
     * 处理文本发送方法事件通知
     * @param text
     */
    public static void handleSendTextMessage(CharSequence text) {
        if(text == null) {
            return ;
        }
        if(text.toString().trim().length() <= 0) {
//            canotSendEmptyMessage();
            return ;
        }
        // 组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
        // 设置消息接收者
//        msg.setTo(mRecipients);
        msg.setTo(TalkActivity.userReceive); // attenion  this number is not the login number! / modified by tiejiang
        ECTextMessageBody msgBody=null;
        Boolean isBQMMMessage=false;
        String emojiNames = null;
//        if(text.toString().contains(CCPChattingFooter2.TXT_MSGTYPE)  && text.toString().contains(CCPChattingFooter2.MSG_DATA)){
//            try {
//                JSONObject jsonObject = new JSONObject(text.toString());
//                String emojiType=jsonObject.getString(CCPChattingFooter2.TXT_MSGTYPE);
//                if(emojiType.equals(CCPChattingFooter2.EMOJITYPE) || emojiType.equals(CCPChattingFooter2.FACETYPE)){//说明是含有BQMM的表情
//                    isBQMMMessage=true;
//                    emojiNames=jsonObject.getString(CCPChattingFooter2.EMOJI_TEXT);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        if (isBQMMMessage) {
            msgBody = new ECTextMessageBody(emojiNames);
            msg.setBody(msgBody);
            msg.setUserData(text.toString());
        } else {
            // 创建一个文本消息体，并添加到消息对象中
            msgBody = new ECTextMessageBody(text.toString());
            msg.setBody(msgBody);
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-handleSendTextMessage" + ", txt = " + text);// add by tiejiang
        }

//        String[] at = mChattingFooter.getAtSomeBody();
//        msgBody.setAtMembers(at);
//        mChattingFooter.clearSomeBody();
        try {
            // 发送消息，该函数见上
            long rowId = -1;
//            if(mCustomerService) {
//                rowId = CustomerServiceHelper.sendMCMessage(msg);
//            } else {
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-SendECMessage");// add by tiejiang
                rowId = IMChattingHelper.sendECMessage(msg);

//            }
            // 通知列表刷新
//            msg.setId(rowId);
//            notifyIMessageListView(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TIEJIANG", "[RemoteControlCommandActivity]-send failed");// add by tiejiang
        }
    }

    //初始化视图
    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }

    private String[] msgArray = new String[]{" 旅游计划！",
            "有去北京故宫德玛...",
            "有去长城的吗",
            "有去颐和园的吗？",
            "有多少人呢?",
            "还在联系",
            "可以组个群吗！",
            "人数多了就组一个去北京的"};

    private String[]dataArray = new String[]{"2012-09-01 18:00", "2012-09-01 18:10",
            "2012-09-01 18:11", "2012-09-01 18:20",
            "2012-09-01 18:30", "2012-09-01 18:35",
            "2012-09-01 18:40", "2012-09-01 18:50"};
    private final static int COUNT = 8;

    //显示实时信息
    private void displayData(String msg) {
//        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
//            entity.setDate(msg);
//            if (i % 2 == 0)
//            {
                entity.setName("姚");
                entity.setMsgType(true);
//            }else{
//                entity.setName("Shamoo");
//                entity.setMsgType(false);
//            }

            entity.setText(msg);
            mDataArrays.add(entity);
//        }
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }
    //初始化要显示的数据
    private void initData() {
        for(int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0)
            {
                entity.setName("姚");
                entity.setMsgType(true);
            }else{
                entity.setName("Shamoo");
                entity.setMsgType(false);
            }

            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_send:
                send();
                break;
        }
    }
    private void send()
    {
        String contString = mEditTextContent.getText().toString();
        handleSendTextMessage(contString);
//        if (contString.length() > 0)
//        {
//            ChatMsgEntity entity = new ChatMsgEntity();
//            entity.setDate(getDate());
//            entity.setName("");
//            entity.setMsgType(false);
//            entity.setText(contString);
//            mDataArrays.add(entity);
//            mAdapter.notifyDataSetChanged();
//            mEditTextContent.setText("");
//            mListView.setSelection(mListView.getCount() - 1);
//        }
    }

    //获取日期
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
        return sbBuffer.toString();
    }

    private void back() {
        finish();
    }
}
