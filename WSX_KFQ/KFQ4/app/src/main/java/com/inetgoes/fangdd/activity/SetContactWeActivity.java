package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.view.CustomTitleBar;

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
