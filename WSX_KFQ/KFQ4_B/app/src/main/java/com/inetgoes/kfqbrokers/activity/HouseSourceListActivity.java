package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.HouseInfo_BrokerIntroduce;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MySwipeMenuListView;
import com.inetgoes.kfqbrokers.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的所有房源  //TODO 这个类米有写完
 */
public class HouseSourceListActivity extends Activity implements RightDeal {

    private RadioGroup layoutTap;
    private RadioButton newHouse;
    private RadioButton secondHandHouse;
    private RadioButton rentHouse;
    private TextView but_upload;
    private FrameLayout flHouseList;

    private PullToRefreshScrollView scrollView;
    private MySwipeMenuListView lvMyHouseSource;
    private MyHouseSourceListAdapter adapter;

    private int pagenum = 10;//一次加载几个条目

    private int startIndex = 0;

    private int userid;

    private List<HouseInfo_BrokerIntroduce> houses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "我的房源", "编辑", this);
        setContentView(R.layout.activity_housesource_list);

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        initView();

        requestData("新房",startIndex, pagenum, false);
    }

    private void initView() {
        layoutTap = (RadioGroup) findViewById(R.id.layout_tap);
        newHouse = (RadioButton) findViewById(R.id.new_house);
        secondHandHouse = (RadioButton) findViewById(R.id.second_hand_house);
        rentHouse = (RadioButton) findViewById(R.id.rent_house);
        but_upload = (TextView) findViewById(R.id.but_upload);
        // flHouseList = (FrameLayout) findViewById(R.id.fl_house_list);

        scrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        lvMyHouseSource = (MySwipeMenuListView) findViewById(R.id.swipeMenuListView);//房源列表
        //设置SwipeMenuListView
        lvMyHouseSource.setMenuCreator(creator);
        lvMyHouseSource.setOnMenuItemClickListener(menuItemClickLis);

        View none_content = LayoutInflater.from(this).inflate(R.layout.none_content_hint,null);
        ((ImageView)none_content.findViewById(R.id.none_icon)).setImageResource(R.drawable.order1);
        ((TextView)none_content.findViewById(R.id.none_text)).setText("暂无房源");
        lvMyHouseSource.setEmptyView(none_content);

        adapter = new MyHouseSourceListAdapter();
        lvMyHouseSource.setAdapter(adapter);
        //lvMyHouseSource.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                startIndex += pagenum;
                requestData("新房",startIndex, pagenum, true);
            }
        });

        lvMyHouseSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(BuildingListActivity.this, BuildingMainActivity.class);
//                intent.putExtra(BuildingMainActivity.NEWCODE, houses.get(position - 1).getNewcode());
//                intent.putExtra(BuildingMainActivity.TITLEBAR, houses.get(position - 1).getLoupanname());
//                intent.putExtra(BrokerMainActivity.BROKER_ID, brokerid);
//                intent.putExtra(Constants.TRANID, tranid);
//                startActivity(intent);
            }
        });

        but_upload.setOnClickListener(new View.OnClickListener() {//上传
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseSourceListActivity.this, UploadHouseSourceMainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置SwipeMenuListView item侧滑菜单点击监听器
     */
    private SwipeMenuListView.OnMenuItemClickListener menuItemClickLis = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            //删除操作
            // Log.w("MyLog", "删除" + adapter.getNewCode(position));

            //接口暂时没有，现只是效果
            houses.remove(position);
            adapter.notifyDataSetInvalidated();
            // false : close the menu; true : not close the menu
            return false;
        }
    };

    //创建item侧滑菜单
    private SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(HouseSourceListActivity.this);
            deleteItem.setTitle("删除");
            deleteItem.setTitleSize(18);
            deleteItem.setTitleColor(Color.WHITE);
            //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.mate_color)));
            deleteItem.setWidth(130);
            menu.addMenuItem(deleteItem);
        }
    };

    /**
     *
     * @param brokertype brokertype   字符串, 代理类型: 值有: 新房、二手房、出租房   （必写）
     * @param startIndex
     * @param pagenum
     * @param isColse
     */
    private void requestData(String brokertype, int startIndex, int pagenum, final boolean isColse) {
        Map<String, Object> map = new HashMap<>();
        map.put("startindex", startIndex);
        map.put("pagenum", pagenum);
        map.put("brokerid", userid);
        map.put("brokertype", brokertype);

        try {
            L.LogE(JacksonMapper.getObjectMapper().writeValueAsString(map));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("requestData result -- " + result);
                if (!TextUtils.isEmpty(result)) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, HouseInfo_BrokerIntroduce.class);
                    List<HouseInfo_BrokerIntroduce> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        houses.addAll(temps);
                        if (isColse) {
                            scrollView.onRefreshComplete();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.showHouseSourceUrl, map);
    }


    private class MyHouseSourceListAdapter extends BaseAdapter {

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

                convertView = View.inflate(HouseSourceListActivity.this, R.layout.item_myhousesource, null);

                viewHolder.fangyuan_image = (ImageView) convertView.findViewById(R.id.item_iv_fangyuan);
                viewHolder.fangyuan_Name = (TextView) convertView.findViewById(R.id.item_tv_fangyuan1);
                viewHolder.fangyuan_cishu = (TextView) convertView.findViewById(R.id.item_tv_fangyuan2);
                viewHolder.fangyuan_SuccessNum = (TextView) convertView.findViewById(R.id.item_tv_fangyuan3);
                viewHolder.fangyuan_prices = (TextView) convertView.findViewById(R.id.item_tv_fangyuan4);
                viewHolder.fangyuan_position = (TextView) convertView.findViewById(R.id.item_tv_fangyuan5);

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



//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(HouseSourceListPushActivity.this, HouseSourcePreviewActivity.class);
////                    i.putExtra(HouseSourceListActivity.NEWCODE, hbi.getNewcode());
////                    i.putExtra(HouseSourceListActivity.BROKER_ID, brokerid);
////                    i.putExtra(HouseSourceListActivity.TITLEBAR, hbi.getLoupanname());
//                    startActivity(i);
//                }
//            });

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


    //Title右按钮
    @Override
    public void deal() {

    }
}
