package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.util.L;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.inetgoes.fangdd.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投诉界面
 */
public class BrokerComplainActivity extends Activity {
    public static final String RECID = "recid";
    public static final String OPERDEST = "operdest";

    private String recid;
    private String operdest;

    private MyListView listView;
    private EditText ed_info_backup;
    private Button btn_save;

    private List<Reason> reasons = new ArrayList<>();
    private ReasonAdapter adapter;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "投诉", true, false);
        setContentView(R.layout.activity_broker_complain);

        recid = getIntent().getStringExtra(RECID);
        operdest = getIntent().getStringExtra(OPERDEST);

        initData();

        initView();

    }

    private void initData() {
        reasons.add(new Reason("职业技能不够专业", false));
        reasons.add(new Reason("服务态度不好", false));
        reasons.add(new Reason("房源是假房源", false));
        reasons.add(new Reason("服务过程中欺骗顾客", false));
    }

    private void initView() {

        listView = (MyListView) findViewById(R.id.listView);
        ed_info_backup = (EditText) findViewById(R.id.ed_info_backup);
        btn_save = (Button) findViewById(R.id.btn_save);

        adapter = new ReasonAdapter(this, reasons);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean fiag = reasons.get(position).isSelect();
                reasons.get(position).setSelect(!fiag);
                adapter.notifyDataSetInvalidated();
            }
        });

        btn_save.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //getReason();

            String complaindesc = getReason();

            L.LogE("res is " + recid + " -- " + complaindesc);

            if (TextUtils.isEmpty(complaindesc)) {
                Toast.makeText(BrokerComplainActivity.this, "请选择投诉原因", Toast.LENGTH_SHORT).show();
            } else {
                save(recid, complaindesc, operdest, "user");
            }

        }
    };

    /**
     * @param recid        如果operdest=tran时，传tranid,否则传看房订单表id
     * @param complaindesc 投诉描述
     * @param operdest     值有: tran 表示预约, kanfang 表示看房订单
     * @param roletype     值有: user,broker,owner分别表示:用户,经纪人,业主
     */
    private void save(String recid, String complaindesc, String operdest, String roletype) {

        dialog = DialogUtil.showWait(this);
        dialog.setCancelable(true);

        Map<String, Object> map = new HashMap<>();
        map.put("recid", recid);
        map.put("complaindesc", complaindesc);
        map.put("operdest", operdest);
        map.put("roletype", roletype);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogI("BackupSet save is " + result);

                dialog.dismiss();

                if (TextUtils.isEmpty(result)) {
                    return;
                }
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if ((boolean) resMap.get("state")) {
                    Toast.makeText(BrokerComplainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    BrokerComplainActivity.this.finish();
                } else {
                    L.LogI("保存失败 -- ");
                    Toast.makeText(BrokerComplainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.complainBrokerUrl, map);
    }

    private String getReason() {
        String res = null;
        StringBuffer sb = new StringBuffer();
        int l = reasons.size();
        for (int i = 0; i < l; i++) {
            if (reasons.get(i).isSelect()) {
                sb.append(reasons.get(i).getRes());
                sb.append(";");
            }
        }
        if (sb.length() != 0) {
            res = sb.substring(0, sb.length() - 1);
        }

        String ed_tx = ed_info_backup.getText().toString().trim();

        if (res == null) {
            return ed_tx;
        }

        if (!TextUtils.isEmpty(ed_tx)) {
            res = res + ";" + ed_tx;
        }

        return res;
    }

    private class ReasonAdapter extends BaseAdapter {
        private Context context;
        private List<Reason> list;
        private LayoutInflater inflater;

        public ReasonAdapter(Context context, List<Reason> list) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHopler vh = null;
            if (convertView == null) {
                vh = new ViewHopler();
                convertView = this.inflater.inflate(R.layout.item_complain_much_select, parent, false);
                vh.item_icon = (ImageView) convertView.findViewById(R.id.item_icon);
                vh.item_title = (TextView) convertView.findViewById(R.id.item_title);

                convertView.setTag(vh);
            } else {
                vh = (ViewHopler) convertView.getTag();
            }

            vh.item_title.setText(list.get(position).getRes());
            if (list.get(position).isSelect()) {
                vh.item_icon.setImageResource(R.drawable.complaint_light);
            } else {
                vh.item_icon.setImageResource(R.drawable.complaint_gray);
            }

            return convertView;
        }

        private class ViewHopler {
            private TextView item_title;
            private ImageView item_icon;
        }
    }

    public class Reason {

        private String res;
        private boolean Select;

        public Reason(String res, boolean Select) {
            this.res = res;
            this.Select = Select;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public boolean isSelect() {
            return Select;
        }

        public void setSelect(boolean select) {
            Select = select;
        }
    }

}
