package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.view.CustomTitleBar;


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
        if (TextUtils.isEmpty(title)) {
            CustomTitleBar.getTitleBar(this, "房产专家加盟", true, false);

        } else if ("anew".equals(title)) {
            CustomTitleBar.getTitleBar(this, "重设审核资料", true, false);
            hintString = "审核资料已提交";
        }
        setContentView(R.layout.activity_regist_success);

        ((TextView) findViewById(R.id.dialog_title)).setText(hintString);

        findViewById(R.id.join_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistSuccessActivity.this, "下载房砖", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
