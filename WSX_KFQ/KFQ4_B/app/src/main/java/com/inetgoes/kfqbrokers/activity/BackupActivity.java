package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.BackupInfo;
import com.inetgoes.kfqbrokers.model.CustMarkKanfangOrder;
import com.inetgoes.kfqbrokers.model.KanFangListStateResp;
import com.inetgoes.kfqbrokers.utils.AloneDeal;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MyListView;
import com.inetgoes.kfqbrokers.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户资料
 */
public class BackupActivity extends Activity implements RightDeal {
    public static final String CUSTID = "custid";
    public static final String CUSTICON = "custicon";
    public static final String CUSTNAME = "custname";
    public static final String CUSFROMTYPE = "fromtype";

    private int userid;   //经纪人id
    private int custid;   //对方用户id
    private String custicon;
    private String custname;
    private String fromtype;  //来源:值:normal表示正式用客户基本信息,baobei表示是微信前端经纪人报备的

    private BackupInfo backupInfo;

    private ImageView client_icon;  //客户头像
    private TextView client_name;  //客户姓名
    private TextView client_phone;  //客户电话
    private ImageView client_sex;   //客户性别

    private View intent_edit, intent_area, intent_house, intent_huxing, intent_prop, layout_infobackup,intent_fromtype;
    private TextView infobackup;

    private MyListView listView;
    private SeeHouseAdapter adapter;
    private List<CustMarkKanfangOrder> orders = new ArrayList<>();

    private LinearLayout yituijianloupan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar_Backup(this, "详细资料", this);
        setContentView(R.layout.activity_backup);

        custid = getIntent().getIntExtra(CUSTID, 0);
        custicon = getIntent().getStringExtra(CUSTICON);
        custname = getIntent().getStringExtra(CUSTNAME);
        fromtype = getIntent().getStringExtra(CUSFROMTYPE);
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        initView();

