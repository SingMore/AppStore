package com.example.appstore.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.utils.UIUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by SingMore on 2017/3/15.
 */

public class RegisterActivity extends BaseActivity {

    private EditText et_username;
    private EditText et_userpsd;
    private EditText et_userphone;
    private EditText et_useremail;
    private Button bt_register;

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        findViewById();
    }

    private void findViewById() {
        et_username = (EditText) findViewById(R.id.et_username_register);
        et_userpsd = (EditText) findViewById(R.id.et_userpsd_register);
        et_userphone = (EditText) findViewById(R.id.et_userphone_register);
        et_useremail = (EditText) findViewById(R.id.et_useremail_register);
        bt_register = (Button) findViewById(R.id.bt_register);
    }

    @Override
    public void initListener() {
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String userpsd = et_userpsd.getText().toString().trim();
                String userphone = et_userphone.getText().toString().trim();
                String useremail = et_useremail.getText().toString().trim();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(userpsd)
                        || TextUtils.isEmpty(userphone) || TextUtils.isEmpty(useremail)) {
                    Toast.makeText(UIUtils.getContext(), "请将信息补充完整！", Toast.LENGTH_SHORT).show();
                    return;
                }

                BmobUser user = new BmobUser();
                user.setUsername(username);
                user.setPassword(userpsd);
                user.setMobilePhoneNumber(userphone);
                user.setEmail(useremail);

                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(user != null) {//注册成功
                            Toast.makeText(UIUtils.getContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(UIUtils.getContext(), "注册失败！请检查邮箱是否正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
