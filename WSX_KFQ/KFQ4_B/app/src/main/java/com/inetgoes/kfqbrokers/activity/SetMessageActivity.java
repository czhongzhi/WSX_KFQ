package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.utils.SoundPoolUtil;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.SlideSwitch;


public class SetMessageActivity extends Activity {
    private View item_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "消息设置", true, false);
        setContentView(R.layout.activity_set_message);

        item_layout = findViewById(R.id.set_mess);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("消息开关");
            SlideSwitch slideSwitch = (SlideSwitch) item_layout.findViewById(R.id.set_switch);
            slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
                @Override
                public void open() {
                    Log.e("czhongzhi", "open");
                }

                @Override
                public void close() {
                    Log.e("czhongzhi", "close");
                }
            });
        }

        item_layout = findViewById(R.id.set_ring);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("铃声提醒");
            SlideSwitch slideSwitch = (SlideSwitch) item_layout.findViewById(R.id.set_switch);
            slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
                @Override
                public void open() {
                    SoundPoolUtil.getInstance().play(SetMessageActivity.this,R.raw.hint);

                    Log.e("czhongzhi", "open");
                    //boolean常量flase
                }

                @Override
                public void close() {
                    //boolean常量true

                    Log.e("czhongzhi", "close");
                }
            });
        }

        item_layout = findViewById(R.id.set_shake);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("震动提醒");
            SlideSwitch slideSwitch = (SlideSwitch) item_layout.findViewById(R.id.set_switch);
            slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {

                private Vibrator vibrator;

                @Override
                public void open() {
                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                    vibrator.vibrate(pattern, -1);

                    Log.e("czhongzhi", "open");
                }

                @Override
                public void close() {
                    if (null != vibrator)
                        vibrator.cancel();

                    Log.e("czhongzhi", "close");
                }
            });
        }
    }


}
