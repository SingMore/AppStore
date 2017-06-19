package com.example.appstore.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.activity.AppManageActivity;
import com.example.appstore.activity.ChatActivity;
import com.example.appstore.activity.LoginActivity;
import com.example.appstore.activity.UserDetailActivity;
import com.example.appstore.base.BaseFragment;
import com.example.appstore.base.LoadingPager.LoadedResult;
import com.example.appstore.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;

/**
 * Created by SingMore on 2017/3/8.
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.rl_user)
    RelativeLayout rl_user;

    @ViewInject(R.id.tv_userName)
    TextView tv_userName;

    @ViewInject(R.id.tv_userNum)
    TextView tv_userNum;

    @ViewInject(R.id.rl_installed)
    RelativeLayout rl_installed;

    @ViewInject(R.id.rl_setting)
    RelativeLayout rl_setting;

    @ViewInject(R.id.rl_robot)
    RelativeLayout rl_robot;

    @ViewInject(R.id.rl_logout)
    RelativeLayout rl_logout;

    private BmobUser currentUser;//当前登录的用户

    @Override
    public LoadedResult initData() {
        currentUser = BmobUser.getCurrentUser();
        return LoadedResult.SUCCESS;
    }

    @Override
    public View initSuccessView() {
        View personView = View.inflate(UIUtils.getContext(), R.layout.fragment_person, null);
        ViewUtils.inject(this, personView);

        //拿到数据显示到控件上
        displayData();

        initListener();
        return personView;
    }

    private void displayData() {
        if (currentUser != null) {
            tv_userName.setText(currentUser.getUsername());
            tv_userNum.setText(currentUser.getMobilePhoneNumber());
        } else {
            tv_userName.setText("暂未登录");
            tv_userNum.setText("-----------");
        }
    }

    private void initListener() {
        rl_user.setOnClickListener(this);
        rl_installed.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_robot.setOnClickListener(this);
        rl_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user:
                //判断是否已登录
                if (currentUser != null) {//已登录，进入用户信息详情界面
                    Intent intent = new Intent(UIUtils.getContext(), UserDetailActivity.class);
                    startActivity(intent);
                } else {//未登录，进入登录界面
                    Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_installed:
                //进入应用管理界面
                Intent intent2 = new Intent(UIUtils.getContext(), AppManageActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_setting:
                //进入设置界面
                Toast.makeText(UIUtils.getContext(), "更多强大功能，敬请期待~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_robot:
                //进入客服聊天界面
                Intent intent3 = new Intent(UIUtils.getContext(), ChatActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_logout:
                //退出登录
                if(currentUser != null) {
                    BmobUser.logOut();//清除缓存用户对象
                    Toast.makeText(UIUtils.getContext(), "注销成功~", Toast.LENGTH_SHORT).show();
                    currentUser = BmobUser.getCurrentUser();
                    displayData();
                }else {
                    Toast.makeText(UIUtils.getContext(), "您还未登录^_^", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
