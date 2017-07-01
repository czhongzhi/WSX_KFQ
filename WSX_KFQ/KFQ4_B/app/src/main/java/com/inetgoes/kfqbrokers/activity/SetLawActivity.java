package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

/**
 * 法律条款
 */
public class SetLawActivity extends Activity {
    private static Activity activity;
    private View item_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "法律条款", true, false);
        setContentView(R.layout.activity_set_law);

        activity = SetLawActivity.this;

        initView();
    }

    private void initView() {
        item_layout = findViewById(R.id.set_layout_law_1);//法律声明及隐私政策
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("法律声明及隐私政策");
            item_layout.findViewById(R.id.set_hitm).setVisibility(View.GONE);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        item_layout = findViewById(R.id.set_layout_law_2);//经济人合作协作
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("经济人合作协作");
            item_layout.findViewById(R.id.set_hitm).setVisibility(View.GONE);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        item_layout = findViewById(R.id.set_layout_law_3);//看房去使用条款
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("看房去使用条款");
            item_layout.findViewById(R.id.set_hitm).setVisibility(View.GONE);
            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
