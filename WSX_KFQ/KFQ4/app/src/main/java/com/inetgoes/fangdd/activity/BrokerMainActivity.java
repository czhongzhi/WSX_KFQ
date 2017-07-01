package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.model.AllTalk;
import com.inetgoes.fangdd.model.BrokerMainInfo;
import com.inetgoes.fangdd.model.HouseInfo_BrokerIntroduce;
import com.inetgoes.fangdd.util.AloneDeal;
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
 * 经纪人主页
 * Created by czz on 2015/11/06.
 */
public class BrokerMainActivity extends Activity {
    public final static String BROKER_ID = "Brokerid";
    public final static int CALLHINT = 411;
    private int brokerid;
    private String tranid;
    private int showListNum = 3;
    private int currHouseNum = 0;
    private int currCommNum = 0;
    private boolean isHouseMore = true;
    private boolean isCommMore = true;
    private BrokerMainInfo brokerMainInfo;

    private String isconnected;

    private ImageView broker_h_icon;
    private TextView tv_guanzhu;
    private TextView tv_name;
    private RatingBar broker_star;
    private TextView tv_score;
    private TextView tv_introduce;
    private TextView tv_ability;
    private TextView tv_evaluation1;
    private TextView tv_yuyue;
    private TextView tv_chengjiao;
    private TextView tv_BrokerRank;
    private TextView tv_Brokers;
    private TextView tv_AllBrokers;
    private ListView lv_BrokerIntroduce;
    private ListView lv_AllTalk;
    private TextView tv_broker_comm;

