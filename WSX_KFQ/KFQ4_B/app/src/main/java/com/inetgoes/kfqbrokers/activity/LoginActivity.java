package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.IM_Util.XmppUtil;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.LoginInfo;
import com.inetgoes.kfqbrokers.utils.HttpUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录页
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText edPhone;
    private EditText edPassword;
    private Button butRegister;
    private TextView appProtocol;
    private TextView brokerRegist;
    private TextView nopassword;

    private AlertDialog dialog_login;
    private String phoneString;//手机号
    private String PasswordString;


    private LoginInfo loginInfo;
    private AppSharePrefManager sManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        CustomTitleBar.getTitleBar(this, "登录", true, false);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        edPhone = (EditText) findViewById(R.id.ed_phone);
        edPassword = (EditText) findViewById(R.id.ed_password);
        butRegister = (Button) findViewById(R.id.but_login);
        appProtocol = (TextView) findViewById(R.id.app_protocol);
        brokerRegist = (TextView) findViewById(R.id.broker_regist);
        nopassword = (TextView) findViewById(R.id.nopassword);


        butRegister.setOnClickListener(this);
        brokerRegist.setOnClickListener(this);
        nopassword.setOnClickListener(this);

        String tempPhone = AppSharePrefManager.getInstance(this).getLastest_login_phone_num();
        if (!TextUtils.isEmpty(tempPhone)) {
            edPhone.setText(tempPhone);
        }

    }

    /**
     * 开启正在登陆dialog
     */
    private void showDialogLogin() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this, R.style.Dialog);
        dialog_login = builder2.show();
        View view = LayoutInflater.from(this).inflate(R.layout.register_dialog_login, null);
        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.mate_wait_rotate);
        anim.setInterpolator(new LinearInterpolator());
        wait_icon.setAnimation(anim);
        dialog_login.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.but_login:
                //登录
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);

                phoneString = edPhone.getText().toString().trim();
                PasswordString = edPassword.getText().toString().trim();

                if (TextUtils.isEmpty(phoneString)) {
                    Toast.makeText(this, "手机不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //正则判断手机号
                if (TextUtils.isEmpty(PasswordString)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!HttpUtil.isNetworkAble(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开启正在登陆dialog
                showDialogLogin();
                queryAccount(phoneString, PasswordString);
                break;

            case R.id.broker_regist:
                //注册页面
                intent = new Intent(this, RegistMainActivity.class);
                startActivity(intent);
                break;

            case R.id.nopassword:
                //忘记密码页面
                intent = new Intent(this, RePasswordActivity.class);
                startActivity(intent);
                break;
        }

    }

    //登录的方法
    private void queryAccount(final String phone, String password) {

        sManager = AppSharePrefManager.getInstance(this);
        Map<String, Object> map = new HashMap<>();
        map.put("cellphone", phone);
        map.put("pwd", password);

        //查询账号
        new HttpAsy(new PostExecute() {
            private Boolean state;

            @Override
            public void onPostExecute(String result) {
                L.LogE("Login result is " + result);
                //关闭dialog提示
                if (dialog_login != null && dialog_login.isShowing())
                    dialog_login.dismiss();
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                ObjectMapper mapper = JacksonMapper.getObjectMapper();
                try {
                    loginInfo = mapper.readValue(result, LoginInfo.class);
                    if (loginInfo == null)
                        return;
                    //登陆状态
                    state = loginInfo.isState();
                    if (!state) {//账号密码错误
                        //登陆失败的理由
                        String reason = loginInfo.getReason();
                        Toast.makeText(LoginActivity.this, reason, Toast.LENGTH_SHORT).show();
                        return;
                    } else {//账号正确

                        //保存经纪人id
                        sManager.setLastest_login_id(loginInfo.getUserid());

                        if(sManager.getLastest_login_phone_num().equals(phoneString)){

                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("loginInfo", loginInfo);
                            startActivity(intent);

                            return;
                        }else{

                            //聊天账号注册
                            new RegOpenfire().execute(String.valueOf(loginInfo.getUserid()));

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.loginUrl, map);
    }

    private class RegOpenfire extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            int reg = XmppUtil.getInstance().regist(params[0], params[0]);
            if (reg == 1 || reg == 2) {
                Log.e("czhongzhi", "loginAct openfire 注册成功或已存在");
                return true;
            } else {
                Log.e("czhongzhi", "loginAct openfire 注册失败");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.e("RegisTwoActivity", "loginAct openfire 注册成功或已存在");

            } else {
                Log.e("RegisTwoActivity", "loginAct openfire 注册失败");
            }

            // 保存手机号
            sManager.setLastest_login_phone_num(phoneString);

            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("loginInfo", loginInfo);
            startActivity(intent);
        }
    }
}