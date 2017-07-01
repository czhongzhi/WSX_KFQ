package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.IM_Util.XmppUtil;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.HttpUtil;
import com.inetgoes.fangdd.util.UIHandler;
import com.inetgoes.fangdd.view.CustomTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;


/**
 * 经纪人加盟一
 */
public class RegistMainActivity extends Activity implements View.OnClickListener {
    private EditText ed_phone;
    private EditText ed_code;
    private Button but_getcode;
    private Button but_register;
    private Dialog waitDialog;
    private String phString;
    private String vcString;

    private EventHandler eHandler;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            Log.e("czhongzhi", "event -- " + event + " result --" + result);

            if (result == SMSSDK.RESULT_COMPLETE) {//短信成功

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    //关闭dialog
                    if (waitDialog != null && waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }
                    //关闭本页面
                    finish();
                    //进入注册页面二
                    Intent intent = new Intent(RegistMainActivity.this, RegistTwoActivity.class);
                    intent.putExtra(Constants.brokerphone, phString);
                    startActivity(intent);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//验证码发送成功

                    //开启验证码已发送dialog
                    showDialogSendCode();
                }
            } else {
                //关闭dialog提示
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    showDialogErroCode();
                }
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE && data != null && (data instanceof UserInterruptException)) {
                    // 由于此处是开发者自己决定要中断发送的，因此什么都不用做
                    return;
                }

                ((Throwable) data).printStackTrace();
                Throwable throwable = (Throwable) data;
                try {
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegistMainActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "房产专家加盟", true, false);
        setContentView(R.layout.activity_regist_one);
        initView();
        SMSSDK.registerEventHandler(eHandler);
    }

    private void initView() {
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_code = (EditText) findViewById(R.id.ed_autocode);
        but_getcode = (Button) findViewById(R.id.but_getcode);
        but_register = (Button) findViewById(R.id.but_register);
        TextView tvProtocol = (TextView) findViewById(R.id.app_protocol);


        tvProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvProtocol.getPaint().setAntiAlias(true);//抗锯齿
        tvProtocol.setOnClickListener(this);
        but_getcode.setOnClickListener(this);
        but_register.setOnClickListener(this);

        String tempPhone = AppSharePrefManager.getInstance(this).getLastest_login_phone_num();
        if (!TextUtils.isEmpty(tempPhone)) {
            ed_phone.setText(tempPhone);
        }
    }

    /**
     * 开启验证码已发送dialog
     */
    private void showDialogSendCode() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this, R.style.Dialog);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(this).inflate(R.layout.register_dialog_send, null);
        dialog.setContentView(view);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        }, 1500);
    }


    /**
     * 验证码错误
     */
    private void showDialogErroCode() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(this).inflate(R.layout.register_dialog_erro, null);
        view.findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        //p.height = (int) (d.getHeight()*0.6);
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_getcode://获取验证码
                phString = ed_phone.getText().toString().trim();
                Log.e("czhongzhi", phString);
                if (TextUtils.isEmpty(phString)) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!HttpUtil.isNetworkAble(RegistMainActivity.this)) {
                    Toast.makeText(RegistMainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开启验证码倒计时，在倒计时时不能点击按钮
                countDown();
                SMSSDK.getVerificationCode("86", phString);
                break;
            case R.id.but_register://立即加盟

                //绕过验证
//                {
//                phString = ed_phone.getText().toString().trim();
//                Intent intent=new Intent(RegistMainActivity.this, RegistTwoActivity.class);
//                Log.e("RegisMainActivity", phString==null?"phString是空的":"phString不是空的");
//                intent.putExtra(Constants.brokerphone, "15217721685");
//                startActivity(intent);}

                vcString = ed_code.getText().toString().trim();
                if (TextUtils.isEmpty(vcString)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!HttpUtil.isNetworkAble(RegistMainActivity.this)) {
                    Toast.makeText(RegistMainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", phString, vcString);
                //开启dialog提示
                waitDialog = DialogUtil.showWait(this);
                break;
            case R.id.app_protocol://法律声明和隐私政策
                Toast.makeText(this, "法律声明和隐私政策", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        eHandler = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eHandler);
    }

    private static final int RETRY_INTERVAL = 60;
    private int time = RETRY_INTERVAL;
    private boolean isReturnVerifyCode = false;

    /**
     * 倒数计时
     */
    private void countDown() {
        runOnUIThread(new Runnable() {
            public void run() {
                time--;

                if (time == 0 || isReturnVerifyCode) {
                    but_getcode.setEnabled(true);
                    but_getcode.setText("获取验证码");
                    time = RETRY_INTERVAL;
                } else {
                    String unReceive = "<font color='#8f8f90'>" + time + "</font>S";
                    but_getcode.setText(Html.fromHtml(unReceive));
                    but_getcode.setEnabled(false);
                    runOnUIThread(this, 1000);
                }
            }
        }, 1000);
    }

    private void runOnUIThread(final Runnable paramRunnable, long paramLong) {
        UIHandler.sendEmptyMessageDelayed(0, paramLong, new Handler.Callback() {
            public boolean handleMessage(Message paramAnonymousMessage) {
                paramRunnable.run();
                return false;
            }
        });
    }

    private void runOnUIThread(final Runnable paramRunnable) {
        UIHandler.sendEmptyMessage(0, new Handler.Callback() {
            public boolean handleMessage(Message paramAnonymousMessage) {
                paramRunnable.run();
                return false;
            }
        });
    }

    private class RegOpenfire extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            int reg = XmppUtil.getInstance().regist(params[0], params[0]);
            if (reg == 1 || reg == 2) {
                Log.e("czhongzhi", "openfire 注册成功或已存在");
                return true;
            } else {
                Log.e("czhongzhi", "openfire 注册失败");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                FangApplication.getFangApplication().handler.sendEmptyMessage(10011);
            }
            //关闭dialog提示
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();

                startActivity(new Intent(RegistMainActivity.this, MainActivity.class));
            }
        }

    }

    /**
     * 手机型号
     *
     * @return String
     */
    private String getAndroidType() {

        return "手机厂商:" + android.os.Build.MANUFACTURER + "型号:" + android.os.Build.MODEL;
    }


    /**
     * 开启正在登陆dialog
     */
//    private void showDialogLogin() {
//        AlertDialog.Builder builder2 = new AlertDialog.Builder(this, R.style.Dialog);
//        dialog_login = builder2.show();
//        View view = LayoutInflater.from(this).inflate(R.layout.register_dialog_login, null);
//        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.mate_wait_rotate);
//        anim.setInterpolator(new LinearInterpolator());
//        wait_icon.setAnimation(anim);
//        dialog_login.setContentView(view);
//    }


}


