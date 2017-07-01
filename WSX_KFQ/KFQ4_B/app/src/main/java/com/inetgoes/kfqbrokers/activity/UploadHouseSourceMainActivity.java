package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

public class UploadHouseSourceMainActivity extends Activity {

    private Button newhouseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "上传房源", true, false);

        setContentView(R.layout.activity_upload_housesource_one);


        newhouseBtn = (Button) findViewById(R.id.newhouse_btn);

        newhouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UploadHouseSourceMainActivity.this, UploadHouseSourceTwoActivity.class);
                startActivity(intent);
            }
        });
    }
}
