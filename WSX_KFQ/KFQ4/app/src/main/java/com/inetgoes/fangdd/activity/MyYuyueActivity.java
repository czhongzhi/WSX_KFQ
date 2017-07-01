package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.inetgoes.fangdd.model.MyYuyue;
import com.inetgoes.fangdd.util.AloneDeal;
import com.inetgoes.fangdd.util.AppUtil;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.util.L;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的预约
 * Created by czz on 2015/11/11
 */
public class MyYuyueActivity extends Activity {
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
        setContentView(R.layout.activity_my_yuyue);

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        initView();

        requertData(startindex, pagenum, false, false);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int pagenum = yuyues.size();
        requertData(0, pagenum, false, true);
    }

    private void initView() {

        listView = (PullToRefreshListView) findViewById(R.id.listView);

        View none_content = LayoutInflater.from(this).inflate(R.layout.none_content_hint, null);
        ((ImageView) none_content.findViewById(R.id.none_icon)).setImageResource(R.drawable.sub);
        ((TextView) none_content.findViewById(R.id.none_text)).setText("暂无预约");
        listView.setEmptyView(none_content);

        adapter = new MyYuyueAdapter(this, yuyues);

        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex += pagenum;
                requertData(startindex, pagenum, true, false);
            }
        });
    }


    private void requertData(int startindex, int pagenum, final boolean isLoad, final boolean isClear) {

        final Dialog waitDialog = DialogUtil.showWait(MyYuyueActivity.this);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("MyYuyueActivity", "result----" + result);

                waitDialog.dismiss();

                if (!TextUtils.isEmpty(result)) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, MyYuyue.class);
                    List<MyYuyue> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        if (isClear) {
                            yuyues.clear();
                        }
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
        }).execute(Constants.myYuyueListUrl + "?userid=" + userid + "&startindex=" + startindex + "&pagenum=" + pagenum);
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
            final MyYuyue myYuyue = list.get(position);
            if (convertView == null) {
                vh = new ViewHolper();
                convertView = inflater.inflate(R.layout.item_myyuyue, parent, false);
                vh.broker_img = (ImageView) convertView.findViewById(R.id.broker_img);
                vh.broker_name = (TextView) convertView.findViewById(R.id.broker_name);
                vh.introduce = (TextView) convertView.findViewById(R.id.introduce);
                vh.broker_score = (TextView) convertView.findViewById(R.id.broker_score);
                vh.tadehouse = (TextView) convertView.findViewById(R.id.tadehouse);
                vh.sendmess = (TextView) convertView.findViewById(R.id.yuyueta);
                vh.status = (TextView) convertView.findViewById(R.id.trans_status);
                vh.time = (TextView) convertView.findViewById(R.id.trans_time);
                vh.broker_star = (RatingBar) convertView.findViewById(R.id.broker_star);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            //加载图片
            String broker_img = myYuyue.getUserimage_ver();
            if (!TextUtils.isEmpty(broker_img)) {
                ImageLoader.getInstance().displayImage(broker_img, vh.broker_img, FangApplication.options_R, FangApplication.animateFirstListener);
            }

            vh.broker_name.setText(myYuyue.getUsername());
            vh.introduce.setText(myYuyue.getProfname() + "(" + myYuyue.getSkillyear() + ")");
            vh.broker_star.setRating(myYuyue.getStarlevel());
            vh.broker_score.setText(myYuyue.getStarlevel().toString());
            //交易时间
            vh.time.setText(myYuyue.getRequestdate_str());

            //交易状态:
            String transtate = myYuyue.getTranstate();
            //各交易状态下的按钮
            vh.status.setText(transtate + " ");
            vh.status.setTextColor(getResources().getColor(R.color.divider_font_red));
            if (!TextUtils.isEmpty(transtate)) {
                if ("已预约".equals(transtate)) {
                    vh.tadehouse.setVisibility(View.VISIBLE);
                    vh.tadehouse.setText("发送消息");
                    vh.sendmess.setVisibility(View.VISIBLE);
                    vh.sendmess.setText("拨打电话");

                    vh.tadehouse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "发送消息", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuyueActivity.this, MessageingActivity.class);
                            intent.putExtra(MessageingActivity.SESSION_ID, myYuyue.getSessionid());
                            intent.putExtra(MessageingActivity.SESSION_FROM, myYuyue.getUsername());
                            intent.putExtra(MessageingActivity.SESSION_TOUSERID, myYuyue.getBrokerid());
                            intent.putExtra("isFromMyYuyue", true);
                            startActivity(intent);
                        }
                    });

