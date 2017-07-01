package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.model.HouseInfo_BrokerIntroduce;
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
 * Created by sunhui on 2015/11/12.
 */
public class BuildingListActivity extends Activity {
    private PullToRefreshListView lv_Allbuiding;//所有房源的LV

    private AllBuidingListAdapter allBuidingListAdapter;

    private int pagenum = 10;//一次加载几个条目

    private int startIndex = 0;

    private String isconnected;


    private int brokerid;//经纪人id
    private String tranid;

    //private HouseInfo_BrokerIntroduce houseInfo;
    private List<HouseInfo_BrokerIntroduce> houses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "房源列表", true, false);
        setContentView(R.layout.activity_allbuidinglist);
        brokerid = getIntent().getIntExtra(BrokerMainActivity.BROKER_ID, 0);
        tranid = getIntent().getStringExtra(Constants.TRANID);

        isconnected = getIntent().getStringExtra("isconnected");
        if(null == isconnected){
            isconnected = "N";
        }

        initView();

        requestData(startIndex, pagenum, false);
    }


    private void initView() {

        lv_Allbuiding = (PullToRefreshListView) findViewById(R.id.lv_allbuiding);

        allBuidingListAdapter = new AllBuidingListAdapter();

        lv_Allbuiding.setAdapter(allBuidingListAdapter);
        lv_Allbuiding.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        lv_Allbuiding.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startIndex += pagenum;
                requestData(startIndex, pagenum, true);
            }
        });

    }

    private void requestData(int startIndex, int pagenum, final boolean isColse) {

        Map<String, Object> map = new HashMap<>();
        map.put("startindex", startIndex);
        map.put("pagenum", pagenum);
        map.put("brokerid", brokerid);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("BuidingListActivity", "result -- " + result);
                if (!TextUtils.isEmpty(result)) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, HouseInfo_BrokerIntroduce.class);
                    List<HouseInfo_BrokerIntroduce> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        houses.addAll(temps);
                        if (isColse) {
                            lv_Allbuiding.onRefreshComplete();
                        }
                        allBuidingListAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.allBuidingUrl, map);
    }


    private class AllBuidingListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return houses.size();
        }

        @Override
        public Object getItem(int position) {
            return houses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();

                convertView = View.inflate(BuildingListActivity.this, R.layout.item_broker_fangyuan, null);

                viewHolder.fangyuan_image = (ImageView) convertView.findViewById(R.id.item_iv_fangyuan);
                viewHolder.fangyuan_Name = (TextView) convertView
                        .findViewById(R.id.item_tv_fangyuan1);
                viewHolder.fangyuan_cishu = (TextView) convertView
                        .findViewById(R.id.item_tv_fangyuan2);
                viewHolder.fangyuan_SuccessNum = (TextView) convertView
                        .findViewById(R.id.item_tv_fangyuan3);
                viewHolder.fangyuan_prices = (TextView) convertView
                        .findViewById(R.id.item_tv_fangyuan4);
                viewHolder.fangyuan_position = (TextView) convertView
                        .findViewById(R.id.item_tv_fangyuan5);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            final HouseInfo_BrokerIntroduce hbi = houses.get(position);

            if (hbi != null) {

                //加载图片
                String imageurl = hbi.getLoupan_image_url();
                if (!TextUtils.isEmpty(imageurl)) {
                    ImageLoader.getInstance().displayImage(imageurl, viewHolder.fangyuan_image, FangApplication.options, FangApplication.animateFirstListener);
                }
                viewHolder.fangyuan_Name.setText(hbi.getLoupanname());
                viewHolder.fangyuan_cishu.setText("看房次数：" + hbi.getKanfang_time().toString());
                viewHolder.fangyuan_SuccessNum.setText("成交量：" + hbi.getTran_success_num().toString());
                viewHolder.fangyuan_prices.setText(hbi.getPricedesc());
                viewHolder.fangyuan_position.setText(hbi.getStrict());
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BuildingListActivity.this, BuildingMainActivity.class);
                    intent.putExtra(BuildingMainActivity.NEWCODE, hbi.getNewcode());
                    intent.putExtra(BuildingMainActivity.TITLEBAR, hbi.getLoupanname());
                    intent.putExtra(BrokerMainActivity.BROKER_ID, brokerid);
                    intent.putExtra(Constants.TRANID, tranid);

                    L.LogI("BuildingListAct tarnid is " + tranid);

                    intent.putExtra("isconnected", isconnected);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        /**
         * 房源的holder
         */
        class ViewHolder {
            ImageView fangyuan_image;
            TextView fangyuan_Name;
            TextView fangyuan_cishu;
            TextView fangyuan_SuccessNum;
            TextView fangyuan_prices;
            TextView fangyuan_position;
        }
    }
}