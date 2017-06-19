package com.example.appstore.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.adapter.ChatMessageAdapter;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.bean.ChatMessage;
import com.example.appstore.bean.ResponseBean;
import com.example.appstore.conf.Constants;
import com.example.appstore.factory.ThreadPoolFactory;
import com.example.appstore.utils.LogUtils;
import com.example.appstore.utils.UIUtils;
import com.google.gson.Gson;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

/**
 * Created by SingMore on 2017/3/20.
 */

public class ChatActivity extends BaseActivity {
    private ListView lv_msg;
    private EditText et_input;
    private Button bt_send;

    private List<ChatMessage> mDataList;
    private ChatMessageAdapter adapter;

    private TuringApiManager manager;

    private String response;//获得回复消息

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (adapter == null) {
                adapter = new ChatMessageAdapter(mDataList);
                lv_msg.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
                lv_msg.setSelection(mDataList.size());
            }
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_chat);
        findViewById();

//        initTuring();
    }

    private void initTuring() {
        //初始化TuringSDK
        SDKInitBuilder builder = new SDKInitBuilder(UIUtils.getContext());
        builder.setTuringKey(Constants.TURING_KEY);
        builder.setSecret(Constants.TURING_SECRET);
        SDKInit.init(builder, new InitListener() {
            //初始化成功后再实例化TuringApiManager,否则很多功能不能使用
            @Override
            public void onComplete() {
                LogUtils.e("wqz", "初始化图灵SDK成功");

                //实例化 TuringApiManager 类
                manager = new TuringApiManager(UIUtils.getContext());

                MyHttpConnectionListener listener = new MyHttpConnectionListener();
                manager.setHttpListener(listener);
            }

            @Override
            public void onFail(String s) {
                LogUtils.e("wqz", "初始化图灵SDK失败");
            }
        });
    }

    private void findViewById() {
        lv_msg = (ListView) findViewById(R.id.lv_msg_chat);
        et_input = (EditText) findViewById(R.id.et_input_msg_chat);
        bt_send = (Button) findViewById(R.id.bt_send_chat);
    }

    @Override
    public void initData() {
        mDataList = new ArrayList<ChatMessage>();
        mDataList.add(new ChatMessage("你好，小宝为你服务", ChatMessage.Type.INCOMING, new Date()));
        adapter = new ChatMessageAdapter(mDataList);

        lv_msg.setAdapter(adapter);
    }


    @Override
    public void initListener() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(UIUtils.getContext(), "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mDataList.add(new ChatMessage(msg, ChatMessage.Type.OUTCOMING, new Date()));
                //通知刷新
                adapter.notifyDataSetChanged();
//                lv_msg.setSelection(mDataList.size());
                et_input.setText("");

                getResponseData(msg);

//                //发送请求，在onSuccess()里回调成功数据
//                new Thread() {
//                    @Override
//                    public void run() {
//                        manager.requestTuringAPI(msg);
//                    }
//                }.start();

            }
        });
    }


    public class MyHttpConnectionListener implements HttpConnectionListener {

        @Override
        public void onError(ErrorMessage errorMessage) {
            LogUtils.e("wqz", "失败后回调：" + errorMessage.getMessage());
        }

        @Override
        public void onSuccess(RequestResult requestResult) {
            LogUtils.e("wqz", "成功后回调：" + requestResult.getContent().toString());
            processResponse(requestResult);
        }
    }

    private void processResponse(RequestResult requestResult) {
        String json = requestResult.getContent().toString();
        Gson gson = new Gson();
        ResponseBean responseBean = gson.fromJson(json, ResponseBean.class);
        mDataList.add(new ChatMessage(responseBean.text, ChatMessage.Type.INCOMING, new Date()));

        handler.sendEmptyMessage(0);
    }


    /**
     * 直接通过网络请求图灵API，获得回复消息
     *
     * @param msg
     */
    private void getResponseData(String msg) {
        try {
            //一定要将其变成utf-8,防止乱码
            msg = URLEncoder.encode(msg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //直接请求图灵API的URL地址
        final String urlStr = Constants.TURING_API + "?key=" + Constants.TURING_KEY + "&info=+" + msg;

        //开启子线程访问网络，使用线程池管理
        ThreadPoolFactory.getNormalThreadPool().executeTask(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
                    conn.connect();

                    //打开这个页面的输入流，这个网站的内容以字节流的形式返回。如果是网页就返回html，图片就返回图片的内容。
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    response = baos.toString();
                    //返回的JSON，弄成字符串后去掉头和尾就行
                    response = response.substring(23, response.length() - 2);
                    System.out.println("收到消息：" + response);
                    mDataList.add(new ChatMessage(response, ChatMessage.Type.INCOMING, new Date()));
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mDataList.add(new ChatMessage("亲的网络有点问题哦~", ChatMessage.Type.INCOMING, new Date()));
                    handler.sendEmptyMessage(0);
                } finally {
                    if (is != null && baos != null) {
                        try {
                            is.close();
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }


}

