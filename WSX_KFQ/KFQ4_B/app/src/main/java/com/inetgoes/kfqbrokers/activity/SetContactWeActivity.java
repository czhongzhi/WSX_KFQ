package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.os.Bundle;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;


/**
 * 联系我们
 */
public class SetContactWeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "联系我们", true, false);
        setContentView(R.layout.activity_set_contact_we);
    }


}
