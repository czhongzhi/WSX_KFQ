package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.model.HouseInfoBasedataResp;
import com.inetgoes.kfqbrokers.utils.AloneDeal;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.view.BuildingFragment;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * todo 这个上传房源展示页  有3个fragment  第一个里面是MyGridView
 */
public class HouseSourcePreviewActivity extends Activity {

    public static final String NEWCODE = "buidingid";
    public static final String TITLEBAR = "title_bar";

    public static final String HOUSEINFOBEAN = "houseInfoBasedataResp";
    public HouseInfoBasedataResp houseInfoBasedataResp;//楼盘bean
    private int brokerid;
    private String tranid;
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
    private String titlebar;//房源名称
    private Bundle bundle;

    private Button btn_Submit;

    private ArrayList<Drawable> loufangImages;//上传房源图片集合

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取传过来的数据
//        brokerid = getIntent().getIntExtra(BrokerMainActivity.BROKER_ID, 0);
//        newcode = getIntent().getStringExtra(BuildingMainActivity.NEWCODE);
//        tranid = getIntent().getStringExtra(Constants.TRANID);
//        titlebar = getIntent().getStringExtra(BuildingMainActivity.TITLEBAR);


        //获取传递过来的Bean
        houseInfoBasedataResp = (HouseInfoBasedataResp) getIntent().getSerializableExtra("houseInfo");
        loufangImages = (ArrayList) getIntent().getSerializableExtra("imagelist");
        titlebar = houseInfoBasedataResp.getLoupanname();

        CustomTitleBar.getTitleBar(this, titlebar, true, false);
        setContentView(R.layout.activity_housesource_preview);

        initView();

        setDefaultFragment();

        initData();

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

        //lv_fangyuan = (ListView) findViewById(R.id.lv_fangyuan);//房源显示的listview
        btn_Submit = (Button) findViewById(R.id.btn_submit);//提交按钮

    }

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
        buildingfg.setLayout(R.layout.fragment1_building);//todo MyGridView
        mFragmentTrans.add(R.id.building_framelayout, buildingfg).commit();
    }


    private void subMitHouse() {
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        map.put("brokerid", brokerid);
        map.put("newcode", newcode);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Map<String, Object> map = JacksonMapper.getInstance().mapObjFromJson(result);
                    Log.e("czhongzhi", map.get("status") + "");
                    Log.e("czhongzhi", map.get("reason") + "");
                    if ((boolean) map.get("status")) {
                        AloneDeal dealLeft = new AloneDeal() {
                            @Override
                            public void deal() {//点击查看预约 跳转我的预约
                                HouseSourcePreviewActivity.this.startActivity(new Intent(HouseSourcePreviewActivity.this, HouseSourcePreviewActivity.class));
                                HouseSourcePreviewActivity.this.finish();
                            }
                        };
                        AloneDeal dealRight = new AloneDeal() {
                            @Override
                            public void deal() {//点击好的 跳转主页
                                HouseSourcePreviewActivity.this.startActivity(new Intent(HouseSourcePreviewActivity.this, MainActivity.class));
                                HouseSourcePreviewActivity.this.finish();
                            }
                        };
                        DialogUtil.showYuyueSuccess(HouseSourcePreviewActivity.this, dealLeft, dealRight);
                    } else {
                        Toast.makeText(HouseSourcePreviewActivity.this, "选择失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).execute(null, map);//todo 没有接口
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

        //提交房源
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subMitHouse();
            }
        });

        //房源的fragment
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
