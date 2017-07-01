package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.model.BrokerResponse;
import com.inetgoes.fangdd.model.HouseInfo_BrokerIntroduce;
import com.inetgoes.fangdd.util.AloneDeal;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.inetgoes.fangdd.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 匹配经纪人
 * Created by czz on 2015/11/02.
 */
public class MateBrokerActivity extends Activity {
    public static MateBrokerActivity activity;
    private ListView listView;
    private RadioGroup layout_tap;
    private MateBrokerAdapter adapter;
    private List<BrokerResponse> brokers = new ArrayList<>();

    private String orderdy = "createtime";   //排序字段，值有: createtime, starlevel,fangnum,ordernum
    private int showListNum = 3;
    private int currNum = 0;
    private boolean isMore = true;

    private String tranid;

    private Dialog dialog_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBarCancel(this, "确认砖家", "取消", new RightDeal() {
            @Override
            public void deal() {
                //
                if (tranid != null) {
                    dialog_cancel = DialogUtil.showCanceling(MateBrokerActivity.this, "正在取消");
                    cancelBroker();
                } else {
                    Log.e("czhongzhi", "确认砖家取消 tranid == null");
                }
            }
        });
        setContentView(R.layout.activity_mate_broker);

        activity = this;

        tranid = getIntent().getStringExtra(Constants.TRANID);

        initView();

        requestData(orderdy, 0, showListNum,false);

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MateBrokerAdapter(this, brokers, tranid);

        listView.setAdapter(adapter);

