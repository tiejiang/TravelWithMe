package com.demo.travelsociety;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/16.
 */

public class xunfeiActivity extends Activity {

    /**
     * @author Administrator语音技术由科大讯飞提供
     */
    private int xunfeiLan = 1;// 1
    public static final String XUNFEI_APPID = "55ed07cc";// 55ed07cc
    private final String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String TAG = "loobotdebug";

    //ImageView webview;
    TextView textview;
    Button start;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.xunfei);

      //  webview = (ImageView) findViewById(R.id.webview);
        start = (Button) findViewById(R.id.start);
        mDictInfoShow = (TextView) findViewById(R.id.mDictInfoShow);

        // Glide.with(MainActivity.this)
        // .load("file:///android_asset" + "/acc.gif").asGif()
        // .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(webview);
        initXunFei();


    }

    public void btn(View view) {

        startListenAndSleepTime();

    }

    public void exit(View view) {
        startActivity(new Intent(xunfeiActivity.this, MainActivity.class));
        // finish();
    }

    public void stop(View view) {

        if (player != null) {
            player.start();
            player.release();
            player = null;

        }
        stopGenerating();
        startListenAndSleepTime();

    }

    private void stopGenerating() {

        mTts.stopSpeaking();

    }

    private void setTVColor(String str, char ch1, char ch2, int color,
                            TextView tv) {
        int a = str.indexOf(ch1); // 从字符ch1的下标开始
        int b = str.indexOf(ch2) + 1; // 到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[
        // a,b )左闭右开
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(color), a, b,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }

    private String ContiNuousSpeak = "NO";
    private String forever = "小雪";
    private boolean listen_directly = false;
    private String voiceText;
    private TextView mDictInfoShow;
    Handler handler = new Handler();
    Runnable runnableSetcolor = new Runnable() {
        @Override
        public void run() {
            mDictInfoShow.setText("");
        }
    };

    public static ArrayList<String> parseUnderstandResult(String json) {
        ArrayList<String> retArray = new ArrayList<String>();
        String opStr;
        String serviceStr;
        String name;
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            // if (joResult.getInt("rc") == 0)
            {
                opStr = joResult.getString("operation");
                if (!TextUtils.isEmpty(opStr)) {
                    if (opStr.equals("ANSWER")) {
                        retArray.add("text");
                        retArray.add(joResult.getString("service"));
                        retArray.add(joResult.getJSONObject("answer")
                                .getString("text"));
                        return retArray;
                    }

                    if (opStr.equals("QUERY")) {
                        serviceStr = joResult.getString("service");

                        if (serviceStr.equals("weather")) {
                            JSONArray items = joResult.getJSONObject("data")
                                    .getJSONArray("result");
                            JSONObject obj = items.getJSONObject(0);
                            String contentStr = obj.getString("city") + ",";
                            contentStr += (obj.getString("weather") + ",");
                            contentStr += (obj.getString("tempRange") + ",");
                            contentStr += (obj.getString("wind"));

                            retArray.add("text");
                            retArray.add(serviceStr);
                            retArray.add(contentStr);

                            return retArray;
                        }

                        if (serviceStr.equals("pm25")) {
                            JSONArray items = joResult.getJSONObject("data")
                                    .getJSONArray("result");
                            JSONObject obj = items.getJSONObject(0);

                            String contentStr = obj.getString("area") + ",";
                            contentStr += (obj.getString("quality") + ", pm2.5, ");
                            contentStr += (obj.getString("pm25"));

                            retArray.add("text");
                            retArray.add(serviceStr);
                            retArray.add(contentStr);

                            return retArray;
                        }
                        if (serviceStr.equals("stock")) {
                            retArray.add("stock");
                            retArray.add(serviceStr);
                            retArray.add(serviceStr);
                            return retArray;
                        }

                        return retArray;
                    }
                    if (opStr.equals("CREATE")) {
                        serviceStr = joResult.getString("service");
                        if (serviceStr.equals("schedule")) {
                            retArray.add("schedule");
                            retArray.add(serviceStr);
                            retArray.add("schedule");
                        }
                        return retArray;
                    }
                    if (opStr.equals("CALL")) {
                        serviceStr = joResult.getString("service");
                        if (serviceStr.equals("telephone")) {
                            retArray.add("call");
                            retArray.add("call");
                            retArray.add("call");
                        }
                        return retArray;
                    }
                    if (opStr.equals("SEND")) {
                        serviceStr = joResult.getString("service");
                        if (serviceStr.equals("message")) {
                            retArray.add("message");
                            retArray.add("message");
                            retArray.add("message");
                        }
                        return retArray;
                    }
                    if (opStr.equals("TRANSLATION")) {
                        serviceStr = joResult.getString("service");

                        if (serviceStr.equals("translation")) {
                            retArray.add("dict");
                            retArray.add(serviceStr);
                            retArray.add(joResult.getJSONObject("semantic")
                                    .getJSONObject("slots")
                                    .getString("content"));
                        }

                        return retArray;
                    }

                    if (opStr.equals("PLAY")) {
                        serviceStr = joResult.getString("service");

                        if (serviceStr.equals("music")) {
                            JSONArray items = joResult.getJSONObject("data")
                                    .getJSONArray("result");
                            JSONObject obj = items.getJSONObject(0);

                            retArray.add("music");
                            retArray.add(serviceStr);
                            retArray.add(obj.getString("downloadUrl"));
                            return retArray;
                        }
                        return retArray;
                    }
                    return retArray;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retArray;
    }

    public static ArrayList<String> parseIatResult(String json) {
        ArrayList<String> retArray = new ArrayList<String>();

        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                retArray.add(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retArray;
    }

    private void voiceResultHandle(RecognizerResult results, boolean isLast) {
        try {

            int resSize = 0;

            String curSentence = new String("");

            ArrayList<String> retArray = parseIatResult(results

                    .getResultString());

            for (int i = 0; i < retArray.size(); i++) {

                curSentence += retArray.get(i).toLowerCase();

            }

            if (!mKeywordsFlag) {
                // keyword flag false
                if (ContiNuousSpeak.equals("YES")) {
                } else {
                    Log.d("loobot", "loobotdebug  curSentence=" + curSentence);

                    if (curSentence.contains(helloStr) || listen_directly) {
                        char[] ch = helloStr.toCharArray();

                        setTVColor(curSentence, ch[0], ch[1], Color.RED,
                                mDictInfoShow);
                        handler.postDelayed(runnableSetcolor, 6000);
                        mKeywordsFlag = true;

                        if (!listen_directly) {
                            Log.d("loobot", "loobotdebug !listen_directly");
                            String[] temp = curSentence.split(helloStr);
                            Log.d("loobot", "loobotdebug  temp.lenth="
                                    + temp.length + "  temp=" + temp);
                            if (temp.length > 0) {

                                curSentence = curSentence.substring(temp[0]
                                        .length()

                                        + helloStr.length());
                                Log.d("loobot", "loobotdebug  >0curSentence="
                                        + curSentence);

                            } else {

                                curSentence = curSentence.substring(helloStr
                                        .length());
                            }

                        }

                    } else if (curSentence.contains(forever) || listen_directly) {
                        mKeywordsFlag = true;

                        if (!listen_directly) {
                            Log.d("loobot", "loobotdebug !listen_directly");
                            String[] temp = curSentence.split(forever);
                            Log.d("loobot", "loobotdebug  temp.lenth="
                                    + temp.length);
                            if (temp.length > 0) {

                                curSentence = curSentence.substring(temp[0]
                                        .length()

                                        + forever.length());

                            } else {

                                curSentence = curSentence.substring(forever
                                        .length());
                            }

                        }

                    } else {
                        Log.d("loobot", "loobotdebug  helledeelse"
                                + "  isLast=" + isLast);
                        if (isLast)

                            startListening();

                        return;

                    }
                }

            }

            try {

                JSONObject resultJson = new JSONObject(
                        results.getResultString());

                // music_Str = results.getResultString();// 添加

                // Log.d("TAG",

                // "-----------------------------music_Str=:"+music_Str);

                String sn = resultJson.optString("sn");

                mIatResults.put(sn, curSentence);

            } catch (JSONException e) {

                e.printStackTrace();

                Log.d("loobot", "loobotdebug  非连续模式");

                startListenAndSleepTime();

            }

            StringBuffer mIatResultBuffer = new StringBuffer();

            for (String key : mIatResults.keySet()) {

                mIatResultBuffer.append(mIatResults.get(key));

            }

            voiceText = mIatResultBuffer.toString();

            Log.d(TAG, "-voiceText=:" + voiceText);

            // resSize = mWhykeyInfos.size();
            Log.d(TAG, "loobotdebug  resSize=" + resSize);

            if (isLast) {
                Log.d(TAG, " 进入重要方法 if islast" + isLast);

                stopListening();

                if (voiceText.length() < 2) {
                    if (ContiNuousSpeak.equals("YES")) {

                    } else {

                        // 没听懂
                        voiceUnknowHandle();
                    }

                    return;
                }
                if (xunfeiLan == 0) {

                } else {
                    if (!startUnderstand(voiceText)) {

                        if (ContiNuousSpeak.equals("YES")) {

                        } else {
                            Log.d(TAG, "  中文!startUnderstand(voiceText)");
                            // 没听懂
                            voiceUnknowHandle();
                        }

                    }

                }
                curSentence = null;
                if (ContiNuousSpeak.equals("YES")) {

                } else {
                    if (voiceText.length() < 1) {
                        // 没听懂
                        voiceUnknowHandle();

                        listen_directly = false;

                        return;

                    }
                }

            }

        } catch (Exception e) {
            Log.d(TAG, "  重要方法异常" + e);
        }
    }

    ArrayList<String> resArray;
    String texts;
    String json;
    private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener() {

        public void onResult(final UnderstanderResult result) {
            new Thread() {
                public void run() {

                    try {
                        if (null != result) {

                            texts = result.getResultString();// 添加
                            Log.d(TAG, "  result =" + texts);

                            resArray = parseUnderstandResult(result
                                    .getResultString());

                            JSONObject jsonObject = new JSONObject(texts);
                            json = jsonObject.getString("text");
                            json = json.replaceFirst("，", "");
                            json = json.replace("？", "");
                            json = json.replace("。", "");
                            json = json.replace("'", " i");
                            json = json.replace("’", " i");
                            Log.d(TAG, "  json=" + json + " xunfeiLan="
                                    + xunfeiLan);

                            if (json.contains("什么是税收")
                                    || json.contains("税收是什么")) {

                                startGenearting("税收，就是国家凭借政治权力，按照法定的标准，向居民和经-济组织强制地、无偿地征收用以向社会提供公共产品的财政收入。");

                                return;
                            } else if (json.contains("税收有什么特征")
                                    || json.contains("税收特征")) {

                                startGenearting("税收作为政府筹集财政收入的一种规范形式，具有区别于其他财政收入形式的特点。税收特征可以概括为强制性、无偿性和固定性。");
                                return;
                            } else if (json.contains("什么是增值税")
                                    || json.contains("什么事增值税")
                                    || json.contains("增值税")) {
                                startGenearting("增值税是以商品（含应税劳务）在流转过程中产生的增值额作为计税依据而征收的一种流转税。");
                                return;
                            } else if (json.contains("什么是营改增")
                                    || json.contains("营改造")
                                    || json.contains("改造")) {
                                startGenearting("是指以前缴纳营业税的应税项目改成缴纳增值税，增值税只对产品或者服务的增值部分纳税，减少了重复纳税的环节");
                                return;
                            }

                            Log.d(TAG, "  resArray.size()=" + resArray.size());
                            if (resArray.size() == 3) {

                                String text = resArray.get(0);

                                if (!TextUtils.isEmpty(text)) {

                                    Log.d(TAG, "  resArray.get(0)=text="

                                            + text);

                                    if (text.equals("text")) {
                                        Log.d(TAG, "  resArray.get(2)="

                                                + resArray.get(2));
                                        // showFlash("file:///android_asset/motion/speaking.swf");

                                        // showTip("语义_text" + "texts=" +
                                        // texts);
                                        Log.d(TAG, " xunfeilan=" + xunfeiLan);
                                        if (xunfeiLan == 0) {

                                        } else if (xunfeiLan == 1) {
                                            setGeneratorParm();
                                            // Semantic(json);
                                            // startGenearting(answer);
                                            startGenearting(resArray.get(2));

                                        }

                                    } else if (text.equals("music")) { // 新加
                                        music_xunfei();

                                    } else {
                                        Log.d(TAG, " 进入不为语义理解部分");
                                        voiceUnknowHandle();
                                    }

                                } else {
                                    Log.d(TAG, " 进入.get (text)=null");
                                    voiceUnknowHandle();
                                }

                            } else {
                                Log.d(TAG, " 进入.rssize!==3  xunfeiLan="
                                        + xunfeiLan);
                                voiceUnknowHandle();

                            }
                        } else {
                            voiceUnknowHandle();
                        }

                    } catch (Exception e) {
                        Log.d(TAG, " result异常" + e);

                        if (ContiNuousSpeak.equals("YES")) {
                        } else {
                            Log.d(TAG, "  非连续模式");

                            startListenAndSleepTime();
                        }
                    }

                }

                ;
            }.start();

        }

        public void onError(SpeechError error) {

            // 文本语义不能使用回调错误码14002，请确认您下载sdk时是否勾选语义场景和私有语义的发布

            voiceUnknowHandle();

        }

    };

    private void voiceUnknowHandle() {
        startGenearting("你说啥，不好意思，我没听懂");
        // 没听懂或没答案
    }

    String music_xunfei;

    MediaPlayer player;

    int index_music = 0;

    public int[] fn(int n, int t) {

        ArrayList<Integer> numbers = new ArrayList();

        int[] rtnumbers = new int[t];

        for (int i = 0; i < n; i++) { // 初始化数组

            numbers.add(i + 1);

        }

        for (int j = 0; j < t; j++) {

            int raNum = (int) (Math.random() * numbers.size());

            rtnumbers[j] = numbers.get(raNum);

            numbers.remove(raNum);

        }

        return rtnumbers;

    }

    public void music_xunfei() {

        Log.d(TAG, "loobot 进入music_xunfei");

        try {

            Log.d(TAG, "  texts=" + texts);

            JSONObject object = new JSONObject(texts);

            String num = object.getJSONObject("data").getJSONArray("result")

                    .getJSONObject(0).getString("sourceName");

            Log.d(TAG, "  num=" + num);

            if (num.indexOf("音乐") > -1) {

                int musicindex[] = fn(object.getJSONObject("data")

                        .getJSONArray("result").length(), 1);

                Random ran = new Random();
                for (int i = 0; i < object.getJSONObject("data")

                        .getJSONArray("result").length(); i++) {
                    index_music = ran.nextInt(object.getJSONObject("data")

                            .getJSONArray("result").length());
                }

                JSONObject json = object.getJSONObject("data")

                        .getJSONArray("result").getJSONObject(index_music);

                music_xunfei = json.getString("downloadUrl");

                Log.d(TAG, "  music_xunfei=" + music_xunfei);

                if (ContiNuousSpeak.equals("YES")) {

                    stopListening();
                }

                if (player == null) {
                    player = new MediaPlayer();
                }
                player = MediaPlayer.create(xunfeiActivity.this,

                        Uri.parse(music_xunfei));

                // 实例化对象，通过播放本机服务器上的一首音乐

                player.setLooping(false);// 设置不循环播放

                player.start();

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {

                        // TODO Auto-generated method stub

                        if (ContiNuousSpeak.equals("YES")) {
                        } else {
                            Log.d(TAG, "  非连续模式");

                            startListenAndSleepTime();
                        }

                    }

                });

            }

        } catch (Exception e) {

            Log.d(TAG, " 进入music_xunfei异常" + e);

            if (ContiNuousSpeak.equals("YES")) {
            } else {
                Log.d(TAG, "  非连续模式");

                startListenAndSleepTime();
            }
            // TODO: handle exception

        }

    }

    private boolean startUnderstand(String text) {

        if (mTextUnderstander.isUnderstanding()) {

            mTextUnderstander.cancel();

        }

        if (mTextUnderstander.understandText(text, mTextUnderstanderListener) != 0) {

            Log.d("loobotdebug", " loobotdebug understandText error");

            return false;

        }

        return true;

    }

    private void startGenearting(String text) {
        stopListening();
        int code = mTts.startSpeaking(text, mTtsListener);
        Log.d(TAG, "合成code = " + code);
    }

    private void initXunFei() {

        StringBuffer param = new StringBuffer();
        param.append("appid=" + XUNFEI_APPID);
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(xunfeiActivity.this, param.toString());

        // SpeechUtility.createUtility(getApplicationContext(), "appid="
        //
        // + XUNFEI_APPID);

        // Setting.showLogcat(false);

        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        mTextUnderstander = TextUnderstander.createTextUnderstander(this,

                mTextUdrInitListener);

    }

    private SpeechRecognizer mIat;

    private boolean mKeywordsFlag = false;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 听写
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {

            Log.d(TAG, "loobotdebug   SpeechRecognizer init() code = " + code);

            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "loobotdebug SpeechRecongnizer初始化失败，错误码 " + code);
            }

        }

    };

    //

    private String voicer = "nannan";// "xiaoqi";

    private String voicer_eng = "vimary";// "xiaoyan";/* voice actor */

    private SpeechSynthesizer mTts;/* tts variant */

    // 合成
    private InitListener mTtsInitListener = new InitListener() {

        @Override
        public void onInit(int code) {

            Log.d(TAG, " 合成code = " + code);

            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, " 合成初始化失败，错误码 " + code);
                // showTip("初始化失败,错误码："+code);

            } else {

                // 初始化成功，之后可以调用startSpeaking方法

                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，

                // 正确的做法是将onCreate中的startSpeaking调用移至这里

            }

        }

    };
    // 文字理解
    private TextUnderstander mTextUnderstander;

    private InitListener mTextUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {

            Log.d(TAG, "语义理解初始化code = " + code);

            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "loobotdebug  语义理解初始化失败 code = " + code);

            }

        }

    };

    protected void onResume() {

        voiceKeyStringInit();

        setListenerParam();

        setGeneratorParm();

        startListenAndSleepTime();
        super.onResume();

    }

    ;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        if (player != null) {

            player.stop();// 停止播放

            player.release();// 释放资源

            player = null;// 把player对象设置为null

        }
        super.onDestroy();
    }

    private void startListenAndSleepTime() {

        startListening();

    }

    private void startListening() {
        Log.d(TAG, "startlistentime");
        int ret = 0;

        setListenerParam();
        try {
            mKeywordsFlag = false;

            mIatResults.clear();
            // Log.d(TAG, " 听写监听");
            ret = mIat.startListening(recognizerListener);

        } catch (Exception e) {
            Log.d(TAG, "startlistentime异常 ---" + e);
            e.printStackTrace();

        }
        if (ret != ErrorCode.SUCCESS) {

        } else {
        }

    }

    // 合成回调
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        public void onSpeakBegin() {

            // showTip("开始播放");

        }

        public void onSpeakPaused() {

            // showTip("暂停播放");

        }

        public void onSpeakResumed() {

            // showTip("继续播放");

        }

        public void onBufferProgress(int percent, int beginPos, int endPos,

                                     String info) {

        }

        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        public void onCompleted(SpeechError error) {
            Log.d(TAG, "合成error = " + error);
            if (mTextUnderstander.isUnderstanding()) {

                mTextUnderstander.cancel();

            }
            startListenAndSleepTime();

        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }

    };

    // 听写回调
    private RecognizerListener recognizerListener = new RecognizerListener() {

        public void onBeginOfSpeech() {

        }

        @Override
        public void onError(SpeechError error) {
            Log.d(TAG, "听写回调异常 ---" + error);
            stopListening();

            startListening();
        }

        public void onEndOfSpeech() {

            // showTip("end listen");

        }

        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG,
                    "onresult ---" + isLast + "  results="
                            + results.getResultString());
            voiceResultHandle(results, isLast);

        }

        public void onVolumeChanged(int volume, byte[] data) {
            // showTip("volume voice is:" + volume);

        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }

    };

    private void stopListening() {

        // Log.d(TAG, " 进入stop  listening");

        mIat.stopListening();

    }

    private void setGeneratorParm() {

        mTts.setParameter(SpeechConstant.PARAMS, null);

        // 根据合成引擎设置相应参数

        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {

            mTts.setParameter(SpeechConstant.ENGINE_TYPE,

                    SpeechConstant.TYPE_CLOUD);

            // 设置在线合成发音人
            // lwj注释
            if (xunfeiLan == 0)

                mTts.setParameter(SpeechConstant.VOICE_NAME, "catherine");

            if (xunfeiLan == 1)

                mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);// voicer

        } else {

            mTts.setParameter(SpeechConstant.ENGINE_TYPE,

                    SpeechConstant.TYPE_LOCAL);

            // 设置本地合成发音人 voicer为空，默认通过语音+界面指定发音人。

            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        // 设置合成语速

        mTts.setParameter(SpeechConstant.SPEED, "60");

        // 设置合成音调

        mTts.setParameter(SpeechConstant.PITCH, "50");

        // 设置合成音量

        mTts.setParameter(SpeechConstant.VOLUME, "100");

        // 设置播放器音频流类型

        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        // 设置播放合成音频打断音乐播放，默认为true

        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置合成音频保存路径，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限

        mTts.setParameter(SpeechConstant.PARAMS, "tts_audio_path="

                + Environment.getExternalStorageDirectory() + "/test.pcm");

        // mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,

        // "./storage/sdcard1/tx/" + "/loomusic.mp3");

    }

    private void setListenerParam() {

        // clear parameter

        mIat.setParameter(SpeechConstant.PARAMS, null);

        // set listen engine

        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);

        // set result format

        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        switch (xunfeiLan) {

            case 0:
                // Log.d(TAG, "  enus=");
                // set language

                mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");

                break;

            case 1:
                // Log.d(TAG, "  zh_cn=");
                // set language

                mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

                break;

        }

        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理

        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");// 10000

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音

        mIat.setParameter(SpeechConstant.VAD_EOS, "1100");// 3000

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点

        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限

        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,

                Environment.getExternalStorageDirectory()

                        + "/iflytek/wavaudio.pcm");

        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果

        // 注：该参数暂时只对在线听写有效

        mIat.setParameter(SpeechConstant.ASR_DWA, "0");

    }

    private void voiceKeyStringInit() {

        switch (xunfeiLan) {

            case 1:

                helloStr = helloSChi;

                singStr = singStrSChi;

                danceStr = danceStrSChi;

                storyStr = storyStrSChi;

                poemStr = poemStrSChi;

                poem1Str = poem1StrSChi;

                lang1Str = lang1StrSChi;

                lang2Str = lang2StrSChi;

                tell1Str = tell1StrSChi;

                tell2Str = tell2StrSChi;

                callStr = callStrSChi;

                warmStr = warmStrSChi;

                roolStr = roolStrSChi;

                roolStopStr = roolStopStrSChi;

                roolFStr = roolFStrSChi;

                roolBStr = roolBStrSChi;

                roolLStr = roolLStrSChi;

                roolRStr = roolRStrSChi;

                break;

        }

    }

    String helloStr;

    String singStr;

    String danceStr;

    String storyStr;

    String poemStr;

    String poem1Str;

    String lang1Str;

    String lang2Str;

    String tell1Str;

    String tell2Str;

    String callStr;

    String warmStr;

    String roolStopStr;

    String roolStr;

    String roolFStr;

    String roolBStr;

    String roolLStr;

    String roolRStr;

    String helloEng = new String("hi");

    String singStrEng = new String("child song");// song

    String danceStrEng = new String("dance");

    String storyStrEng = new String("story");

    String poemStrEng = new String("read a poem");

    String poem1StrEng = new String("tell me a");

    String lang1StrEng = new String("in chinese");

    String lang2StrEng = new String("in chinese");

    String tell1StrEng = new String("how to say");

    String tell2StrEng = new String("how to speak");

    String callStrEng = new String("call");

    String warmStrEng = new String("warm up");

    String roolStopStrEng = new String("stop");

    String roolStrEng = new String("roll");

    String roolFStrEng = new String("front");

    String roolBStrEng = new String("back");

    String roolLStrEng = new String("left");

    String roolRStrEng = new String("right");

    String helloSChi = new String("小宝");

    String singStrSChi = new String("儿歌");

    String danceStrSChi = new String("跳舞");//

    String storyStrSChi = new String("故事");

    String poemStrSChi = new String("背首");

    String poem1StrSChi = new String("背一首");

    String lang1StrSChi = new String("英文");

    String lang2StrSChi = new String("英语");

    String tell1StrSChi = new String("怎么说");

    String tell2StrSChi = new String("怎么讲");

    String callStrSChi = new String("打电话");

    String warmStrSChi = new String("热身");

    String roolStrSChi = new String("滚");

    String roolStopStrSChi = new String("停止");

    String roolFStrSChi = new String("前");

    String roolBStrSChi = new String("后");

    String roolLStrSChi = new String("左");

    String roolRStrSChi = new String("右");


}
