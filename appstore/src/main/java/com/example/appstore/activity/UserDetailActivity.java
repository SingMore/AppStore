package com.example.appstore.activity;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstore.R;
import com.example.appstore.base.BaseActivity;
import com.example.appstore.utils.UIUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SingMore on 2017/3/14.
 */

public class UserDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private RelativeLayout rl_username;
    private TextView tv_username;
    private RelativeLayout rl_userpassword;
    private RelativeLayout rl_userphone;
    private TextView tv_userphone;
    private RelativeLayout rl_useremail;
    private TextView tv_useremail;

    private BmobUser currentUser;//当前登录的用户
    private static final int ALBUM_OK = 0;//从图库选择图片的请求码
    private String picPath;//从图库选中的图片路径

    @Override
    public void initView() {
        setContentView(R.layout.activity_userdetail);
        findViewById();
    }

    @Override
    public void initData() {
        currentUser = BmobUser.getCurrentUser();
        tv_username.setText(currentUser.getUsername());
        tv_userphone.setText(currentUser.getMobilePhoneNumber());
        tv_useremail.setText(currentUser.getEmail());
    }

    @Override
    public void initListener() {
        iv_back.setOnClickListener(this);
        rl_username.setOnClickListener(this);
        rl_userpassword.setOnClickListener(this);
        rl_userphone.setOnClickListener(this);
        rl_useremail.setOnClickListener(this);
    }

    private void findViewById() {
        iv_back = (ImageView) findViewById(R.id.iv_back_userdetail);
        rl_username = (RelativeLayout) findViewById(R.id.rl_username_userdetail);
        tv_username = (TextView) findViewById(R.id.tv_username_userdetail);
        rl_userpassword = (RelativeLayout) findViewById(R.id.rl_userpassword_userdetail);
        rl_userphone = (RelativeLayout) findViewById(R.id.rl_userphone_userdetail);
        tv_userphone = (TextView) findViewById(R.id.tv_userphone_userdetail);
        rl_useremail = (RelativeLayout) findViewById(R.id.rl_useremail_userdetail);
        tv_useremail = (TextView) findViewById(R.id.tv_useremail_userdetail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_userdetail:
                this.finish();
                break;
            case R.id.rl_username_userdetail:
                //弹出对话框，修改用户名
                popNameDialog();
                break;
            case R.id.rl_userpassword_userdetail:
                //弹出对话框，修改用户密码
                popPasswordDialog();
                break;
            case R.id.rl_userphone_userdetail:
                //弹出对话框，修改用户手机号
                popPhoneNumDialog();
                break;
            case R.id.rl_useremail_userdetail:
                //弹出对话框，修改用户邮箱
                popEmailDialog();
        }
    }

    /**
     * 弹出修改用户邮箱的对话框
     */
    private void popEmailDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(UIUtils.getContext(), R.layout.dialog_updateemail, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText et_useremail = (EditText) view.findViewById(R.id.et_useremail_dialog);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit_updateemail_dialog);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_updateemail_dialog);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = et_useremail.getText().toString().trim();
                if (TextUtils.isEmpty(useremail)) {
                    Toast.makeText(UIUtils.getContext(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //更新用户名到服务器
                    updateEmailToServer(useremail);
                    dialog.dismiss();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 弹出修改用户手机号的对话框
     */
    private void popPhoneNumDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(UIUtils.getContext(), R.layout.dialog_updatephone, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText et_userphone = (EditText) view.findViewById(R.id.et_userphone_dialog);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit_updatephone_dialog);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_updatephone_dialog);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userphone = et_userphone.getText().toString().trim();
                if (TextUtils.isEmpty(userphone)) {
                    Toast.makeText(UIUtils.getContext(), "手机号不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //更新用户名到服务器
                    updatePhoneToServer(userphone);
                    dialog.dismiss();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 弹出修改用户密码的对话框
     */
    private void popPasswordDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(UIUtils.getContext(), R.layout.dialog_updatepassword, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText et_user_oldpsd = (EditText) view.findViewById(R.id.et_user_oldpsd_dialog);
        final EditText et_user_newpsd = (EditText) view.findViewById(R.id.et_user_newpsd_dialog);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit_updatepassword_dialog);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_updatepassword_dialog);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpsd = et_user_oldpsd.getText().toString().trim();
                String newpsd = et_user_newpsd.getText().toString().trim();
                if (TextUtils.isEmpty(oldpsd) || TextUtils.isEmpty(newpsd)) {
                    Toast.makeText(UIUtils.getContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.equals(oldpsd, newpsd)) {
                    Toast.makeText(UIUtils.getContext(), "新旧密码一样了哦~", Toast.LENGTH_SHORT).show();
                }else{
                    //直接更改当前登录用户的密码
                    currentUser.updateCurrentUserPassword(oldpsd, newpsd, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {//更新到服务器成功
                                Toast.makeText(UIUtils.getContext(), "修改密码成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UIUtils.getContext(), "亲的网络不太好哦~", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 弹出修改用户名的对话框
     */
    private void popNameDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(UIUtils.getContext(), R.layout.dialog_updatename, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText et_username = (EditText) view.findViewById(R.id.et_username_dialog);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit_updatename_dialog);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_updatename_dialog);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(UIUtils.getContext(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    //更新用户名到服务器
                    updateNameToServer(username);
                    dialog.dismiss();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * 更新用户名到服务器
     */
    private void updateNameToServer(final String username) {
        BmobUser newUser = new BmobUser();
        newUser.setUsername(username);
        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {//更新到服务器成功
                    //更新显示到控件上
                    tv_username.setText(username);
                } else {
                    Toast.makeText(UIUtils.getContext(), "亲的网络不太好哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 更新用户手机号到服务器
     */
    private void updatePhoneToServer(final String userphone) {
        BmobUser newUser = new BmobUser();
        newUser.setMobilePhoneNumber(userphone);
        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {//更新到服务器成功
                    //更新显示到控件上
                    tv_userphone.setText(userphone);
                } else {
                    Toast.makeText(UIUtils.getContext(), "亲的网络不太好哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 更新用户邮箱到服务器
     */
    private void updateEmailToServer(final String useremail) {
        BmobUser newUser = new BmobUser();
        newUser.setEmail(useremail);
        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {//更新到服务器成功
                    //更新显示到控件上
                    tv_useremail.setText(useremail);
                } else {
                    Toast.makeText(UIUtils.getContext(), "亲的网络不太好哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*

    */
/**
 * 从系统图库选择一张图片
 *//*

    private void choosePic() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*/
/*");
        startActivityForResult(albumIntent, ALBUM_OK);
    }

    */
/**
 * 当选中一张图片后调用此方法
 *//*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_OK && resultCode == RESULT_OK && data != null) {
            //拿到图片路径
            getPicPath(data);

            //更新头像到服务器
            updatePicToServer();
        }
    }

    */
/**
 * 获取选中图片的路径
 *//*

    private void getPicPath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picPath = cursor.getString(columnIndex);
        cursor.close();
    }

    */
/**
 * 更新头像到服务器
 *//*

    private void updatePicToServer() {
        //新建一个用户，用来进行数据更新操作
        //此方法只会更新你提交的用户信息（比如只会向服务器提交当前用户的头像），而不会将本地存储的用户信息也提交到后台更新。
        MyUser newUser = new MyUser();
        BmobFile picFile = new BmobFile(new File(picPath));
        newUser.setUsericon(picFile);
        newUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {//更新到服务器成功
                    //更新显示到控件上
                    iv_usericon.setImageBitmap(BitmapFactory.decodeFile(picPath));
                } else {
                    Toast.makeText(UIUtils.getContext(), "亲的网络不太好哦~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/



}
