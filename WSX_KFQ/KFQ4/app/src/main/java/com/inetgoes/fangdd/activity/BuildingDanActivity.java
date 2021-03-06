package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.model.BrokerComm;
import com.inetgoes.fangdd.model.HouseInfoBasedataResp;
import com.inetgoes.fangdd.util.AloneDeal;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.BuildingFragment;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunhui on 2015/11/10.
 */
public class BuildingDanActivity extends Activity {

    public static final String NEWCODE = "buidingid";
    public static final String TITLEBAR = "title_bar";
    public static final String KanReqId = "KanReqId";

    public static final String But_Left = "button_left";
    public static final String But_Right = "button_right";

    public static final String HOUSEINFOBEAN = "houseInfoBasedataResp";


    public HouseInfoBasedataResp houseInfoBasedataResp;//楼盘bean
    //    private int brokerid;
//    private String tranid;
    private ImageView iv_buiding;
    private TextView tv_building_pricedesc;
    private TextView tv_buiding_position;
    private TextView tv_broker_evaluation1;
    private TextView tv_building_seenumber;
    private TextView tv_building_chengjiao;
    private TextView tv_buiding_brokersnum;
    private ListView lv_fangyuan;
    private BuildingFragment buildingfg;
    private RadioGroup layout_tap;
    private View mIndicator;
    private int mIndicatorStepDistance;
    private FragmentManager mFm;
    private int mIndicatorPosition = 0;
    private String newcode;
    private String titlebar;
    private Bundle bundle;

    private Button yuyuekanfang;

    private String huxingtype;  //字符中, 户型类型，值有: 一室、二室、三室、四室、五室以上  （可写）
    private long msgid;     //长整型, 消息id   （可写）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传过来的数据
//        brokerid = getIntent().getIntExtra(BrokerMainActivity.BROKER_ID, 0);
        newcode = getIntent().getStringExtra(BuildingDanActivity.NEWCODE);
//        tranid = getIntent().getStringExtra(Constants.TRANID);
        titlebar = getIntent().getStringExtra(BuildingDanActivity.TITLEBAR);

        CustomTitleBar.getTitleBar(this, titlebar, true, false);
        setContentView(R.layout.activity_building_dan);

        //从我的会话中进传，其他场景时intent传值是可不写
        huxingtype = getIntent().getStringExtra("huxingtype");
        msgid = getIntent().getLongExtra("msgid",0);

        initView();

        requertData(newcode,huxingtype,msgid);