    private List<HouseInfo_BrokerIntroduce> fangYuanList = new ArrayList<>();
    private List<AllTalk> brokercomment_arr = new ArrayList<>();
    private BrokerIntroduceAdapter brokerIntroduceAdapter;
    private BrokerCommentAdapter brokerCommentAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CALLHINT:
                    DialogUtil.showCallHint(BrokerMainActivity.this,brokerMainInfo.getName(),brokerMainInfo.getCellphone(),null);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "房产专家", true, false);
        setContentView(R.layout.activity_broker_main);

        brokerid = getIntent().getIntExtra(BROKER_ID, 0);
        tranid = getIntent().getStringExtra(Constants.TRANID);
        brokerIntroduceAdapter = new BrokerIntroduceAdapter();
        brokerCommentAdapter = new BrokerCommentAdapter();

        isconnected = getIntent().getStringExtra("isconnected");
        if (isconnected == null) {
            isconnected = "Y";
        }

        initView();

        requertData();
        requestGuanzhu(Constants.bFindGuanzhuUrl, 0);
    }

    /**
     * 请求主页数据
     */
    private void requertData() {

        final Dialog waitDialog = DialogUtil.showWait(BrokerMainActivity.this);

        AppSharePrefManager sManager = AppSharePrefManager.getInstance(this);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", sManager.getLastest_login_id());
        map.put("tranid", tranid);
        //
        map.put("firstnum", showListNum);
        map.put("brokerid", brokerid);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);

                waitDialog.dismiss();

                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BrokerMainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    ObjectMapper mapper = JacksonMapper.getObjectMapper();
                    try {
                        brokerMainInfo = mapper.readValue(result, BrokerMainInfo.class);
                        if (brokerMainInfo == null)
                            return;
                        //添加数据
                        initData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.newHouseBrokerInfoUrl, map);
    }

    /**
     * 查看更多匹配楼盘
     *
     * @param startindex
     * @param pagenum
     */
    private void requestHouseMore(int startindex, int pagenum) {
        AppSharePrefManager sManager = AppSharePrefManager.getInstance(this);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", sManager.getLastest_login_id());
        map.put("tranid", tranid);

        //
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        map.put("brokerid", brokerid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BrokerMainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(result)) {
                        JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, HouseInfo_BrokerIntroduce.class);
                        List<HouseInfo_BrokerIntroduce> temps = null;
                        try {
                            temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                            //添加数据
                            addHouseData(temps);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).execute(Constants.bHuouseMoreUrl, map);
    }

    /**
     * 查看更多评论数据
     *
     * @param startindex
     * @param pagenum
     */
    private void requestTalkMore(int startindex, int pagenum) {
        Map<String, Object> map = new HashMap<>();
        //
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        map.put("brokerid", brokerid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BrokerMainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(result)) {
                        JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, AllTalk.class);
                        List<AllTalk> temps = null;
                        try {
                            temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                            //添加数据
                            addCommData(temps);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).execute(Constants.bTalkMoreUrl, map);
    }

    /**
     * 请求关注
     *
     * @param url
     * @param dealType 0:检查是否关注，1:关注，2：取消关注
     */
    private void requestGuanzhu(String url, final int dealType) {
        AppSharePrefManager sManager = AppSharePrefManager.getInstance(this);
        Map<String, Object> map = new HashMap<>();
        //
        map.put("userid", sManager.getLastest_login_id());
        map.put("objecttype", Constants.DOCTYPE_BROKER);
        map.put("objectid", brokerid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BrokerMainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(result)) {
                        String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                        switch (dealType) {
                            case 0:
                                if (state.equals("true")) {
                                    tv_guanzhu.setText(" 已关注");
                                }
                                break;
                            case 1:
                                if (state.equals("true")) {
                                    Toast.makeText(BrokerMainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    tv_guanzhu.setText(" 已关注");
                                } else {
                                    Toast.makeText(BrokerMainActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:

                                break;
                        }
                    }
                }
            }
        }).execute(url, map);
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

                        BrokerMainActivity.this.tranid = (String)map.get("tranid");

                        AloneDeal dealRight = new AloneDeal() {
                            @Override
                            public void deal() {//点击好的 跳转主页
                                Intent intent = new Intent(BrokerMainActivity.this, MainActivity.class);
                                intent.putExtra(MainActivity.ISSHOWCALLHINT, true);
                                intent.putExtra("tranid_hint",BrokerMainActivity.this.tranid);
                                startActivity(intent);
                                BrokerMainActivity.this.finish();
                            }
                        };
                        DialogUtil.showYuyueSuccess2(BrokerMainActivity.this, dealRight);
                    } else {
                        Toast.makeText(BrokerMainActivity.this, "选择失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).execute(Constants.selectBrokerUrl, map);
    }

    /**
     *
     * @param tranid
     * @param reason
     */
    public static void cancelBroker(Activity context,String tranid, final String reason, final AloneDeal aloneDeal){
        final Dialog waitDialog = DialogUtil.showWait(context);
        Map<String,Object> map = new HashMap<>();
        map.put("tranid",tranid);
        map.put("reason",reason);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                waitDialog.dismiss();
                if (TextUtils.isEmpty(result)){
                    return;
                }
                Map<String,Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if((boolean)resMap.get("state")){
                    L.LogI("取消成功");
                    aloneDeal.deal();
                }else{
                    L.LogI("取消失败");
                }
            }
        }).execute(Constants.cancelBrokerAfterconnUrl,map);
    }

    private void callHint(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(true){
                    mHandler.sendEmptyMessage(CALLHINT);
                }
            }
        }.start();
    }

    /**
     * 查找布局文件
     */
    private void initView() {
        broker_h_icon = (ImageView) findViewById(R.id.broker_h_icon);
        tv_guanzhu = (TextView) findViewById(R.id.broker_guanzhu);//关注按钮
        tv_name = (TextView) findViewById(R.id.broker_name);//经纪人姓名
        broker_star = (RatingBar) findViewById(R.id.broker_star);//星级
        tv_score = (TextView) findViewById(R.id.broker_score);//星级数字
        tv_introduce = (TextView) findViewById(R.id.introduce);//经验
        tv_ability = (TextView) findViewById(R.id.broker_ability);//擅长区域
        tv_evaluation1 = (TextView) findViewById(R.id.broker_evaluation1);//楼盘评测
        tv_yuyue = (TextView) findViewById(R.id.broker_yuyue);//预约次数
        tv_chengjiao = (TextView) findViewById(R.id.broker_chengjiao);//订单成交量
        tv_BrokerRank = (TextView) findViewById(R.id.broker_paiming);//经纪人排名
        tv_Brokers = (TextView) findViewById(R.id.broker_fangyuan);//匹配房源
        tv_AllBrokers = (TextView) findViewById(R.id.broker_allfangyuan);//所有房源
        lv_BrokerIntroduce = (ListView) findViewById(R.id.lv_fangyuan);//房源显示的listview
        lv_AllTalk = (ListView) findViewById(R.id.lv_pingjia);//评价
        tv_broker_comm = (TextView) findViewById(R.id.broker_comm);

        //房源list添加adapter
        lv_BrokerIntroduce.setAdapter(brokerIntroduceAdapter);
        //评价list添加adapter
        lv_AllTalk.setAdapter(brokerCommentAdapter);

        changeBottomButton(isconnected);

        findViewById(R.id.yuyueta).setOnClickListener(new View.OnClickListener() {//预约他
            @Override
            public void onClick(View v) {
                selectBroker(tranid, brokerid);
            }
        });

        findViewById(R.id.cancelyuyue).setOnClickListener(new View.OnClickListener() { //取消预约
            @Override
            public void onClick(View v) {
                //
                AloneDeal dealLeft = new AloneDeal() {
                    @Override
                    public void deal() {
                      //确认操作
                       cancelBroker(BrokerMainActivity.this,tranid,"用户取消",new AloneDeal() {
                           @Override
                           public void deal() {
                               startActivity(new Intent(BrokerMainActivity.this,MyYuyueActivity.class));
                               BrokerMainActivity.this.finish();
                           }
                       });
                    }
                };
//                DialogUtil.showCancelYuyueDeal(BrokerMainActivity.this,dealLeft,null);
                DialogUtil.showDeleteYuyueDeal(BrokerMainActivity.this,"确认取消预约",dealLeft,null);
            }
        });

    }

    private void changeBottomButton(String isconnected){
        View layout_connected_y = findViewById(R.id.layout_connected_y);
        View layout_connected_n = findViewById(R.id.layout_connected_n);
        if("Y".equals(isconnected)){
            layout_connected_y.setVisibility(View.GONE);
            layout_connected_n.setVisibility(View.VISIBLE);
        }else{
            layout_connected_y.setVisibility(View.VISIBLE);
            layout_connected_n.setVisibility(View.GONE);
        }
    }

    private void initData() {
        if(getIntent().getBooleanExtra("isStartCallHint",false)){
            callHint();
        }

        if (!TextUtils.isEmpty(brokerMainInfo.getUserimage_hor())) {
            ImageLoader.getInstance().displayImage(brokerMainInfo.getUserimage_hor(), broker_h_icon, FangApplication.options, FangApplication.animateFirstListener);
        }

        tv_name.setText(brokerMainInfo.getName());

        broker_star.setRating(brokerMainInfo.getStarlevel());

        tv_score.setText(brokerMainInfo.getStarlevel().toString());

        tv_introduce.setText(brokerMainInfo.getBrokertype() + ":(" + brokerMainInfo.getSkillyear() + ")");

        tv_ability.setText("擅长区域:" + (brokerMainInfo.getMasterarea().equals("ALL") ? "全部" : brokerMainInfo.getMasterarea()));

        tv_evaluation1.setText(brokerMainInfo.getEvalnum().toString());

        tv_yuyue.setText(brokerMainInfo.getAppointmentnum().toString());

        tv_chengjiao.setText(brokerMainInfo.getOrdernum().toString());

        tv_BrokerRank.setText(brokerMainInfo.getRank().toString());

        tv_Brokers.setText("与您匹配的房源:(" + brokerMainInfo.getHouse_num_matched().toString() + ")");

        tv_AllBrokers.setText("查看TA的全部房源:(" + brokerMainInfo.getHouse_num_total().toString() + ")");

        tv_broker_comm.setText("收到的评论(" + brokerMainInfo.getComment_total() + ")");


        /**房源列表开始添加数据*/

        lv_BrokerIntroduce.setFocusable(false);
        //房源list集合添加数据
        if (brokerMainInfo.getHouseinfo_matched_arr() != null || brokerMainInfo.getHouseinfo_matched_arr().size() != 0) {
            fangYuanList.addAll(brokerMainInfo.getHouseinfo_matched_arr());
        }

        //刷新房源数据
        brokerIntroduceAdapter.notifyDataSetChanged();

        /**评价列表开始添加数据*/

        lv_AllTalk.setFocusable(false);

        //评论list集合添加数据
        if (brokerMainInfo.getBrokercomment_arr() != null || brokerMainInfo.getBrokercomment_arr().size() != 0) {
            brokercomment_arr.addAll(brokerMainInfo.getBrokercomment_arr());
        }

        brokerCommentAdapter.notifyDataSetChanged();


        /**按钮事件*/

        //关注按钮
        tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实现关注功能  <临时打开经纪人评价页面>
                //startActivity(new Intent(BrokerMainActivity.this, BrokerEvaluationActivity.class));
                requestGuanzhu(Constants.bGuanzhuUrl, 1);
            }
        });

        //所有房源按钮事件
        tv_AllBrokers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BrokerMainActivity.this, BuildingListActivity.class);
                i.putExtra(BrokerMainActivity.BROKER_ID, brokerid);
                i.putExtra(Constants.TRANID,tranid);

                L.LogI("BrokerMainAct tarnid is " + tranid);

                i.putExtra("isconnected",isconnected);
                startActivity(i);
            }
        });

    }

    /**
     * 添加查看更多楼盘数据
     */
    private void addHouseData(List<HouseInfo_BrokerIntroduce> temps) {
        if (temps != null && temps.size() != 0) {
            fangYuanList.addAll(temps);
            if (temps.size() < showListNum) {
                isHouseMore = false;
            }
        }
        brokerIntroduceAdapter.notifyDataSetChanged();
    }

    /**
     * /**
     * 收起查看更多楼盘数据
     */
    private void moveHouseData() {
        isHouseMore = true;
        List<HouseInfo_BrokerIntroduce> temps = new ArrayList<>();
        for (int i = showListNum; i < fangYuanList.size(); i++) {
            temps.add(fangYuanList.get(i));
        }
        fangYuanList.removeAll(temps);
        brokerIntroduceAdapter.notifyDataSetChanged();
        currHouseNum = 0;
    }

    /**
     * 添加查看更多评论数据
     */
    private void addCommData(List<AllTalk> temps) {
        if (temps != null && temps.size() != 0) {
            brokercomment_arr.addAll(temps);
            if (temps.size() < showListNum) {
                isCommMore = false;
            }
        }
        brokerCommentAdapter.notifyDataSetChanged();
    }

    /**
     * /**
     * 收起查看更多评论数据
     */
    private void moveCommData() {
        isCommMore = true;
        List<AllTalk> temps = new ArrayList<>();
        for (int i = showListNum; i < brokercomment_arr.size(); i++) {
            temps.add(brokercomment_arr.get(i));
        }
        brokercomment_arr.removeAll(temps);
        brokerCommentAdapter.notifyDataSetChanged();
        currCommNum = 0;
    }

    /**
     * 匹配房源列表适配器
     */
    private class BrokerIntroduceAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fangYuanList.size() >= 3 ? fangYuanList.size() + 1 : fangYuanList.size();
        }

        @Override
        public Object getItem(int position) {
            return fangYuanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (fangYuanList.size() < 3) {
                return 0;
            }
            return position == fangYuanList.size() ? 1 : 0;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            switch (getItemViewType(position)) {

                case 0: {
                    ViewHolder viewHolder;
                    if (null == convertView) {
                        viewHolder = new ViewHolder();
                        convertView = View.inflate(BrokerMainActivity.this, R.layout.item_broker_fangyuan2, null);

                        viewHolder.fangyuan_image = (ImageView) convertView.findViewById(R.id.item_iv_fangyuan);
                        viewHolder.fangyuan_name = (TextView) convertView.findViewById(R.id.item_tv_fangyuan1);
                        viewHolder.fangyuan_cishu = (TextView) convertView.findViewById(R.id.item_tv_fangyuan2);
                        viewHolder.fangyuan_chengjiaol = (TextView) convertView.findViewById(R.id.item_tv_fangyuan3);
                        viewHolder.fangyuan_prices = (TextView) convertView.findViewById(R.id.item_tv_fangyuan4);
                        viewHolder.fangyuan_position = (TextView) convertView.findViewById(R.id.item_tv_fangyuan5);
                        viewHolder.item_tv_huxing_m = (TextView) convertView.findViewById(R.id.item_tv_huxing_m);
                        convertView.setTag(viewHolder);

                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    final HouseInfo_BrokerIntroduce hbi = fangYuanList.get(position);

                    if (hbi != null && viewHolder != null)

                    {
                        //加载图片
                        String imageurl = hbi.getLoupan_image_url();
                        if (!TextUtils.isEmpty(imageurl)) {
                            ImageLoader.getInstance().displayImage(imageurl, viewHolder.fangyuan_image, FangApplication.options, FangApplication.animateFirstListener);
                        }
                        //viewHolder.fangyuan_image.setImageResource(hbi.getLoupan_image_url());
                        viewHolder.fangyuan_name.setText(hbi.getLoupanname());
                        viewHolder.fangyuan_cishu.setText("看房次数：" + hbi.getKanfang_time().toString());
                        viewHolder.fangyuan_chengjiaol.setText("成交量：" + hbi.getTran_success_num().toString());
                        viewHolder.fangyuan_prices.setText(hbi.getPricedesc());
                        viewHolder.fangyuan_position.setText(hbi.getStrict());

                        viewHolder.item_tv_huxing_m.setText(hbi.getMatch_huxing());
                    }

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BrokerMainActivity.this, BuildingMainActivity.class);
                            intent.putExtra(BuildingMainActivity.NEWCODE, hbi.getNewcode());
                            intent.putExtra(BuildingMainActivity.TITLEBAR, hbi.getLoupanname());
                            intent.putExtra(BrokerMainActivity.BROKER_ID, brokerid);
                            intent.putExtra(Constants.TRANID, tranid);
                            intent.putExtra("isconnected", isconnected);
                            startActivity(intent);
                        }
                    });
                    break;
                }
                case 1: {
                    ViewGroup view = (ViewGroup) View.inflate(BrokerMainActivity.this, R.layout.item_broker_loadmore, null);
                    if (isHouseMore) {
                        view.getChildAt(0).setVisibility(View.VISIBLE);
                    } else {
                        view.getChildAt(0).setVisibility(View.INVISIBLE);
                    }
                    view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currHouseNum += showListNum;
                            requestHouseMore(currHouseNum, showListNum);
                        }
                    });

                    if (fangYuanList.size() <= 3) {
                        view.getChildAt(1).setVisibility(View.INVISIBLE);
                    } else {
                        view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                moveHouseData();
                            }
                        });
                    }
                    convertView = view;
                    break;
                }
            }
            return convertView;
        }

        /**
         * 房源的holder
         */
        class ViewHolder {
            ImageView fangyuan_image;
            TextView fangyuan_name;
            TextView fangyuan_cishu;
            TextView fangyuan_chengjiaol;
            TextView fangyuan_prices;
            TextView fangyuan_position;

            TextView item_tv_huxing_m; ////户型 面积 三室 85㎡、90㎡ --- add 20151223
        }
    }

    /**
     * 经济人评论列表适配器
     */
    private class BrokerCommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return brokercomment_arr.size() >= 3 ? brokercomment_arr.size() + 1 : brokercomment_arr.size();

        }

        @Override
        public Object getItem(int position) {
            return brokercomment_arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (brokercomment_arr.size() < 3) {
                return 0;
            }
            return position == brokercomment_arr.size() ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {

                case 0: {
                    PinglunViewHolder viewHolder;
                    if (null == convertView) {
                        viewHolder = new PinglunViewHolder();

                        convertView = View.inflate(BrokerMainActivity.this, R.layout.item_evaluation, null);

                        viewHolder.item_evaluation_tvtime = (TextView) convertView.findViewById(R.id.item_evaluation_tvtime);
                        viewHolder.item_evaluation_tv = (TextView) convertView
                                .findViewById(R.id.item_evaluation_tv);
                        viewHolder.evaluation_star = (RatingBar) convertView
                                .findViewById(R.id.evaluation_star);

                        convertView.setTag(viewHolder);

                    } else {
                        viewHolder = (PinglunViewHolder) convertView.getTag();
                    }

                    AllTalk allTalk = brokercomment_arr.get(position);

                    if (allTalk != null) {
                        viewHolder.item_evaluation_tvtime.setText(allTalk.getCreatetime_str());
                        viewHolder.item_evaluation_tv.setText(allTalk.getContentdesc());
                        viewHolder.evaluation_star.setRating(allTalk.getStarlevel() == null ? 0 : allTalk.getStarlevel());
                    }
                    break;
                }
                case 1: {
                    ViewGroup view = (ViewGroup) View.inflate(BrokerMainActivity.this, R.layout.item_broker_loadmore, null);
                    if (isCommMore) {
                        view.getChildAt(0).setVisibility(View.VISIBLE);
                    } else {
                        view.getChildAt(0).setVisibility(View.INVISIBLE);
                    }
                    view.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currCommNum += showListNum;
                            requestTalkMore(currCommNum, showListNum);
                        }
                    });

                    if (brokercomment_arr.size() <= 3) {
                        view.getChildAt(1).setVisibility(View.INVISIBLE);
                    } else {
                        view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                moveCommData();
                            }
                        });
                    }
                    convertView = view;
                    break;
                }
            }
            return convertView;
        }

        class PinglunViewHolder {
            TextView item_evaluation_tvtime;
            TextView item_evaluation_tv;
            RatingBar evaluation_star;

        }
    }


}