        reqData(userid, custid, fromtype, custname);
    }

    private void initView() {
        client_icon = (ImageView) findViewById(R.id.client_icon);
        client_name = (TextView) findViewById(R.id.client_name);
        client_phone = (TextView) findViewById(R.id.client_phone);
        client_sex = (ImageView) findViewById(R.id.client_sex);

        if (!TextUtils.isEmpty(custicon)) {
            ImageLoader.getInstance().displayImage(custicon, client_icon, FangApplication.options, FangApplication.animateFirstListener);
        }
        client_name.setText(custname);

        intent_edit = findViewById(R.id.intent_edit);
        intent_area = findViewById(R.id.intent_area);
        intent_house = findViewById(R.id.intent_house);
        intent_huxing = findViewById(R.id.intent_huxing);
        intent_prop = findViewById(R.id.intent_prop);
        layout_infobackup = findViewById(R.id.layout_infobackup);
        intent_fromtype = findViewById(R.id.intent_fromtype);

        infobackup = (TextView) findViewById(R.id.info_backup);

        intent_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BackupActivity.this, BackupSetActivity.class);
                intent.putExtra(BackupSetActivity.BACKUPINFO, backupInfo);
                intent.putExtra(BackupSetActivity.CUSFROMTYPE, fromtype);
                intent.putExtra(BackupActivity.CUSTID, custid);
                startActivityForResult(intent, 411);
            }
        });

        yituijianloupan = (LinearLayout) findViewById(R.id.yituijianloupan);
        yituijianloupan.setVisibility(View.GONE);

        listView = (MyListView) findViewById(R.id.listView);
        adapter = new SeeHouseAdapter(this, orders);
        listView.setAdapter(adapter);

    }

    private void setData(BackupInfo backupInfo) {
        if (backupInfo == null) {
            return;
        }
        if (!backupInfo.isState()) {
            RelativeLayout textl = (RelativeLayout) intent_edit.findViewById(R.id.layout_item);
            textl.setVisibility(View.VISIBLE);
            ((TextView) intent_edit.findViewById(R.id.item_title)).setText("编辑客户资料");
            (intent_edit.findViewById(R.id.item_content)).setVisibility(View.INVISIBLE);

            backupInfo.setCustname(custname);
            return;
        } else {
            (intent_edit.findViewById(R.id.layout_item)).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(backupInfo.getCustphone())) {
            client_phone.setText(backupInfo.getCustphone());
        }
        if (!TextUtils.isEmpty(backupInfo.getCustname())) {
            client_name.setText(backupInfo.getCustname());
        }
        if (!TextUtils.isEmpty(backupInfo.getSex())) {
            client_sex.setVisibility(View.VISIBLE);
            String sex = backupInfo.getSex();
            if ("男".equals(sex)) {
                client_sex.setImageResource(R.drawable.boy);
            } else if ("女".equals(sex)) {
                client_sex.setImageResource(R.drawable.girl);
            }
        }

        setIntentView(intent_area, "意向区域", backupInfo.getIntent_area());

        setIntentView(intent_house, "意向房源", backupInfo.getIntent_fangname());

        setIntentView(intent_huxing, "意向户型", backupInfo.getIntent_huxing());

        setIntentView(intent_prop, "意向面积", backupInfo.getIntent_size());

        if("baobei".equals(fromtype)){
            setIntentView(intent_fromtype,"来源","通过报备添加");
        }else if ("normal".equals(fromtype)){
            setIntentView(intent_fromtype,"来源","通过发起预约添加");
        }


        if (!TextUtils.isEmpty(backupInfo.getOthermark())) {
            layout_infobackup.setVisibility(View.VISIBLE);
            infobackup.setText("描述：" + backupInfo.getOthermark());
        }

        if (backupInfo.getOrders().size() != 0) {
            yituijianloupan.setVisibility(View.VISIBLE);
            orders.clear();
            orders.addAll(backupInfo.getOrders());
            adapter.notifyDataSetChanged();
        }
    }

    private void setIntentView(View view, String title, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        view.findViewById(R.id.layout_item).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.item_title)).setText(title);
        ((TextView) view.findViewById(R.id.item_content)).setText(content);
    }

    /**
     * @param brokerid 经纪人id
     * @param custid   对方用户id
     */
    private void reqData(final int brokerid, int custid, String fromtype, String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("brokerid", brokerid);
        if ("baobei".equals(fromtype)) {
            map.put("custid", 0);
        } else {
            map.put("custid", custid);
        }
        map.put("fromtype", fromtype);
        map.put("username", username);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogI("barkup readata is " + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                try {
                    backupInfo = JacksonMapper.getObjectMapper().readValue(result, BackupInfo.class);

                    setData(backupInfo);

                    L.LogI("barkup readata is " + backupInfo.getMarkid());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.backupInfoUrl, map);

    }


    @Override
    public void deal() {
        Intent intent = new Intent(BackupActivity.this, BackupSetActivity.class);
        intent.putExtra(BackupSetActivity.BACKUPINFO, backupInfo);
        intent.putExtra(BackupActivity.CUSTID, custid);
        intent.putExtra(BackupSetActivity.CUSFROMTYPE, fromtype);
        startActivityForResult(intent, 411);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 411 && resultCode == 666) {
            reqData(userid, custid, fromtype, custname);
        }
    }

    private class SeeHouseAdapter extends BaseAdapter {
        private List<CustMarkKanfangOrder> list;
        private LayoutInflater inflater;
        private Context context;

        private SeeHouseAdapter(Context context, List<CustMarkKanfangOrder> list) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
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
            ViewHolper vh = null;
            if (convertView == null) {
                vh = new ViewHolper();
                convertView = inflater.inflate(R.layout.item_seehouse, parent, false);
                vh.broker_icon = (ImageView) convertView.findViewById(R.id.broker_icon);
                vh.broker_name = (TextView) convertView.findViewById(R.id.broker_name);
//                vh.broker_star = (RatingBar) convertView.findViewById(R.id.broker_star);
//                vh.broker_score = (TextView) convertView.findViewById(R.id.broker_score);
                vh.trans_status = (TextView) convertView.findViewById(R.id.trans_status);
                vh.loupen_icon = (ImageView) convertView.findViewById(R.id.loupen_icon);
                vh.loupen_title = (TextView) convertView.findViewById(R.id.loupen_title);
                vh.loupen_price = (TextView) convertView.findViewById(R.id.loupen_price);
                vh.text = (TextView) convertView.findViewById(R.id.text);
                vh.time = (TextView) convertView.findViewById(R.id.time);
                vh.dingdandeal = (TextView) convertView.findViewById(R.id.dingdandeal);
                vh.lookhouse = (TextView) convertView.findViewById(R.id.lookhouse);
                vh.lookplan = (TextView) convertView.findViewById(R.id.lookplan);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            final CustMarkKanfangOrder kf = list.get(position);
            if (!TextUtils.isEmpty(custicon)) {
                ImageLoader.getInstance().displayImage(custicon, vh.broker_icon, FangApplication.options, FangApplication.animateFirstListener);
            }
            vh.broker_name.setText(custname);
//            vh.broker_star.setRating(kf.getStarlevel());
//            vh.broker_score.setText(String.valueOf(kf.getStarlevel()));
            if (TextUtils.isEmpty(kf.getKanfang_state())) {
                vh.trans_status.setText("");
                vh.text.setText("");
            } else {
                vh.trans_status.setText(kf.getKanfang_state() + " ");
                vh.text.setText(kf.getHuxing_type() + "、" + kf.getHuxing_size());
            }

            if (!TextUtils.isEmpty(kf.getLoupan_image_url())) {
                ImageLoader.getInstance().displayImage(kf.getLoupan_image_url(), vh.loupen_icon, FangApplication.options, FangApplication.animateFirstListener);
            }
            vh.loupen_title.setText(kf.getLoupanname());
            vh.loupen_price.setText(kf.getPricedesc());
            vh.lookplan.setVisibility(View.VISIBLE);
            vh.dingdandeal.setVisibility(View.VISIBLE);
            vh.lookhouse.setVisibility(View.VISIBLE);

            vh.time.setText(kf.getCreatedate_str());

            String state = kf.getKanfang_state();
            vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_red));
            if ("待看房".equals(state)) {
                vh.dingdandeal.setText("申请取消");
                vh.lookhouse.setText("确认看房");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getCancelKanfang());
                vh.lookhouse.setOnClickListener(c.getConfirmKanfang());
                vh.lookplan.setOnClickListener(c.getLookplan());

            } else if ("已看房".equals(state)) {
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());
                vh.lookplan.setOnClickListener(c.getLookplan());

            } else if ("已取消".equals(state)) {
                vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");
                vh.lookplan.setVisibility(View.GONE);

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());

            } else if ("已失败".equals(state)) {
                vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");
                vh.lookplan.setVisibility(View.GONE);

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());

            } else {
//                vh.dingdandeal.setText("XXXX");
//                vh.lookhouse.setText("CCCC");
                vh.lookhouse.setText("查看进度");
                vh.lookhouse.setVisibility(View.VISIBLE);
                vh.dingdandeal.setVisibility(View.GONE);
                vh.lookplan.setVisibility(View.GONE);

                Click c = new Click(kf, adapter, list);
                vh.lookhouse.setOnClickListener(c.getLookplan());

            }

            return convertView;
        }

        private class ViewHolper {
            private ImageView broker_icon;
            private TextView broker_name;
//            private RatingBar broker_star;
//            private TextView broker_score;

            private TextView trans_status;

            private ImageView loupen_icon;
            private TextView loupen_title;
            private TextView loupen_price;
            private TextView text;
            private TextView time;

            private TextView dingdandeal;
            private TextView lookhouse;
            private TextView lookplan;

        }

        private class Click {
            private CustMarkKanfangOrder kf;
            private SeeHouseAdapter adapter;
            private List<CustMarkKanfangOrder> list;

            public Click(CustMarkKanfangOrder kf, SeeHouseAdapter adapter, List<CustMarkKanfangOrder> list) {
                this.kf = kf;
                this.adapter = adapter;
                this.list = list;
            }

            private View.OnClickListener deleceKnafang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog waitDialog = DialogUtil.showWait(BackupActivity.this);
                    new HttpAsy(new PostExecute() {
                        @Override
                        public void onPostExecute(String result) {
                            waitDialog.dismiss();
                            if (!TextUtils.isEmpty(result)) {
                                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                                if (state.equals("true")) {//删除成功
                                    DialogUtil.showDealSuccess(BackupActivity.this, "删除成功", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(kf);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                } else {//删除失败
                                    DialogUtil.showDealFailed(BackupActivity.this, "删除失败", null);
                                }
                            }
                        }
                    }).execute(Constants.deleteKanfangDanUrl + "?recid=" + kf.getKan_recid() + "&roletype=broker");
                }
            };

            private View.OnClickListener cancelKanfang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showDeleteYuyueDeal(BackupActivity.this, "确认取消看房", new AloneDeal() {
                        @Override
                        public void deal() {
                            final Dialog waitDialog = DialogUtil.showWait(BackupActivity.this);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("recid", kf.getKan_recid());
                            map.put("opertype", "cancel");
                            new HttpAsy(new PostExecute() {
                                @Override
                                public void onPostExecute(String result) {
                                    Log.e("czhongzhi", "confirmkanfang result is " + result);
                                    waitDialog.dismiss();
                                    if (!TextUtils.isEmpty(result)) {
                                        Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                                        if ((boolean) resMap.get("state")) {
                                            //reqData(0, list.size(), false,true);
                                        } else {
                                            Toast.makeText(BackupActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }).execute(Constants.cancelOrConfirmKFUrl, map);
                        }
                    }, null);
                }
            };

            private View.OnClickListener confirmKanfang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogUtil.showDeleteYuyueDeal(BackupActivity.this, "是否确认看房", new AloneDeal() {
                        @Override
                        public void deal() {
                            final Dialog waitDialog = DialogUtil.showWait(BackupActivity.this);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("recid", kf.getKan_recid());
                            map.put("opertype", "confirm");
                            new HttpAsy(new PostExecute() {
                                @Override
                                public void onPostExecute(String result) {
                                    Log.e("czhongzhi", "confirmkanfang result is " + result);
                                    waitDialog.dismiss();
                                    if (!TextUtils.isEmpty(result)) {
                                        Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                                        if ((boolean) resMap.get("state")) {
                                            //reqData(0, list.size(), false,true);
                                        } else {
                                            Toast.makeText(BackupActivity.this, "确认失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }).execute(Constants.cancelOrConfirmKFUrl, map);
                        }
                    }, null);
                }
            };

            private View.OnClickListener complainKanfan = new View.OnClickListener() {//投诉
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BackupActivity.this, BrokerComplainActivity.class);
                    intent.putExtra(BrokerComplainActivity.RECID, String.valueOf(kf.getKan_recid()));
                    intent.putExtra(BrokerComplainActivity.OPERDEST, "kanfang");
                    startActivity(intent);
                }
            };

            private View.OnClickListener lookplan = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BackupActivity.this, SeePlanActivity.class);
                    intent.putExtra(SeePlanActivity.FROMTYPE, fromtype);
                    intent.putExtra(SeePlanActivity.KANRECID, kf.getKan_recid());
                    intent.putExtra(SeePlanActivity.TITLE,kf.getLoupanname());
                    startActivity(intent);
                }
            };

            public View.OnClickListener getDeleceKnafang() {
                return deleceKnafang;
            }

            public void setDeleceKnafang(View.OnClickListener deleceKnafang) {
                this.deleceKnafang = deleceKnafang;
            }

            public View.OnClickListener getCancelKanfang() {
                return cancelKanfang;
            }

            public void setCancelKanfang(View.OnClickListener cancelKanfang) {
                this.cancelKanfang = cancelKanfang;
            }

            public View.OnClickListener getConfirmKanfang() {
                return confirmKanfang;
            }

            public void setConfirmKanfang(View.OnClickListener confirmKanfang) {
                this.confirmKanfang = confirmKanfang;
            }

            public View.OnClickListener getComplainKanfan() {
                return complainKanfan;
            }

            public void setComplainKanfan(View.OnClickListener complainKanfan) {
                this.complainKanfan = complainKanfan;
            }

            public View.OnClickListener getLookplan() {
                return lookplan;
            }

            ;

            public void setLookplan(View.OnClickListener lookplan) {
                this.lookplan = lookplan;
            }
        }
    }
}
