package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

/**
 * 添加客户
 */
public class AddClientActivity extends Activity implements View.OnClickListener{

    private EditText ed_name;
    private EditText ed_phone;
    private Button but_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "添加客户", true, false);
        setContentView(R.layout.activity_add_client);

        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        but_add = (Button) findViewById(R.id.but_add);
        but_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_add:  //添加到我的客户

                break;
        }
    }
}
