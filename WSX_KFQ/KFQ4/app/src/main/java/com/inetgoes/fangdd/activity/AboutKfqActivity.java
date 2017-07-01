package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.os.Bundle;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.view.CustomTitleBar;

public class AboutKfqActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this,"关于看房去",true,false);
        setContentView(R.layout.activity_about_kfq);
    }


}