        layout_tap = (RadioGroup) findViewById(R.id.layout_tap);
        layout_tap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.starlevel:
                        orderdy = "starlevel";
                        break;
                    case R.id.fangnum:
                        orderdy = "fangnum";
                        break;
                    case R.id.ordernum:
                        orderdy = "ordernum";
                        break;
                }
                int onePageNum = brokers.size();
                requestData(orderdy,0,onePageNum,true);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    /**
     * 显示已抢单的新房经纪人
     *
     * @param orderby    排序字段，值有: createtime, starlevel,fangnum,ordernum
     * @param startindex 起始行, 0表示第一行
     * @param pagenum    一页显示数量
     */
    private void requestData(String orderby, int startindex, int pagenum, final boolean isClear) {
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("orderby", orderby);
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (!TextUtils.isEmpty(result)) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, BrokerResponse.class);
                    List<BrokerResponse> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        if(isClear){
                            brokers.clear();
                        }
                        addData(temps);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.reqDisplayBrokerUrl, map);
    }

    private void cancelBroker() {
        Map<String, String> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("reason", "确认经济人——取消");
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (dialog_cancel != null && dialog_cancel.isShowing()) {
                    dialog_cancel.dismiss();
                }
                if (!TextUtils.isEmpty(result)) {
                    String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("state");
                    if (state.equals("true")) {
                        DialogUtil.showCancelSuccess(MateBrokerActivity.this, new AloneDeal() {
                            @Override
                            public void deal() {
                                startActivity(new Intent(MateBrokerActivity.this, MainActivity.class));
                                MateBrokerActivity.this.finish();
                            }
                        });
                    } else {
//                        Toast.makeText(MateBrokerActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                        DialogUtil.showCancelFailed(MateBrokerActivity.this, null);
                    }
                }
            }
        }).execute(Constants.cancelBrokerUrl, map);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MateBrokerActivity.this, MainActivity.class));
            MateBrokerActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 添加查看更多数据
     */
    private void addData(List<BrokerResponse> temps) {
        if (temps != null) {
            brokers.addAll(temps);
            if (temps.size() < showListNum) {
                isMore = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * /**
     * 收起查看更多数据
     */
    private void moveData() {
        isMore = true;
        List<BrokerResponse> temps = new ArrayList<>();
        for (int i = showListNum; i < brokers.size(); i++) {
            temps.add(brokers.get(i));
        }
        brokers.removeAll(temps);
        adapter.notifyDataSetChanged();
        currNum = 0;
    }

    private class MateBrokerAdapter extends BaseAdapter {
        private List<BrokerResponse> list;
        private LayoutInflater inflater;
        private Context context;
        private String tranid;

        private MateBrokerAdapter(Context context, List<BrokerResponse> list, String tranid) {
            this.context = context;
            this.list = list;
            this.tranid = tranid;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            int l = list.size();
            return l >= 3 ? l + 1 : l;
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
        public int getItemViewType(int position) {
            if (list.size() < 3) {
                return 0;
            }
            return position == list.size() ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolper vh = null;
            if (convertView == null) {
                vh = new ViewHolper();
                switch (getItemViewType(position)) {
                    case 0:
                        convertView = inflater.inflate(R.layout.item_matebroker_2, parent, false);
                        vh.broker_img = (ImageView) convertView.findViewById(R.id.broker_img);
                        vh.broker_name = (TextView) convertView.findViewById(R.id.broker_name);
                        vh.introduce = (TextView) convertView.findViewById(R.id.introduce);
                        vh.broker_score = (TextView) convertView.findViewById(R.id.broker_score);
                        vh.tadehouse = (TextView) convertView.findViewById(R.id.tadehouse);
                        vh.yuyueta = (TextView) convertView.findViewById(R.id.yuyueta);
                        vh.trans_num = (TextView) convertView.findViewById(R.id.trans_num);
                        vh.broker_star = (RatingBar) convertView.findViewById(R.id.broker_star);

                        break;
                    case 1:
                        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.item_broker_ok_loadmore, parent, false);
                        if (isMore) {
                            view.getChildAt(0).setVisibility(View.VISIBLE);
                        } else {
                            view.getChildAt(0).setVisibility(View.INVISIBLE);
                        }
                        view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currNum += showListNum;
                                requestData(orderdy, currNum, showListNum,false);
                            }
                        });

                        if (list.size() <= 3) {
                            view.getChildAt(1).setVisibility(View.INVISIBLE);
                        } else {
                            view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    moveData();
                                }
                            });
                        }
                        convertView = view;
                        break;
                }
                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            switch (getItemViewType(position)) {
                case 0:
                    final ViewHolper finalVh = vh;
                    final BrokerResponse br = list.get(position);
                    vh.yuyueta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectBroker(finalVh.brokerid);
                        }
                    });

                    vh.tadehouse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(context, "TA的房源", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MateBrokerActivity.this, MatchingBuidingListActivity.class);
                            intent.putExtra(BrokerMainActivity.BROKER_ID, br.getBrokerid());
                            intent.putExtra(Constants.TRANID, tranid);
                            startActivity(intent);

                        }
                    });


                    vh.brokerid = br.getBrokerid();
                    vh.broker_name.setText(br.getUsername());
                    String tempStr = "成交 " + br.getOrdernum() + " 房源 " + br.getFangtotal();
                    vh.trans_num.setText(tempStr);
                    String intro = br.getProfname() + "(" + br.getSkillyear() + "年)";
                    vh.introduce.setText(intro);
                    vh.broker_star.setRating(Float.valueOf(br.getStarlevel()));
                    vh.broker_score.setText(String.valueOf(br.getStarlevel()));

                    if (!TextUtils.isEmpty(br.getUserimage_ver())) {
                        ImageLoader.getInstance().displayImage(br.getUserimage_ver(), vh.broker_img, FangApplication.options, FangApplication.animateFirstListener);
                    }

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MateBrokerActivity.this, BrokerMainActivity.class);
                            intent.putExtra(BrokerMainActivity.BROKER_ID, br.getBrokerid());
                            intent.putExtra(Constants.TRANID, tranid);
                            intent.putExtra("isconnected", "N");
                            startActivity(intent);
                        }
                    });

                    break;
            }
            return convertView;
        }

        private class ViewHolper {
            private ImageView broker_img;
            private TextView broker_name;
            private TextView introduce;
            private TextView broker_score;
            private TextView tadehouse;
            private TextView yuyueta;
            private TextView trans_num;
            private RatingBar broker_star;

            //关联参数
            private Integer brokerid;
        }

        private void selectBroker(final int brokerid) {
            Map<String, Object> map = new HashMap<>();
            map.put("tranid", this.tranid);
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

                            tranid = (String)map.get("tranid");

//                            AloneDeal dealLeft = new AloneDeal() {
//                                @Override
//                                public void deal() {//点击查看预约 跳转我的预约
//                                    MateBrokerActivity.this.startActivity(new Intent(MateBrokerActivity.this, MyYuyueActivity.class));
//                                    MateBrokerActivity.this.finish();
//                                }
//                            };
//                            AloneDeal dealRight = new AloneDeal() {
//                                @Override
//                                public void deal() {//点击好的 跳转房产专家
//
//                                    Intent intent = new Intent(MateBrokerActivity.this, BrokerMainActivity.class);
//                                    intent.putExtra(BrokerMainActivity.BROKER_ID, brokerid);
//                                    intent.putExtra(Constants.TRANID, tranid);
//                                    intent.putExtra("isconnected", "Y");
//                                    intent.putExtra("isStartCallHint",true);
//                                    startActivity(intent);
//                                    MateBrokerActivity.this.finish();
//                                }
//                            };
//                            DialogUtil.showYuyueSuccess(MateBrokerActivity.this, dealLeft, dealRight);

                            AloneDeal dealRight = new AloneDeal() {
                                @Override
                                public void deal() {//点击好的 跳转主页
                                    Intent intent = new Intent(MateBrokerActivity.this, MainActivity.class);
                                    intent.putExtra(MainActivity.ISSHOWCALLHINT, true);
                                    intent.putExtra("tranid_hint",tranid);
                                    startActivity(intent);
                                    MateBrokerActivity.this.finish();
                                }
                            };
                            DialogUtil.showYuyueSuccess2(MateBrokerActivity.this, dealRight);
                        } else {
                            Toast.makeText(context, "选择失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }).execute(Constants.selectBrokerUrl, map);
        }
    }


}
