package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.IM_Util.XmppUtil;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.service.PushService;
import com.inetgoes.kfqbrokers.utils.AppUtil;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.HttpUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.utils.UIHandler;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;

/**
 * 重设密码界面
 */
public class RePasswordActivity extends Activity implements View.OnClickListener {

    private EditText edPhone;
    private EditText edAutocode;
    private Button butGetcode;
    private EditText edId;
    private EditText edPassword;
    private EditText edRepassword;
    private Button butRegister;
    private TextView appProtocol;
    private Dialog waitDialog;

    private String phString; //手机号码
    private String vcString; //验证码
    private String IID;    //身份证
    private String newpwdone; //新密码一
    private String newpwdtwo;  //新密码二

    private EventHandler eHandler;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            Log.e("czhongzhi", "event -- " + event + " result --" + result);

            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    //调用方法提交重设密码信息
                    resetPwd(phString,IID,newpwdone);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //验证码已经发送
                    //开启dialog提示
                    showDialogSendCode();

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(RePasswordActivity.this, "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {

                showDialogErroCode();

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
                        Toast.makeText(RePasswordActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void resetPwd(String phString, String iid, String newpwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("phoneno", phString);
        map.put("shenfenzhengno", iid);
        map.put("password", newpwd);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if(waitDialog != null && waitDialog.isShowing()){
                    waitDialog.dismiss();
                }

                Log.e("czhongzhi", "resetPwd is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(RePasswordActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                //
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if ((boolean) resMap.get("state")) {
                    RePasswordActivity.this.finish();
                    exitUserInfo();
                } else {
                    Toast.makeText(RePasswordActivity.this, "" + resMap.get("reason"), Toast.LENGTH_SHORT).show();
                }


            }
        }).execute(Constants.resetPwdUrl, map);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "重设密码", true, false);
        setContentView(R.layout.activity_re_password);
        initView();
        SMSSDK.registerEventHandler(eHandler);


    }

    private void exitUserInfo() {
        String phone = AppSharePrefManager.getInstance(this).getLastest_login_phone_num();
        if (AppSharePrefManager.getInstance(this).getEdit().clear().commit()) {
            AppSharePrefManager.getInstance(this).setLastest_login_phone_num(phone);
            AppSharePrefManager.getInstance(this).setVersionName(AppUtil.getCurrentAppVersionName(this));
            //Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();

            new Thread() {
                @Override
                public void run() {
                    XmppUtil.getInstance().closeConnection();
                }
            }.start();

            Intent pushService = new Intent(PushService.ACTION);
            pushService.setPackage(getPackageName());
            stopService(pushService);

            //switchRun(AppSharePrefManager.getInstance(RePasswordActivity.this).getLastest_login_id(), "ready");

            Intent it = new Intent(RePasswordActivity.this, RePasswordSuccessActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }
    }

    private void initView() {

        edPhone = (EditText) findViewById(R.id.ed_phone);//电话号码
        edPhone.setText(AppSharePrefManager.getInstance(this).getLastest_login_phone_num());

        edAutocode = (EditText) findViewById(R.id.ed_autocode);//验证码
        butGetcode = (Button) findViewById(R.id.but_getcode);//获取验证码按钮
        edId = (EditText) findViewById(R.id.ed_id);//身份证
        edPassword = (EditText) findViewById(R.id.ed_password);//新密码
        edRepassword = (EditText) findViewById(R.id.ed_repassword);//重设的密码
        butRegister = (Button) findViewById(R.id.but_register);//提交按钮

        appProtocol = (TextView) findViewById(R.id.app_protocol);//看房去服务协议

        butGetcode.setOnClickListener(this);
        butRegister.setOnClickListener(this);

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
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_getcode://获取验证码
                phString = edPhone.getText().toString().trim();
                Log.e("czhongzhi", phString);
                if (TextUtils.isEmpty(phString)) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!HttpUtil.isNetworkAble(RePasswordActivity.this)) {
                    Toast.makeText(RePasswordActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开启验证码倒计时，在倒计时时不能点击按钮
                countDown();
                SMSSDK.getVerificationCode("86", phString);
                break;
            case R.id.but_register://立即加盟

                vcString = edAutocode.getText().toString().trim();
                if (TextUtils.isEmpty(vcString)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!HttpUtil.isNetworkAble(RePasswordActivity.this)) {
                    Toast.makeText(RePasswordActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }

                IID = edId.getText().toString().trim();
                if (TextUtils.isEmpty(IID)) {
                    Toast.makeText(this, "身份证号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                newpwdone = edPassword.getText().toString().trim();
                newpwdtwo = edRepassword.getText().toString().trim();
                if (TextUtils.isEmpty(newpwdone)) {
                    Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newpwdone.equals(newpwdtwo)) {
                    DialogUtil.showDealFailed(this, "设置密码与确认密码不一致", null);
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
                    butGetcode.setEnabled(true);
                    butGetcode.setText("验证");
                    time = RETRY_INTERVAL;
                } else {
                    String unReceive = "<font color='#8f8f90'>" + time + "</font>S";
                    butGetcode.setText(Html.fromHtml(unReceive));
                    butGetcode.setEnabled(false);
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
}
