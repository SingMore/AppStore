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
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SingMore on 2017/3/15.
 */

public class ResetPasswordActivity extends BaseActivity {

    private EditText et_useremail;
    private Button bt_reset;

    @Override
    public void initView() {
        setContentView(R.layout.activity_resetpsd);

        et_useremail = (EditText) findViewById(R.id.et_useremail_reset);
        bt_reset = (Button) findViewById(R.id.bt_reset);

    }

    @Override
    public void initListener() {
        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String useremail = et_useremail.getText().toString().trim();
                if (TextUtils.isEmpty(useremail)) {
                    Toast.makeText(UIUtils.getContext(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobUser.resetPasswordByEmail(useremail, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(UIUtils.getContext(), "重置密码请求成功，请到" + useremail + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), "重置密码请求失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
