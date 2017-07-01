package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.utils.AppUtil;


/**
 * 欢迎页
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
                if (AppUtil.isFirstInApp(WelcomeActivity.this)) {
                    //开启引导页
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                    AppSharePrefManager.getInstance(WelcomeActivity.this).setVersionName(AppUtil.getCurrentAppVersionName(WelcomeActivity.this));
                } else {
                    if (AppSharePrefManager.getInstance(WelcomeActivity.this).isLogined()) {
                        // 开启主页
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    } else {
                        // 开启登录页
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    }

                }

               // startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));

                //关闭当前欢迎页
                WelcomeActivity.this.finish();
            }
        }, 2000L);
    }
}
