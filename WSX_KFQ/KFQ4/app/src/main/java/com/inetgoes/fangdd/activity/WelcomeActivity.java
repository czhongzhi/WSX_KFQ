package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.util.AppUtil;

/**
 * 欢迎页
 * Created by czz on 2015/10/28.
 */
public class WelcomeActivity extends Activity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        isEnterGuide();
    }

    /**
     * 是否进入引导页，否则进入主页
     */
    private synchronized void isEnterGuide() {
        if (mHandler == null)
            return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUtil.isFirstInApp(WelcomeActivity.this)) {//是否是第一次使用应用或第一次使用更新的
                    //开启引导页
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    AppSharePrefManager.getInstance(WelcomeActivity.this).setVersionName(AppUtil.getCurrentAppVersionName(WelcomeActivity.this));
                } else {
                    if(AppSharePrefManager.getInstance(WelcomeActivity.this).isLogined()){
                        //开启主页
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    }else{
                        //开启登录
                        startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                    }
                }

                //startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));

                //关闭当前欢迎页
                WelcomeActivity.this.finish();
            }
        }, 2000L);
    }
}
