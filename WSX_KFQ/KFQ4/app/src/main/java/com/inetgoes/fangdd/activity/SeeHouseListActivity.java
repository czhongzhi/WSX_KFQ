package com.inetgoes.fangdd.activity;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.model.BrokerComm;
import com.inetgoes.fangdd.model.KanFangListStateResp;
import com.inetgoes.fangdd.util.AloneDeal;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 看房订单的Activity
 */
public class SeeHouseListActivity extends Activity {
    private PullToRefreshListView listView;
    private SeeHouseAdapter adapter;
    private List<KanFangListStateResp> seehouses = new ArrayList<>();

    private int startindex = 0;
    private int pagenum = 8;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "看房订单", true, false);
        setContentView(R.layout.activity_see_house_list);

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        listView = (PullToRefreshListView) findViewById(R.id.listView);

        View none_content = LayoutInflater.from(this).inflate(R.layout.none_content_hint, null);
        ((ImageView) none_content.findViewById(R.id.none_icon)).setImageResource(R.drawable.order1);
        ((TextView) none_content.findViewById(R.id.none_text)).setText("暂无看房订单");
        listView.setEmptyView(none_content);


        adapter = new SeeHouseAdapter(this, seehouses);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeeHouseListActivity.this, BuildingDanActivity.class);
                intent.putExtra(BuildingMainActivity.NEWCODE, seehouses.get(position - 1).getNewcode());
                intent.putExtra(BuildingMainActivity.TITLEBAR, seehouses.get(position - 1).getLoupanname());
                intent.putExtra(BuildingDanActivity.KanReqId, seehouses.get(position - 1).getKanrecid());
                intent.putExtra(BuildingDanActivity.But_Left, (((TextView) view.findViewById(R.id.dingdandeal)).getText().toString().trim()));
                intent.putExtra(BuildingDanActivity.But_Right, (((TextView) view.findViewById(R.id.lookhouse)).getText().toString().trim()));
                intent.putExtra("huxingtype", seehouses.get(position - 1).getHuxing_type());
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex += pagenum;
                reqData(startindex, pagenum, true);
            }
        });

        reqData(startindex, pagenum, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        seehouses.clear();
        reqData(startindex, pagenum, false);
    }

    private void reqData(int startindex, int pagenum, final boolean isPush) {

        final Dialog waitDialog = DialogUtil.showWait(SeeHouseListActivity.this);

        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);

                waitDialog.dismiss();

                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(SeeHouseListActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, KanFangListStateResp.class);
                    List<KanFangListStateResp> temps = null;
                    List<KanFangListStateResp> ts = new ArrayList<>();
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        //因为数据接口返回数据顺序是反的，所以重新倒过
//                        for (KanFangListStateResp k : temps) {
//                            ts.add(0, k);
//                        }
//                        seehouses.addAll(ts);
                        seehouses.addAll(temps);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (isPush) {
                    listView.onRefreshComplete();
                }
            }
        }).execute(Constants.kanfangListStateUrl, map);
    }


    private class SeeHouseAdapter extends BaseAdapter {
        private List<KanFangListStateResp> list;
        private LayoutInflater inflater;
        private Context context;

        private SeeHouseAdapter(Context context, List<KanFangListStateResp> list) {
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
                vh.broker_star = (RatingBar) convertView.findViewById(R.id.broker_star);
                vh.broker_score = (TextView) convertView.findViewById(R.id.broker_score);
                vh.trans_status = (TextView) convertView.findViewById(R.id.trans_status);
                vh.loupen_icon = (ImageView) convertView.findViewById(R.id.loupen_icon);
                vh.loupen_title = (TextView) convertView.findViewById(R.id.loupen_title);
                vh.loupen_price = (TextView) convertView.findViewById(R.id.loupen_price);
                vh.text = (TextView) convertView.findViewById(R.id.text);
                vh.time = (TextView) convertView.findViewById(R.id.time);
                vh.dingdandeal = (TextView) convertView.findViewById(R.id.dingdandeal);
                vh.lookhouse = (TextView) convertView.findViewById(R.id.lookhouse);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            final KanFangListStateResp kf = list.get(position);
            if (!TextUtils.isEmpty(kf.getUserimage())) {
                ImageLoader.getInstance().displayImage(kf.getUserimage(), vh.broker_icon, FangApplication.options, FangApplication.animateFirstListener);
            }
            vh.broker_name.setText(kf.getUsername());
            vh.broker_star.setRating(kf.getStarlevel());
            vh.broker_score.setText(String.valueOf(kf.getStarlevel()));
            vh.trans_status.setText(kf.getState() + " ");

            if (!TextUtils.isEmpty(kf.getLoupan_image_url())) {
                ImageLoader.getInstance().displayImage(kf.getLoupan_image_url(), vh.loupen_icon, FangApplication.options, FangApplication.animateFirstListener);
            }
            vh.loupen_title.setText(kf.getLoupanname());
            vh.loupen_price.setText(kf.getPricedesc());
            vh.text.setText(kf.getLoupan_addr());
            vh.time.setText(kf.getRequestdate_str());

            String state = kf.getState();
            vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_red));
            if (state.equals("待看房")) {
                vh.dingdandeal.setText("申请取消");
                vh.lookhouse.setText("确认看房");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getCancelKanfang());
                vh.lookhouse.setOnClickListener(c.getConfirmKanfang());

            } else if (state.equals("已看房")) {
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());

            } else if (state.equals("已取消")) {
                vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());


            } else if (state.equals("已失败")) {
                vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                vh.dingdandeal.setText("删除订单");
                vh.lookhouse.setText("投诉");

                Click c = new Click(kf, adapter, list);
                vh.dingdandeal.setOnClickListener(c.getDeleceKnafang());
                vh.lookhouse.setOnClickListener(c.getComplainKanfan());


            } else {
                vh.dingdandeal.setText("XXXX");
                vh.lookhouse.setText("CCCC");
            }

            return convertView;
        }

        private class ViewHolper {
            private ImageView broker_icon;
            private TextView broker_name;
            private RatingBar broker_star;
            private TextView broker_score;

            private TextView trans_status;

            private ImageView loupen_icon;
            private TextView loupen_title;
            private TextView loupen_price;
            private TextView text;
            private TextView time;

            private TextView dingdandeal;
            private TextView lookhouse;

        }

        private class Click {
            private KanFangListStateResp kf;
            private SeeHouseAdapter adapter;
            private List<KanFangListStateResp> list;

            public Click(KanFangListStateResp kf, SeeHouseAdapter adapter, List<KanFangListStateResp> list) {
                this.kf = kf;
                this.adapter = adapter;
                this.list = list;
            }

            private View.OnClickListener deleceKnafang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog waitDialog = DialogUtil.showWait(SeeHouseListActivity.this);
                    new HttpAsy(new PostExecute() {
                        @Override
                        public void onPostExecute(String result) {
                            waitDialog.dismiss();
                            if (!TextUtils.isEmpty(result)) {
                                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                                if (state.equals("true")) {//删除成功
                                    DialogUtil.showDealSuccess(SeeHouseListActivity.this, "删除成功", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(kf);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                } else {//删除失败
                                    DialogUtil.showDealFailed(SeeHouseListActivity.this, "删除失败", null);
                                }
                            }
                        }
                    }).execute(Constants.deleteKanfangDanUrl + "?recid=" + kf.getKanrecid() +"&roletype=user");
                }
            };

            private View.OnClickListener cancelKanfang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog waitDialog = DialogUtil.showWait(SeeHouseListActivity.this);
                    new HttpAsy(new PostExecute() {
                        @Override
                        public void onPostExecute(String result) {
                            waitDialog.dismiss();
                            if (!TextUtils.isEmpty(result)) {
                                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                                if (state.equals("true")) {//取消成功
                                    DialogUtil.showCancelSuccess(SeeHouseListActivity.this, new AloneDeal() {
                                        @Override
                                        public void deal() {//重启页面
                                            Intent intent = getIntent();
                                            overridePendingTransition(0, 0);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    DialogUtil.showCancelFailed(SeeHouseListActivity.this, null);
                                }
                            }
                        }
                    }).execute(Constants.concelKanfangDanUrl + "?recid=" + kf.getKanrecid());
                }
            };

            private View.OnClickListener confirmKanfang = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new HttpAsy(new PostExecute() {
                        @Override
                        public void onPostExecute(String result) {
                            Log.e("czhongzhi", "confirmkanfang result is " + result);
                            if (!TextUtils.isEmpty(result)) {
                                try {
                                    BrokerComm brokerComm = JacksonMapper.getObjectMapper().readValue(result, BrokerComm.class);
                                    if (brokerComm.isStatus()) {
                                        Intent intent = new Intent(SeeHouseListActivity.this, BrokerEvaluationActivity.class);
                                        intent.putExtra(BrokerEvaluationActivity.KanFangListStateResp_COMM, brokerComm);
                                        startActivity(intent);
                                    } else {
                                        DialogUtil.showDealFailed(SeeHouseListActivity.this, "确认失败", null);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).execute(Constants.confirmKanfangDanUrl + "?recid=" + kf.getKanrecid());
                }
            };

            private View.OnClickListener complainKanfan = new View.OnClickListener() {//投诉
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SeeHouseListActivity.this, BrokerComplainActivity.class);
                    intent.putExtra(BrokerComplainActivity.RECID, String.valueOf(kf.getKanrecid()));
                    intent.putExtra(BrokerComplainActivity.OPERDEST,"kanfang");
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
        }
    }


}
