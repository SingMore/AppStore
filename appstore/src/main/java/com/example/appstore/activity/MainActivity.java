package com.example.appstore.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStripExtends;
import com.example.appstore.R;
import com.example.appstore.adapter.MainFragmentStateAdapter;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.base.BaseFragment;
import com.example.appstore.factory.FragmentFactory;
import com.example.appstore.utils.JsonParser;
import com.example.appstore.utils.LogUtils;
import com.example.appstore.utils.UIUtils;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class MainActivity extends BaseActivity {

    private EditText et_search;
    private ImageView iv_voice;
    private Button bt_search;
    private PagerSlidingTabStripExtends mTabs;
    private ViewPager mViewPager;

    private String[] mMainTitles;
    private BaseFragment fragment;

    private String search_content;//搜索内容


    /**
     * 初始化布局
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        bt_search = (Button) findViewById(R.id.bt_search);
        mTabs = (PagerSlidingTabStripExtends) findViewById(R.id.psts_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
    }

    @Override
    public void initData() {
        mMainTitles = UIUtils.getStringArr(R.array.main_titles);

        //MainPagerAdapter adapter = new MainPagerAdapter();
        //MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        MainFragmentStateAdapter adapter = new MainFragmentStateAdapter(getSupportFragmentManager(), mMainTitles);
        mViewPager.setAdapter(adapter);

        //绑定tabs和ViewPager
        mTabs.setViewPager(mViewPager);

    }

    @Override
    public void initListener() {
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragment = FragmentFactory.getFragment(position);
                if (fragment != null) {
                    fragment.getLoadingPager().loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_content = et_search.getText().toString().trim();
                if(TextUtils.isEmpty(search_content)) {
                    Toast.makeText(UIUtils.getContext(), "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                jumpToResult();
            }
        });

        //点击语音图标触发事件
        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //语音识别
                speechRecognize();
            }
        });
    }

    private void speechRecognize() {
        //初始化语音输入UI
        initSpeechUI();
/*
        //1.创建SpeechRecognizer对象，第二个参数： 本地识别时传InitListener
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(UIUtils.getContext(), null);
        //2.设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        //3.开始听写
        mIat.startListening(new RecognizerListener() {
            //听写结果回调接口(返回Json格式结果，用户可参见附录13.1)；
            //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
            //关于解析Json的代码可参见Demo中JsonParser类；
            //isLast等于true时会话结束。
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                System.out.println("result:" + recognizerResult.getResultString ());
                if (!isLast){
                    resultStr += recognizerResult.getResultString() + ",";
                    return;
                }
                resultStr += recognizerResult.getResultString() + "]";
                Log.d("wqz:", "results: " + resultStr);
                List localList = (List)new Gson().fromJson(resultStr, new TypeToken(){}.getType());
                String str = "";
                for (int i = 0; ; i++){
                    if (i >= -1 + localList.size()){
                        getResponse(str);
                        return;
                    }
//                    str = str + ((DictationResult)localList.get(i)).toString();
                }

            }

            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
*/
    }


    private void initSpeechUI() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(MainActivity.this, new InitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                LogUtils.e("wqz", "语音识别结果是：" + recognizerResult.getResultString());
                search_content = JsonParser.parseIatResult(recognizerResult.getResultString());
                LogUtils.e("wqz", "语音识别转换之后结果是：" + search_content);

                if(TextUtils.isEmpty(search_content)) {
                    return;
                }
                jumpToResult();
                return;
            }

            @Override
            public void onError(SpeechError speechError) {
                LogUtils.e("语音识别失败：" + speechError.getErrorDescription());
                Toast.makeText(UIUtils.getContext(), "语音识别失败", Toast.LENGTH_SHORT).show();
            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    /**
     * 跳转到搜索结果页面
     */
    private void jumpToResult() {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("search_content", search_content);
        startActivity(intent);
    }


}
