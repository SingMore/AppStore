package com.example.appstore.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.utils.UIUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by SingMore on 2017/3/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_userpsd;
    private Button bt_login;
    private TextView tv_unremember;
    private TextView tv_register;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        findViewById();
    }

    @Override
    public void initListener() {
        bt_login.setOnClickListener(this);
        tv_unremember.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    private void findViewById() {
        et_username = (EditText) findViewById(R.id.et_username_login);
        et_userpsd = (EditText) findViewById(R.id.et_userpsd_login);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_unremember = (TextView) findViewById(R.id.tv_unremember_login);
        tv_register = (TextView) findViewById(R.id.tv_register_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                //登录
                login();
                break;
            case R.id.tv_unremember_login:
                //进入重置密码页面
                enterResetPassword();
                break;
            case R.id.tv_register_login:
                //进入注册页面
                enterRegister();
                break;
        }
    }

    /**
     * 执行登录操作
     */
    private void login() {
        String username = et_username.getText().toString().trim();
        String userpsd = et_userpsd.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(userpsd)) {
            Toast.makeText(UIUtils.getContext(), "用户名密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        //此方法支持用户名+密码、邮箱+密码、手机号+密码登录
        BmobUser.loginByAccount(username, userpsd, new LogInListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (user != null) {//登录成功,进入用户详细信息界面
                    Intent intent = new Intent(LoginActivity.this, UserDetailActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //登录失败，弹出提示
                    Toast.makeText(UIUtils.getContext(), "用户名密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 进入重置密码页面
     */
    private void enterResetPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 进入注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}
