package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MyCircleImageView;

/**
 * 我的资产
 */
public class MyAssetActivity extends Activity implements View.OnClickListener{

    private MyCircleImageView asset_icon;
    private TextView asset_name;
    private TextView asset_num;     //总资产
    private TextView asset_succ;    //成交量
    private TextView asset_visit;   //到访量
    private TextView asset_yuyue;   //预约量

    private View item_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "我的资产", true, false);
        setContentView(R.layout.activity_my_asset_main);

        initView();
    }

    private void initView() {
        asset_icon = (MyCircleImageView) findViewById(R.id.asset_icon);
        asset_name = (TextView) findViewById(R.id.asset_name);
        asset_num = (TextView) findViewById(R.id.asset_num);
        asset_succ = (TextView) findViewById(R.id.asset_succ);
        asset_visit = (TextView) findViewById(R.id.asset_visit);
        asset_yuyue = (TextView) findViewById(R.id.asset_yuyue);

        //历史记录
        item_layout = findViewById(R.id.asset_history);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("历史记录");
            item_layout.setOnClickListener(this);
        }

        //佣金明细
        item_layout = findViewById(R.id.asset_detail);
        if (item_layout != null) {
            ((TextView) item_layout.findViewById(R.id.set_text)).setText("佣金明细");
            item_layout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.asset_history://历史记录
                startActivity(new Intent(this,MyAssetHistoryActivity.class));
                break;

            case R.id.asset_detail://佣金明细
                startActivity(new Intent(this,MyAssetDetailActivity.class));
                break;
        }
    }
}
