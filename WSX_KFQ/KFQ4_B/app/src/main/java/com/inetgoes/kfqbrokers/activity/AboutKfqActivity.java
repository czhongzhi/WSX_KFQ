package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

public class AboutKfqActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "关于看房去", true, false);
        setContentView(R.layout.activity_about_kfq);
    }

}