        initEvent();
    }

    private void initView() {

        iv_buiding = (ImageView) findViewById(R.id.building_img);//楼房图片
        tv_building_pricedesc = (TextView) findViewById(R.id.building_pricedesc);//楼房单价
        tv_buiding_position = (TextView) findViewById(R.id.buiding_position);//位置
        tv_broker_evaluation1 = (TextView) findViewById(R.id.broker_evaluation1);//楼盘评测
        tv_building_seenumber = (TextView) findViewById(R.id.building_seenumber);//看房次数
        tv_building_chengjiao = (TextView) findViewById(R.id.building_chengjiao);//成交量
        tv_buiding_brokersnum = (TextView) findViewById(R.id.buiding_brokersnum);//合作经纪人

        layout_tap = (RadioGroup) findViewById(R.id.layout_tap);//房型展示的GridView

        lv_fangyuan = (ListView) findViewById(R.id.lv_fangyuan);//房源显示的listview

        TextView button_l = (TextView) findViewById(R.id.dingdandeal);
        TextView button_r = (TextView) findViewById(R.id.lookhouse);

        String but_tv_l = getIntent().getStringExtra(But_Left);
        String but_tv_r = getIntent().getStringExtra(But_Right);

        button_l.setText(but_tv_l);
        button_r.setText(but_tv_r);

        if (but_tv_l.equals("申请取消")) {
            button_l.setOnClickListener(cancelKanfang);
        } else if (but_tv_l.equals("删除订单")) {
            button_l.setOnClickListener(deleceKnafang);
        }

        if (but_tv_r.equals("确认看房")) {
            button_r.setOnClickListener(confirmKanfang);
        } else if (but_tv_r.equals("投诉")) {
            button_r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(BuildingDanActivity.this, "投诉", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BuildingDanActivity.this, BrokerComplainActivity.class);
                    intent.putExtra(BrokerComplainActivity.RECID, String.valueOf(getIntent().getLongExtra(KanReqId, 0)));
                    intent.putExtra(BrokerComplainActivity.OPERDEST,"kanfang");
                    startActivity(intent);
                }
            });
        }
    }

    private View.OnClickListener cancelKanfang = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new HttpAsy(new PostExecute() {
                @Override
                public void onPostExecute(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                        if (state.equals("true")) {//取消成功
                            DialogUtil.showCancelSuccess(BuildingDanActivity.this, new AloneDeal() {
                                @Override
                                public void deal() {//重启页面
                                    Intent intent = new Intent(BuildingDanActivity.this, SeeHouseListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            DialogUtil.showCancelFailed(BuildingDanActivity.this, null);
                        }
                    }
                }
            }).execute(Constants.concelKanfangDanUrl + "?recid=" + getIntent().getLongExtra(KanReqId, 0));
        }
    };

    private View.OnClickListener deleceKnafang = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new HttpAsy(new PostExecute() {
                @Override
                public void onPostExecute(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                        if (state.equals("true")) {//删除成功
                            DialogUtil.showDealSuccess(BuildingDanActivity.this, "删除成功", new AloneDeal() {
                                @Override
                                public void deal() {
                                    Intent intent = new Intent(BuildingDanActivity.this, SeeHouseListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {//删除失败
                            DialogUtil.showDealFailed(BuildingDanActivity.this, "删除失败", null);
                        }
                    }
                }
            }).execute(Constants.deleteKanfangDanUrl + "?recid=" + getIntent().getLongExtra(KanReqId, 0) +"&roletype=user");
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
                                Intent intent = new Intent(BuildingDanActivity.this, BrokerEvaluationActivity.class);
                                intent.putExtra(BrokerEvaluationActivity.KanFangListStateResp_COMM, brokerComm);
                                startActivity(intent);
                                finish();
                            } else {
                                DialogUtil.showDealFailed(BuildingDanActivity.this, "确认失败", null);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).execute(Constants.confirmKanfangDanUrl + "?recid=" + getIntent().getLongExtra(KanReqId, 0));
        }
    };

    private void setDefaultFragment() {
        mIndicator = findViewById(R.id.indicatorView);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mIndicatorStepDistance = outMetrics.widthPixels;//设备宽度px
        mIndicatorStepDistance /= 3;
        mFm = getFragmentManager();
        FragmentTransaction mFragmentTrans = mFm.beginTransaction();
        //第一次加载界面
        bundle = new Bundle();
        bundle.putSerializable(HOUSEINFOBEAN, houseInfoBasedataResp);
        buildingfg = new BuildingFragment();
        buildingfg.setArguments(bundle);
        buildingfg.setLayout(R.layout.fragment1_building);
        mFragmentTrans.add(R.id.building_framelayout, buildingfg).commit();
    }


    /**
     * 请求主页数据
     * @param newcode   楼盘id   (必写)
     * @param huxingtype   户型类型，值有: 一室、二室、三室、四室、五室以上  （可写）
     * @param msgid  消息id   （可写）
     */
    private void requertData(String newcode,String huxingtype,long msgid) {
        Map<String,Object> map = new HashMap<>();
        map.put("newcode",newcode);
        map.put("huxingtype",huxingtype);
        if(msgid == 0){
            map.put("msgid",null);
        }else{
            map.put("msgid",msgid);
        }

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BuildingDanActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    ObjectMapper mapper = JacksonMapper.getObjectMapper();
                    try {
                        houseInfoBasedataResp = mapper.readValue(result, HouseInfoBasedataResp.class);
                        if (houseInfoBasedataResp == null)
                            return;

                        // 设置默认的Fragment
                        setDefaultFragment();
                        //添加数据
                        initData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.buidingMainUrl,map);
    }

    private void initData() {


        //加载图片
        String imageurl = houseInfoBasedataResp.getLoupan_image_url();
        if (!TextUtils.isEmpty(imageurl)) {
            ImageLoader.getInstance().displayImage(imageurl, iv_buiding, FangApplication.options, FangApplication.animateFirstListener);
        }
        //单价
        tv_building_pricedesc.setText(houseInfoBasedataResp.getPricedesc());
        //项目地址
        tv_buiding_position.setText(houseInfoBasedataResp.getAddress());
        //评测
        tv_broker_evaluation1.setText(houseInfoBasedataResp.getEvaluation_num().toString());
        //看房次数
        tv_building_seenumber.setText(houseInfoBasedataResp.getKanfang_time().toString());
        //成交次数
        tv_building_chengjiao.setText(houseInfoBasedataResp.getTran_success_num().toString());
        //合作经纪人
        tv_buiding_brokersnum.setText(houseInfoBasedataResp.getBroker_num().toString());

    }

    private void initEvent() {

        layout_tap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                FragmentTransaction mFragmentTrans = mFm.beginTransaction();
                TranslateAnimation anim = null;

                switch (checkedId) {
                    case R.id.building_layout:

                        mFragmentTrans.replace(R.id.building_framelayout, buildingfg);
                        mFragmentTrans.commit();
                        anim = new TranslateAnimation(mIndicatorStepDistance * mIndicatorPosition, 0, 0, 0);
                        mIndicatorPosition = 0;
                        break;
                    case R.id.building_parameter1:

                        BuildingFragment buildingFragment2 = new BuildingFragment();
                        buildingFragment2.setArguments(bundle);
                        buildingFragment2.setLayout(R.layout.fragment2_building);
                        mFragmentTrans.replace(R.id.building_framelayout, buildingFragment2);
                        mFragmentTrans.commit();
                        anim = new TranslateAnimation(mIndicatorStepDistance * mIndicatorPosition, mIndicatorStepDistance, 0, 0);
                        mIndicatorPosition = 1;
                        break;

                    case R.id.building_parameter2:

                        BuildingFragment buildingFragment3 = new BuildingFragment();
                        buildingFragment3.setArguments(bundle);
                        buildingFragment3.setLayout(R.layout.fragment3_building);
                        mFragmentTrans.replace(R.id.building_framelayout, buildingFragment3);
                        mFragmentTrans.commit();
                        anim = new TranslateAnimation(mIndicatorStepDistance * mIndicatorPosition, mIndicatorStepDistance * 2, 0, 0);
                        mIndicatorPosition = 2;
                        break;

                }
                if (anim != null) {
                    anim.setDuration(300);
                    anim.setFillAfter(true);
                    mIndicator.startAnimation(anim);
                }
            }
        });

    }
}
