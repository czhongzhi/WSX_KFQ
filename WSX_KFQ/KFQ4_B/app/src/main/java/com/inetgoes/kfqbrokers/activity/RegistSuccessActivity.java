package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

/**
 * 注册成功界面
 */
public class RegistSuccessActivity extends Activity {
    private String title;
    private String hintString = "您已成功注册";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");
        if(TextUtils.isEmpty(title)){
            CustomTitleBar.getTitleBar(this, "房产专家加盟", true, false);

        }else if("anew".equals(title)){
            CustomTitleBar.getTitleBar(this, "重设审核资料", true, false);
            hintString = "审核资料已提交";
        }
        setContentView(R.layout.activity_regist_success);

        ((TextView)findViewById(R.id.dialog_title)).setText(hintString);
    }

}
