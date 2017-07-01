package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;

import java.util.HashMap;
import java.util.Map;

public class SetUserInfo2Activity extends Activity {
    public static final String TITLE_BAR = "title_bar";
    public static final String EditHint = "edithint";
    private String titlebar;
    private String hint;
    private String type;  //类型
    private String parm;  //参数
    private EditText set_content;
    private Button set_ok;

    private Dialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra(TITLE_BAR);
        if (type.equals(SetUserInfoActivity.SET_NAME)) {
            titlebar = "用户名";
            parm = "name";
        } else if (type.equals(SetUserInfoActivity.SET_EMAIL)) {
            titlebar = "电子邮箱";
            parm = "email";
        } else if (type.equals(SetUserInfoActivity.SET_PHONE)) {
            titlebar = "手机号码";
            parm = "cellphone";
        }
        CustomTitleBar.getTitleBar(this, titlebar, true, false);
        setContentView(R.layout.activity_set_user_info2);

        hint = getIntent().getStringExtra(EditHint);

        initView();
    }

    private void initView() {
        set_content = (EditText) findViewById(R.id.set_content);
        set_ok = (Button) findViewById(R.id.set_ok);

        if (TextUtils.isEmpty(hint))
            set_content.setHint("请输入" + titlebar);
        else
            set_content.setHint(hint);

        set_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = set_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(SetUserInfo2Activity.this, "修改内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //提示修改信息操作
                setUserInfo(parm, content);
            }
        });
    }

    private void setUserInfo(String parm, final String set) {
        int userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
        if (userid == 0)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put(parm, set);
        waitDialog = DialogUtil.showWait(this);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(SetUserInfo2Activity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                    if (state.equals("true")) {//修改成功
                        setSingleLocalInfo(set);
                        startActivity(new Intent(SetUserInfo2Activity.this, SetUserInfoActivity.class));
                        SetUserInfo2Activity.this.finish();
                    } else {//修改失败
                        Toast.makeText(SetUserInfo2Activity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                }
            }
        }).execute(Constants.setUserInfoUrl, map);
    }

    private void setSingleLocalInfo(String set) {
        AppSharePrefManager sManager = AppSharePrefManager.getInstance(this);
        if (type.equals(SetUserInfoActivity.SET_NAME)) {
            sManager.setLastest_login_username(set);//
        } else if (type.equals(SetUserInfoActivity.SET_EMAIL)) {
            sManager.setLastest_email(set);//
        } else if (type.equals(SetUserInfoActivity.SET_PHONE)) {
            sManager.setLastest_login_phone_num(set);//
        }
    }
}
