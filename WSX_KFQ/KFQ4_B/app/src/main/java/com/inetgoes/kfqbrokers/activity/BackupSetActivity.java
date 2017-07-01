package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.BackupInfo;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import java.util.HashMap;
import java.util.Map;

/**
 * 编辑客户资料
 */
public class BackupSetActivity extends Activity {
    public static final String BACKUPINFO = "backupinfo";
    public static final String CUSFROMTYPE = "fromtype";

    private BackupInfo backupInfo;
    private int userid;   //经纪人id
    private int custid;   //对方用户id
    private String fromtype;  //用于区分客户来源

    private EditText ed_name, ed_phone, ed_intent_area, ed_intent_house, ed_intent_huxing, ed_intent_prop, ed_info_backup;
    private RadioGroup rg_sex;
    private Button btn_save;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "编辑客户资料", true, false);
        setContentView(R.layout.activity_backup_set);

        backupInfo = (BackupInfo) getIntent().getSerializableExtra(BACKUPINFO);

        custid = getIntent().getIntExtra(BackupActivity.CUSTID, 0);
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
        fromtype = getIntent().getStringExtra(CUSFROMTYPE);

        initView();

        initData();
    }

    private void initView() {
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_intent_area = (EditText) findViewById(R.id.ed_intent_area);
        ed_intent_house = (EditText) findViewById(R.id.ed_intent_house);
        ed_intent_huxing = (EditText) findViewById(R.id.ed_intent_huxing);
        ed_intent_prop = (EditText) findViewById(R.id.ed_intent_prop);
        ed_info_backup = (EditText) findViewById(R.id.ed_info_backup);

        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_man:
                        backupInfo.setSex("男");
                        ((RadioButton) rg_sex.getChildAt(0)).setChecked(true);
                        break;
                    case R.id.rg_woman:
                        backupInfo.setSex("女");
                        ((RadioButton) rg_sex.getChildAt(1)).setChecked(true);
                        break;
                }
            }
        });

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存
                getBackinfo();
                save();
            }
        });
    }

    private void initData() {
        ed_name.setText(changeNull(backupInfo.getCustname()));
        ed_phone.setText(changeNull(backupInfo.getCustphone()));
        ed_intent_area.setText(changeNull(backupInfo.getIntent_area()));
        ed_intent_house.setText(changeNull(backupInfo.getIntent_fangname()));
        ed_intent_huxing.setText(changeNull(backupInfo.getIntent_huxing()));
        ed_intent_prop.setText(changeNull(backupInfo.getIntent_size()));
        ed_info_backup.setText(changeNull(backupInfo.getOthermark()));

        if (!TextUtils.isEmpty(backupInfo.getSex())) {
            String sex = backupInfo.getSex();
            if ("男".equals(sex)) {
                ((RadioButton) rg_sex.getChildAt(0)).setChecked(true);
            } else if ("女".equals(sex)) {
                ((RadioButton) rg_sex.getChildAt(1)).setChecked(true);
            }
        }
    }


    private String changeNull(String str) {
        return str == null ? "" : str;
    }

    private void getBackinfo() {
        backupInfo.setCustname(getEditItem(ed_name));
        backupInfo.setCustphone(getEditItem(ed_phone));
        backupInfo.setIntent_area(getEditItem(ed_intent_area));
        backupInfo.setIntent_fangname(getEditItem(ed_intent_house));
        backupInfo.setIntent_huxing(getEditItem(ed_intent_huxing));
        backupInfo.setIntent_size(getEditItem(ed_intent_prop));
        backupInfo.setOthermark(getEditItem(ed_info_backup));
    }

    private String getEditItem(EditText ed) {
        return ed.getText().toString().trim();
    }

    private void save() {

        dialog = DialogUtil.showWait(this);
        dialog.setCancelable(true);

        Map<String, Object> map = new HashMap<>();
        map.put("markid",backupInfo.getMarkid());
        map.put("brokerid", userid);
        map.put("custid", custid);
        map.put("custname", backupInfo.getCustname());
        map.put("custphone", backupInfo.getCustphone());
        map.put("sex", backupInfo.getSex());
        map.put("intent_area", backupInfo.getIntent_area());
        map.put("intent_fangname", backupInfo.getIntent_fangname());
        map.put("intent_huxing", backupInfo.getIntent_huxing());
        map.put("intent_size", backupInfo.getIntent_size());
        map.put("othermark", backupInfo.getOthermark());

        map.put("fromtype",fromtype);           //客户类型          (必写) //用于区分客户来源

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogI("BackupSet save is " + result);

                dialog.dismiss();

                if(TextUtils.isEmpty(result)){
                    return;
                }
                Map<String,Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if((boolean)resMap.get("state")){
                    Toast.makeText(BackupSetActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    BackupSetActivity.this.setResult(666);
                    BackupSetActivity.this.finish();
                }else{
                    L.LogI("保存失败 -- ");
                    Toast.makeText(BackupSetActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.setBackupInfoUrl,map);
    }


}
