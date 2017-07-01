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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.MyYuyue;
import com.inetgoes.kfqbrokers.utils.AloneDeal;
import com.inetgoes.kfqbrokers.utils.AppUtil;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyYuYueActivity extends Activity {

    private PullToRefreshListView listView;
    private MyYuyueAdapter adapter;
    private List<MyYuyue> yuyues = new ArrayList<>();

    private int userid;  //请求人的用户id
    private int startindex = 0;
    private int pagenum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "我的预约", true, false);
        setContentView(R.layout.activity_my_yu_yue);

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        initView();

        requertData(startindex, pagenum, false);

    }

    private void requertData(int startindex, int pagenum, final boolean isLoad) {
        final Dialog waitDiglog = DialogUtil.showWait(MyYuYueActivity.this);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("MyYuyueActivity", "result----" + result);

                waitDiglog.dismiss();

                if (!TextUtils.isEmpty(result)) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, MyYuyue.class);
                    List<MyYuyue> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        yuyues.addAll(temps);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (isLoad) {
                    //刷新完成
                    listView.onRefreshComplete();
                }
            }
        }).execute(Constants.myYuyueListUrl + "?brokerid=" + userid + "&startindex=" + startindex + "&pagenum=" + pagenum);
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listView);

        View none_content = LayoutInflater.from(this).inflate(R.layout.none_content_hint, null);
        ((ImageView) none_content.findViewById(R.id.none_icon)).setImageResource(R.drawable.sub);
        ((TextView) none_content.findViewById(R.id.none_text)).setText("暂无预约");
        listView.setEmptyView(none_content);

        adapter = new MyYuyueAdapter(this, yuyues);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MyYuYueActivity.this, "position -- " + (position - 1), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex += pagenum;
                requertData(startindex, pagenum, true);
            }
        });
    }


    private class MyYuyueAdapter extends BaseAdapter {
        private List<MyYuyue> list;
        private LayoutInflater inflater;
        private Context context;

        private MyYuyueAdapter(Context context, List<MyYuyue> list) {
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
                convertView = inflater.inflate(R.layout.item_myyuyue, parent, false);
                vh.user_icon = (ImageView) convertView.findViewById(R.id.user_icon);
                vh.user_name = (TextView) convertView.findViewById(R.id.user_name);
                vh.trans_status = (TextView) convertView.findViewById(R.id.trans_status);
                vh.trans_time = (TextView) convertView.findViewById(R.id.trans_time);
                vh.mate_title = (TextView) convertView.findViewById(R.id.mate_title);
                vh.mate_huxing = (TextView) convertView.findViewById(R.id.mate_huxing);
                vh.mate_area = (TextView) convertView.findViewById(R.id.mate_area);
                vh.mate_peitao = (TextView) convertView.findViewById(R.id.mate_peitao);
                vh.mate_price = (TextView) convertView.findViewById(R.id.mate_price);

                vh.send_msg = (TextView) convertView.findViewById(R.id.send_msg);
                vh.call_phone = (TextView) convertView.findViewById(R.id.call_phone);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            final MyYuyue yuyue = list.get(position);
            if (!TextUtils.isEmpty(yuyue.getUserimage())) {
                ImageLoader.getInstance().displayImage(yuyue.getUserimage(), vh.user_icon, FangApplication.options_R, FangApplication.animateFirstListener);
            }
            vh.user_name.setText(yuyue.getCellphone());
            vh.trans_status.setText(yuyue.getState() +" ");
            vh.trans_time.setText(yuyue.getConnecttime_str());
            vh.mate_title.setText("楼盘 " + (TextUtils.isEmpty(yuyue.getLoupanname()) ? "" : yuyue.getLoupanname()));
            vh.mate_huxing.setText("户型 " + (TextUtils.isEmpty(yuyue.getHuxing()) ? "不限" : yuyue.getHuxing()));
            vh.mate_area.setText("区域 " + yuyue.getArea());
            vh.mate_peitao.setText("配套 " + (TextUtils.isEmpty(yuyue.getPeitao()) ? "不限" : yuyue.getPeitao()));

            vh.mate_price.setText("总价 " + MainActivity.recomPrice(yuyue.getPrice_low(),yuyue.getPrice_high()));


            //交易状态:
            String transtate = yuyue.getState();
            //各交易状态下的按钮
            vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_red));
            if (!TextUtils.isEmpty(transtate)) {
                if ("已预约".equals(transtate)) {
                    vh.send_msg.setVisibility(View.VISIBLE);
                    vh.send_msg.setText("发送消息");
                    vh.call_phone.setVisibility(View.VISIBLE);
                    vh.call_phone.setText("拨打电话");

                    vh.send_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "发送消息", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuYueActivity.this, MessageingActivity.class);
                            intent.putExtra(MessageingActivity.SESSION_ID, yuyue.getSessionid());
                            intent.putExtra(MessageingActivity.SESSION_FROM, yuyue.getCellphone());
                            intent.putExtra(MessageingActivity.SESSION_TOUSERID, yuyue.getUserid());
                            intent.putExtra("isFromMyYuyue", true);
                            startActivity(intent);
                        }
                    });

                    vh.call_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.call2(context,yuyue.getCellphone());
                        }
                    });

                } else if ("已取消".equals(transtate)) {
                    vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                    vh.send_msg.setVisibility(View.VISIBLE);
                    vh.send_msg.setText("删除");
                    vh.call_phone.setVisibility(View.VISIBLE);
                    vh.call_phone.setText("投诉");

                    vh.send_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(yuyue.getSessionid(), "broker", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(yuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuYueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.call_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuYueActivity.this, BrokerComplainActivity.class);
                            intent.putExtra(BrokerComplainActivity.RECID, yuyue.getSessionid());
                            intent.putExtra(BrokerComplainActivity.OPERDEST,"tran");
                            startActivity(intent);
                        }
                    });

                } else if ("已完成".equals(transtate)) {
                    vh.send_msg.setVisibility(View.VISIBLE);
                    vh.send_msg.setText("删除");
                    vh.call_phone.setVisibility(View.VISIBLE);
                    vh.call_phone.setText("投诉");

                    vh.send_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(yuyue.getSessionid(), "broker", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(yuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuYueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.call_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if ("已失败".equals(transtate)) {
                    vh.trans_status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                    vh.send_msg.setVisibility(View.VISIBLE);
                    vh.send_msg.setText("删除");
                    vh.call_phone.setVisibility(View.VISIBLE);
                    vh.call_phone.setText("投诉");

                    vh.send_msg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(yuyue.getSessionid(), "broker", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(yuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuYueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.call_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuYueActivity.this, BrokerComplainActivity.class);
                            intent.putExtra(BrokerComplainActivity.RECID, yuyue.getSessionid());
                            intent.putExtra(BrokerComplainActivity.OPERDEST,"tran");
                            startActivity(intent);
                        }
                    });
                }
            }


            return convertView;
        }

        private class ViewHolper {
            private ImageView user_icon;
            private TextView user_name;
            private TextView trans_status;
            private TextView trans_time;
            private TextView mate_title;
            private TextView mate_huxing;
            private TextView mate_area;
            private TextView mate_peitao;
            private TextView mate_price;
            private TextView send_msg;
            private TextView call_phone;
        }
    }

    /**
     * @param tranid
     */
    public void deleteBroker(String tranid, String roletype, final AloneDeal aloneDeal) {
        final Dialog dialog = DialogUtil.showWait(MyYuYueActivity.this);
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("roletype", roletype);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                dialog.dismiss();
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if ((boolean) resMap.get("state")) {
                    L.LogI("删除成功");
                    aloneDeal.deal();
                } else {
                    L.LogI("删除失败");
                    Toast.makeText(MyYuYueActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.myYuyueDelUrl, map);
    }
}
