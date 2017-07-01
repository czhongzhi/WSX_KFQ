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
public class SetLaw2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "使用条款和隐私政策", true, false);
        setContentView(R.layout.activity_set_law2);

    }

}