//                    vh.sendmess.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AloneDeal dealLeft = new AloneDeal() {
//                                @Override
//                                public void deal() {
//                                    //确认操作
//                                    cancelBroker(myYuyue.getSessionid(),"我的预约取消",new AloneDeal() {
//                                        @Override
//                                        public void deal() {
//                                            int pagenum = list.size();
//                                            requertData(0, pagenum, false, true);
//                                        }
//                                    });
//                                }
//                            };
//                            DialogUtil.showCancelYuyueDeal(MyYuyueActivity.this,dealLeft,null);
//                        }
//                    });
                    vh.sendmess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUtil.call2(MyYuyueActivity.this, myYuyue.getCellphone());
                        }
                    });

                } else if ("已取消".equals(transtate)) {
                    vh.isConn = false;
                    vh.status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                    vh.tadehouse.setVisibility(View.VISIBLE);
                    vh.tadehouse.setText("删除");
                    vh.sendmess.setVisibility(View.VISIBLE);
                    vh.sendmess.setText("投诉");

                    vh.tadehouse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(myYuyue.getSessionid(), "user", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(myYuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuyueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.sendmess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuyueActivity.this, BrokerComplainActivity.class);
                            intent.putExtra(BrokerComplainActivity.RECID, myYuyue.getSessionid());
                            intent.putExtra(BrokerComplainActivity.OPERDEST,"tran");
                            startActivity(intent);
                        }
                    });

                } else if ("已完成".equals(transtate)) {
                    vh.tadehouse.setVisibility(View.VISIBLE);
                    vh.tadehouse.setText("删除");
                    vh.sendmess.setVisibility(View.VISIBLE);
                    vh.sendmess.setText("投诉");

                    vh.tadehouse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(myYuyue.getSessionid(), "user", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(myYuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuyueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.sendmess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuyueActivity.this, BrokerComplainActivity.class);
                            intent.putExtra(BrokerComplainActivity.RECID, myYuyue.getSessionid());
                            intent.putExtra(BrokerComplainActivity.OPERDEST,"tran");
                            startActivity(intent);
                        }
                    });

                } else if ("已失败".equals(transtate)) {
                    vh.isConn = false;
                    vh.status.setTextColor(getResources().getColor(R.color.divider_font_mid));
                    vh.tadehouse.setVisibility(View.VISIBLE);
                    vh.tadehouse.setText("删除");
                    vh.sendmess.setVisibility(View.VISIBLE);
                    vh.sendmess.setText("投诉");

                    vh.tadehouse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AloneDeal aloneDeal = new AloneDeal() {
                                @Override
                                public void deal() {
                                    deleteBroker(myYuyue.getSessionid(), "user", new AloneDeal() {
                                        @Override
                                        public void deal() {
                                            list.remove(myYuyue);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            DialogUtil.showDeleteYuyueDeal(MyYuyueActivity.this, "确定取消预约", aloneDeal, null);
                        }
                    });

                    vh.sendmess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, "投诉功能暂未开放", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyYuyueActivity.this, BrokerComplainActivity.class);
                            intent.putExtra(BrokerComplainActivity.RECID, myYuyue.getSessionid());
                            intent.putExtra(BrokerComplainActivity.OPERDEST,"tran");
                            startActivity(intent);
                        }
                    });
                }
            }


            final ViewHolper finalVh = vh;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyYuyueActivity.this, BrokerMainActivity.class);
                    i.putExtra(BrokerMainActivity.BROKER_ID, myYuyue.getBrokerid());
                    i.putExtra(Constants.TRANID, myYuyue.getSessionid());
                    L.LogI("MyYuyue tarnid is " + myYuyue.getSessionid());
                    if (finalVh.isConn) {
                        i.putExtra("isconnected", "Y");
                    } else {
                        i.putExtra("isconnected", "N");
                    }
                    startActivity(i);
                }
            });

            return convertView;
        }

        private void selectBroker(String tranid, int brokerid) {
            Map<String, Object> map = new HashMap<>();
            map.put("tranid", tranid);
            map.put("brokerid", brokerid);
            new HttpAsy(new PostExecute() {
                @Override
                public void onPostExecute(String result) {
                    Log.e("czhongzhi", "result -- " + result);
                    if (!TextUtils.isEmpty(result)) {
                        Map<String, Object> map = JacksonMapper.getInstance().mapObjFromJson(result);
                        Log.e("czhongzhi", map.get("status") + "");
                        Log.e("czhongzhi", map.get("reason") + "");
                        if ((boolean) map.get("status")) {
                            AloneDeal dealLeft = new AloneDeal() {
                                @Override
                                public void deal() {//点击查看预约 跳转我的预约
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            };
                            AloneDeal dealRight = new AloneDeal() {
                                @Override
                                public void deal() {//点击好的 跳转主页
                                    //
                                }
                            };
                            DialogUtil.showYuyueSuccess(MyYuyueActivity.this, dealLeft, dealRight);
                        } else {
                            Toast.makeText(MyYuyueActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }).execute(Constants.selectBrokerUrl, map);
        }

        private class ViewHolper {
            private ImageView broker_img;
            private TextView broker_name;
            private TextView introduce;
            private TextView broker_score;
            private TextView tadehouse;
            private TextView sendmess;
            private TextView status;
            private TextView time;
            private RatingBar broker_star;

            private boolean isConn = true;
        }
    }

    /**
     * @param tranid
     * @param reason
     */
    public void cancelBroker(String tranid, final String reason, final AloneDeal aloneDeal) {
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("reason", reason);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if ((boolean) resMap.get("state")) {
                    L.LogI("取消成功");
                    aloneDeal.deal();
                } else {
                    L.LogI("取消失败");
                }
            }
        }).execute(Constants.cancelBrokerAfterconnUrl, map);
    }


    /**
     * @param tranid
     */
    public void deleteBroker(String tranid, String roletype, final AloneDeal aloneDeal) {
        final Dialog dialog = DialogUtil.showWait(MyYuyueActivity.this);
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("roletype", roletype);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                dialog.dismiss();
                L.LogI("MyYuyueActivity deleteBroker result is " + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if ((boolean) resMap.get("state")) {
                    L.LogI("删除成功");
                    aloneDeal.deal();
                } else {
                    L.LogI("删除失败");
                    Toast.makeText(MyYuyueActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute(Constants.myYuyueDelUrl, map);
    }

}
